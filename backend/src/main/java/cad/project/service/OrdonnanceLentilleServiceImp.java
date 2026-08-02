package cad.project.service;

import cad.project.exceptions.APIException;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.Client;
import cad.project.model.Examen;
import cad.project.model.OrdonnanceLentille;
import cad.project.playload.ClientDTO;
import cad.project.playload.ClientResponse;
import cad.project.playload.OrdonnanceLentilleDTO;
import cad.project.repositries.ClientRepositry;
import cad.project.repositries.OrdonnanceLentilleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdonnanceLentilleServiceImp implements OrdonnanceLentilleService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OrdonnanceLentilleRepository ordonnanceLentilleRepository;

    @Autowired
    private ClientRepositry clientRepositry;

    @Override
    public OrdonnanceLentilleDTO AddOrdLent(Long clientId, OrdonnanceLentilleDTO ordonnanceLentilleDTO) {

        Client client = clientRepositry.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "ClientId", clientId));

        List<OrdonnanceLentille> ordonnanceLentilleList = ordonnanceLentilleRepository.findByClientId(clientId);

        boolean isOrNotPresent = true;

        for (OrdonnanceLentille value : ordonnanceLentilleList) {
            if (value.getPrescripteur().equalsIgnoreCase(ordonnanceLentilleDTO.getPrescripteur())
                    && value.getDateEmission().equals(ordonnanceLentilleDTO.getDateEmission())
            ) {
                isOrNotPresent = false;
                break;
            }
        }

        if (isOrNotPresent) {
            OrdonnanceLentille ordonnanceLentille = modelMapper.map(ordonnanceLentilleDTO, OrdonnanceLentille.class);
            ordonnanceLentille.setClient(client);

            OrdonnanceLentille ordonnanceLentilleSaved = ordonnanceLentilleRepository.save(ordonnanceLentille);

            OrdonnanceLentilleDTO ordonnanceLentilleDTOSaved = modelMapper.map(ordonnanceLentilleSaved, OrdonnanceLentilleDTO.class);
            ordonnanceLentilleDTOSaved.setClientDTO(modelMapper.map(client, ClientDTO.class));

            return ordonnanceLentilleDTOSaved;
        } else {
            throw new APIException("Ordonnance déjà existante !");
        }
    }

    @Override
    public ClientResponse getAllOrdonnances(Integer pageNumber, Integer pageSize,  String sortOrder, String keyword) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by("dateEmission").ascending()
                : Sort.by("dateEmission").descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Specification<OrdonnanceLentille> spec = (root, query, cb) -> cb.conjunction();
        if (keyword != null && !keyword.isEmpty()) {
            String kw = keyword.trim().toLowerCase().replaceAll("\\s+", " ");

            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(cb.concat(cb.concat(root.get("client").get("nom"), " "), root.get("client").get("prenom"))), "%" + kw + "%"),
                    cb.like(cb.lower(cb.concat(cb.concat(root.get("client").get("prenom"), " "), root.get("client").get("nom"))), "%" + kw + "%")
            ));
        }

        Page<OrdonnanceLentille> ordonnanceLentillesPage = ordonnanceLentilleRepository.findAll(spec, pageDetails);

        List<OrdonnanceLentille> ordonnanceLentilles = ordonnanceLentillesPage.getContent();

        List<OrdonnanceLentilleDTO> ordonnanceLentilleDTOS = ordonnanceLentilles.stream()
                .map(ord -> {
                    OrdonnanceLentilleDTO dto = modelMapper.map(ord, OrdonnanceLentilleDTO.class);
                    dto.setClientDTO(modelMapper.map(ord.getClient(), ClientDTO.class));
                    return dto;
                })
                .toList();

        if (ordonnanceLentilles.isEmpty()) {
            throw new APIException("Aucune ordonnance trouvée !");
        }

        ClientResponse clientResponse = new ClientResponse();
        clientResponse.setOrdonnanceLentilleDTOS(ordonnanceLentilleDTOS);
        clientResponse.setPageNumber(ordonnanceLentillesPage.getNumber());
        clientResponse.setPageSize(ordonnanceLentillesPage.getSize());
        clientResponse.setTotalElements(ordonnanceLentillesPage.getTotalElements());
        clientResponse.setTotalPages(ordonnanceLentillesPage.getTotalPages());
        clientResponse.setLastPage(ordonnanceLentillesPage.isLast());

        return clientResponse;
    }

    @Override
    public OrdonnanceLentilleDTO UpdateOrdLent(Long ordonnanceId, OrdonnanceLentilleDTO ordonnanceLentilleDTO) {

        OrdonnanceLentille ordonnanceLentilleFromDb = ordonnanceLentilleRepository.findById(ordonnanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Ordonnance", "OrdonnanceId", ordonnanceId));

        ordonnanceLentilleFromDb.setPrescripteur(ordonnanceLentilleDTO.getPrescripteur());
        ordonnanceLentilleFromDb.setDateEmission(ordonnanceLentilleDTO.getDateEmission());
        ordonnanceLentilleFromDb.setDateExpiration(ordonnanceLentilleDTO.getDateExpiration());

        ordonnanceLentilleFromDb.setSphereOd(ordonnanceLentilleDTO.getSphereOd());
        ordonnanceLentilleFromDb.setCylindreOd(ordonnanceLentilleDTO.getCylindreOd());
        ordonnanceLentilleFromDb.setAxeOd(ordonnanceLentilleDTO.getAxeOd());
        ordonnanceLentilleFromDb.setRayonOd(ordonnanceLentilleDTO.getRayonOd());
        ordonnanceLentilleFromDb.setDiametreOd(ordonnanceLentilleDTO.getDiametreOd());
        ordonnanceLentilleFromDb.setMatiereOd(ordonnanceLentilleDTO.getMatiereOd());

        ordonnanceLentilleFromDb.setSphereOg(ordonnanceLentilleDTO.getSphereOg());
        ordonnanceLentilleFromDb.setCylindreOg(ordonnanceLentilleDTO.getCylindreOg());
        ordonnanceLentilleFromDb.setAxeOg(ordonnanceLentilleDTO.getAxeOg());
        ordonnanceLentilleFromDb.setRayonOg(ordonnanceLentilleDTO.getRayonOg());
        ordonnanceLentilleFromDb.setDiametreOg(ordonnanceLentilleDTO.getDiametreOg());
        ordonnanceLentilleFromDb.setMatiereOg(ordonnanceLentilleDTO.getMatiereOg());

        ordonnanceLentilleFromDb.setImage(ordonnanceLentilleDTO.getImage());

        ordonnanceLentilleRepository.save(ordonnanceLentilleFromDb);

        return modelMapper.map(ordonnanceLentilleFromDb, OrdonnanceLentilleDTO.class);
    }

    @Override
    public OrdonnanceLentilleDTO DeleteOrdLent(Long ordonnanceId) {

        OrdonnanceLentille ordonnanceLentille = ordonnanceLentilleRepository.findById(ordonnanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Ordonnance", "OrdonnanceId", ordonnanceId));

        ordonnanceLentilleRepository.delete(ordonnanceLentille);

        return modelMapper.map(ordonnanceLentille, OrdonnanceLentilleDTO.class);
    }

    @Override
    public OrdonnanceLentilleDTO getOrdLenById(Long ordonanceId) {
        OrdonnanceLentille ordonnanceLentille = ordonnanceLentilleRepository.findById(ordonanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Ordonnance", "OrdonnanceId", ordonanceId));
        Client client = ordonnanceLentille.getClient();

        OrdonnanceLentilleDTO ordonnanceLentilleDTO = modelMapper.map(ordonnanceLentille, OrdonnanceLentilleDTO.class);
        ordonnanceLentilleDTO.setClientDTO(modelMapper.map(client , ClientDTO.class));

        return ordonnanceLentilleDTO;
    }
}