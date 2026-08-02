package cad.project.service;

import cad.project.exceptions.APIException;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.Client;
import cad.project.model.Examen;
import cad.project.playload.ClientDTO;
import cad.project.playload.ClientResponse;
import cad.project.playload.ExamenDTO;
import cad.project.repositries.ClientRepositry;
import cad.project.repositries.ExamenRepository;
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
public class ExamenServiceImp implements ExamenService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ExamenRepository examenRepository;

    @Autowired
    private ClientRepositry clientRepositry;

    @Override
    public ExamenDTO AddExamen(Long clientId, ExamenDTO examenDTO) {

        Client client = clientRepositry.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "ClientId", clientId));

        Examen examen = modelMapper.map(examenDTO, Examen.class);
        examen.setProchaineVisite(examen.getDateExamen().plusYears(2));
        examen.setClient(client);
        client.setDernierExamen(examen.getDateExamen());

        Examen examenSaved = examenRepository.save(examen);

        ExamenDTO examenDTOSaved = modelMapper.map(examenSaved, ExamenDTO.class);
        examenDTOSaved.setClientDTO(modelMapper.map(client, ClientDTO.class));

        return examenDTOSaved;
    }

    @Override
    public ClientResponse getAllExamens(Integer pageNumber, Integer pageSize,  String sortOrder, String keyword) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by("dateExamen").ascending()
                : Sort.by("dateExamen").descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Specification<Examen> spec = (root, query, cb) -> cb.conjunction();
        if (keyword != null && !keyword.isEmpty()) {
            String kw = keyword.trim().toLowerCase().replaceAll("\\s+", " ");

            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(cb.concat(cb.concat(root.get("client").get("nom"), " "), root.get("client").get("prenom"))), "%" + kw + "%"),
                    cb.like(cb.lower(cb.concat(cb.concat(root.get("client").get("prenom"), " "), root.get("client").get("nom"))), "%" + kw + "%")
            ));
        }

        Page<Examen> examensPage = examenRepository.findAll(spec, pageDetails);

        List<Examen> examens = examensPage.getContent();

        List<ExamenDTO> examenDTOS = examens.stream()
                .map(ex -> {
                    ExamenDTO dto = modelMapper.map(ex, ExamenDTO.class);
                    dto.setClientDTO(modelMapper.map(ex.getClient(), ClientDTO.class));
                    return dto;
                })
                .toList();

        if (examens.isEmpty()) {
            throw new APIException("Aucun examen trouvé !");
        }

        ClientResponse clientResponse = new ClientResponse();
        clientResponse.setExamenDTOS(examenDTOS);
        clientResponse.setPageNumber(examensPage.getNumber());
        clientResponse.setPageSize(examensPage.getSize());
        clientResponse.setTotalElements(examensPage.getTotalElements());
        clientResponse.setTotalPages(examensPage.getTotalPages());
        clientResponse.setLastPage(examensPage.isLast());

        return clientResponse;
    }

    @Override
    public ExamenDTO UpdateExamen(Long examenId, ExamenDTO examenDTO) {

        Examen examenFromDb = examenRepository.findById(examenId)
                .orElseThrow(() -> new ResourceNotFoundException("Examen", "ExamenId", examenId));

        examenFromDb.setDateExamen(examenDTO.getDateExamen());
        examenFromDb.setSphereOd(examenDTO.getSphereOd());
        examenFromDb.setCylindreOd(examenDTO.getCylindreOd());
        examenFromDb.setAxeOd(examenDTO.getAxeOd());
        examenFromDb.setEcartOd(examenDTO.getEcartOd());
        examenFromDb.setSphereOg(examenDTO.getSphereOg());
        examenFromDb.setCylindreOg(examenDTO.getCylindreOg());
        examenFromDb.setAxeOg(examenDTO.getAxeOg());
        examenFromDb.setEcartOg(examenDTO.getEcartOg());
        examenFromDb.setAddition(examenDTO.getAddition());
        examenFromDb.setRemarques(examenDTO.getRemarques());
        examenFromDb.setProchaineVisite(examenDTO.getProchaineVisite());

        examenRepository.save(examenFromDb);

        return modelMapper.map(examenFromDb, ExamenDTO.class);
    }

    @Override
    public ExamenDTO DeleteExamen(Long examenId) {

        Examen examen = examenRepository.findById(examenId)
                .orElseThrow(() -> new ResourceNotFoundException("Examen", "ExamenId", examenId));

        examenRepository.delete(examen);

        return modelMapper.map(examen, ExamenDTO.class);
    }

    @Override
    public ExamenDTO getExamen(Long examenId) {
        Examen examen = examenRepository.findById(examenId)
                .orElseThrow(() -> new ResourceNotFoundException("Examen", "ExamenId", examenId));
        Client client = examen.getClient();

        ExamenDTO examenDTO = modelMapper.map(examen, ExamenDTO.class);
        examenDTO.setClientDTO(modelMapper.map(client, ClientDTO.class));

        return examenDTO;
    }
}