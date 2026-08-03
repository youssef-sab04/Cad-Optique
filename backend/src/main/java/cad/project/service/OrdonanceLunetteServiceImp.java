package cad.project.service;


import cad.project.exceptions.APIException;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.Client;
import cad.project.model.OrdonnanceLunette;
import cad.project.playload.ClientDTO;
import cad.project.playload.ClientResponse;
import cad.project.playload.OrdonnanceLunetteDTO;
import cad.project.repositries.ClientRepositry;
import cad.project.repositries.OrdonanceLunetteRepositry;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class OrdonanceLunetteServiceImp implements  OrdonanceLunetteService{

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OrdonanceLunetteRepositry ordonanceLunetteRepositry;

    @Autowired
    private ClientRepositry clientRepositry;

    @Autowired
    private FileServiceImp fileServiceImp;


    @Override
    public OrdonnanceLunetteDTO AddOrdLun(Long clientId, OrdonnanceLunetteDTO ordonnanceLunetteDTO) {

        Client client = clientRepositry.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "ClientId", clientId));

        List<OrdonnanceLunette> ordonnanceLunetteList = ordonanceLunetteRepositry.findByClientId(clientId);

        boolean isOrNotPresent = true;

        for (OrdonnanceLunette value : ordonnanceLunetteList) {
            if (value.getPrescripteur() != null && value.getDateEmission() != null
                    && value.getPrescripteur().equalsIgnoreCase(ordonnanceLunetteDTO.getPrescripteur())
                    && value.getDateEmission().equals(ordonnanceLunetteDTO.getDateEmission())
            ) {
                isOrNotPresent = false;
                break;
            }
        }
        if(isOrNotPresent){
            OrdonnanceLunette OrdonnanceLunette = modelMapper.map(ordonnanceLunetteDTO , OrdonnanceLunette.class);

            OrdonnanceLunette.setClient(client);
            OrdonnanceLunette.setDateExpiration(OrdonnanceLunette.getDateEmission().plusYears(2));
            OrdonnanceLunette OrdonnanceLunetteSaved = ordonanceLunetteRepositry.save(OrdonnanceLunette);

            OrdonnanceLunetteDTO ordonnanceLunetteDTOsaved = modelMapper.map(OrdonnanceLunetteSaved , OrdonnanceLunetteDTO.class);
            ordonnanceLunetteDTOsaved.setClientDTO(modelMapper.map(client , ClientDTO.class));

            return ordonnanceLunetteDTOsaved;
        }
        else {
            throw new APIException("Ordonance already exist!!");
        }
    }

    public OrdonnanceLunetteDTO addScanOrd(Long clientId , MultipartFile image) throws IOException {

        Client client = clientRepositry.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "ClientId", clientId));

        OrdonnanceLunette ordonnanceLunette = new OrdonnanceLunette();

        String imageUrl = fileServiceImp.uploadImage(image);
        ordonnanceLunette.setImage(imageUrl);

        ordonnanceLunette.setClient(client);
        ordonanceLunetteRepositry.save(ordonnanceLunette);

        return modelMapper.map(ordonnanceLunette , OrdonnanceLunetteDTO.class);
    }

    @Override
    public ClientResponse getAlOrdonance(Integer pageNumber, Integer pageSize, String sortOrder , String keyword) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by("dateEmission").ascending()
                : Sort.by("dateEmission").descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);



        Specification<OrdonnanceLunette> spec = (root, query, cb) -> cb.conjunction();
        if (keyword != null && !keyword.isEmpty()) {
            String kw = keyword.trim().toLowerCase().replaceAll("\\s+", " ");

            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(cb.concat(cb.concat(root.get("client").get("nom"), " "), root.get("client").get("prenom"))), "%" + kw + "%"),
                    cb.like(cb.lower(cb.concat(cb.concat(root.get("client").get("prenom"), " "), root.get("client").get("nom"))), "%" + kw + "%")
            ));
        }

        Page<OrdonnanceLunette> ordonnanceLunettesL = ordonanceLunetteRepositry.findAll(spec , pageDetails);

        List<OrdonnanceLunette> ordonnanceLunettes= ordonnanceLunettesL.getContent();

        List<OrdonnanceLunetteDTO> ordonnanceLunetteDTOS = ordonnanceLunettes.stream()
                .map(Ord -> {
                    OrdonnanceLunetteDTO ordonnanceLunetteDTO = modelMapper.map(Ord, OrdonnanceLunetteDTO.class);
                    ordonnanceLunetteDTO.setImage(Ord.getImage());
                    ordonnanceLunetteDTO.setClientDTO(modelMapper.map(Ord.getClient() , ClientDTO.class));
                    return ordonnanceLunetteDTO;
                })
                .toList();

        if(ordonnanceLunettes.isEmpty()){
            throw new APIException("Aucun ordonance !");
        }
        ClientResponse clientResponse = new ClientResponse();
        clientResponse.setOrdonnanceLunetteDTOS(ordonnanceLunetteDTOS);
        clientResponse.setPageNumber(ordonnanceLunettesL.getNumber());
        clientResponse.setPageSize(ordonnanceLunettesL.getSize());
        clientResponse.setTotalElements(ordonnanceLunettesL.getTotalElements());
        clientResponse.setTotalPages(ordonnanceLunettesL.getTotalPages());
        clientResponse.setLastPage(ordonnanceLunettesL.isLast());
        return  clientResponse;

    }

    @Override
    public OrdonnanceLunetteDTO UpdateOrdLun(Long ordonanceId, OrdonnanceLunetteDTO ordonnanceLunetteDTO) {
        OrdonnanceLunette ordonnanceLunetteFromDb = ordonanceLunetteRepositry.findById(ordonanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Ordonance", "OrdonanceId", ordonanceId));

        ordonnanceLunetteFromDb.setPrescripteur(ordonnanceLunetteDTO.getPrescripteur());
        ordonnanceLunetteFromDb.setDateEmission(ordonnanceLunetteDTO.getDateEmission());
        ordonnanceLunetteFromDb.setDateExpiration(ordonnanceLunetteDTO.getDateExpiration());
        ordonnanceLunetteFromDb.setSphereOd(ordonnanceLunetteDTO.getSphereOd());
        ordonnanceLunetteFromDb.setCylindreOd(ordonnanceLunetteDTO.getCylindreOd());
        ordonnanceLunetteFromDb.setAxeOd(ordonnanceLunetteDTO.getAxeOd());
        ordonnanceLunetteFromDb.setAdditionOd(ordonnanceLunetteDTO.getAdditionOd());
        ordonnanceLunetteFromDb.setSphereOg(ordonnanceLunetteDTO.getSphereOg());
        ordonnanceLunetteFromDb.setCylindreOg(ordonnanceLunetteDTO.getCylindreOg());
        ordonnanceLunetteFromDb.setAxeOg(ordonnanceLunetteDTO.getAxeOg());
        ordonnanceLunetteFromDb.setAdditionOg(ordonnanceLunetteDTO.getAdditionOg());
        ordonnanceLunetteFromDb.setImage(ordonnanceLunetteDTO.getImage());

        ordonanceLunetteRepositry.save(ordonnanceLunetteFromDb);

        return modelMapper.map(ordonnanceLunetteFromDb , OrdonnanceLunetteDTO.class);
    }

    @Override
    public OrdonnanceLunetteDTO DeleteOrdLun(Long ordonanceId) {
        OrdonnanceLunette ordonnanceLunette = ordonanceLunetteRepositry.findById(ordonanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Ordonance", "OrdonanceId", ordonanceId));

        ordonanceLunetteRepositry.delete(ordonnanceLunette);
        return modelMapper.map(ordonnanceLunette, OrdonnanceLunetteDTO.class);
    }

    @Override
    public OrdonnanceLunetteDTO getOrdLunById(Long ordonanceId) {
        OrdonnanceLunette ordonnanceLunette = ordonanceLunetteRepositry.findById(ordonanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Ordonnance", "OrdonnanceId", ordonanceId));
        Client client = ordonnanceLunette.getClient();

        OrdonnanceLunetteDTO ordonnanceLunetteDTO = modelMapper.map(ordonnanceLunette, OrdonnanceLunetteDTO.class);
        ordonnanceLunetteDTO.setClientDTO(modelMapper.map(client, ClientDTO.class));

        return ordonnanceLunetteDTO;
    }

    @Override
    public OrdonnanceLunetteDTO AddOrdLunAvecImage(Long clientId, OrdonnanceLunetteDTO ordonnanceLunetteDTO, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            String imageUrl = fileServiceImp.uploadImage(image);
            ordonnanceLunetteDTO.setImage(imageUrl);
        }
        return AddOrdLun(clientId, ordonnanceLunetteDTO);
    }


}