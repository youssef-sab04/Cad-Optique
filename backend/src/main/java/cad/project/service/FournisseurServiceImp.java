package cad.project.service;

import cad.project.exceptions.APIException;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.Fournisseur;
import cad.project.playload.FournisseurDTO;
import cad.project.playload.FournisseurResponse;
import cad.project.repositries.FournisseurRepositry;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FournisseurServiceImp implements FournisseurService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FournisseurRepositry fournisseurRepositry;

    @Override
    public FournisseurDTO addFournisseur(FournisseurDTO fournisseurDTO) {
        Fournisseur fournisseur = modelMapper.map(fournisseurDTO, Fournisseur.class);
        Fournisseur fournisseurSaved = fournisseurRepositry.save(fournisseur);

        return modelMapper.map(fournisseurSaved, FournisseurDTO.class);
    }

    @Override
    public FournisseurDTO updateFournisseur(Long fournisseurId, FournisseurDTO fournisseurDTO) {
        Fournisseur fournisseurFromDb = fournisseurRepositry.findById(fournisseurId)
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur", "fournisseurId", fournisseurId));

        Fournisseur fournisseur = modelMapper.map(fournisseurDTO, Fournisseur.class);

        fournisseurFromDb.setNom(fournisseur.getNom());
        fournisseurFromDb.setPhoneNumber(fournisseur.getPhoneNumber());
        fournisseurFromDb.setAdresse(fournisseur.getAdresse());
        fournisseurFromDb.setEmail(fournisseur.getEmail());

        fournisseurRepositry.save(fournisseurFromDb);

        return modelMapper.map(fournisseurFromDb, FournisseurDTO.class);
    }

    @Override
    public FournisseurDTO deleteFournisseur(Long fournisseurId) {
        Fournisseur deletedFournisseur = fournisseurRepositry.findById(fournisseurId)
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur", "fournisseurId", fournisseurId));

        fournisseurRepositry.delete(deletedFournisseur);
        FournisseurDTO deletedFournisseurDTO = modelMapper.map(deletedFournisseur, FournisseurDTO.class);

        return deletedFournisseurDTO;
    }

    @Override
    public FournisseurResponse getAllFournisseurs(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Fournisseur> fournisseurPage = fournisseurRepositry.findAll(pageDetails);

        List<Fournisseur> fournisseurs = fournisseurPage.getContent();
        if (fournisseurs.isEmpty())
            throw new APIException("No Fournisseur Created till now");

        List<FournisseurDTO> fournisseurDTOS = fournisseurs.stream()
                .map(fournisseur -> modelMapper.map(fournisseur, FournisseurDTO.class))
                .toList();

        FournisseurResponse fournisseurResponse = new FournisseurResponse();
        fournisseurResponse.setContent(fournisseurDTOS);
        fournisseurResponse.setPageNumber(fournisseurPage.getNumber());
        fournisseurResponse.setPageSize(fournisseurPage.getSize());
        fournisseurResponse.setTotalElements(fournisseurPage.getTotalElements());
        fournisseurResponse.setTotalPages(fournisseurPage.getTotalPages());
        fournisseurResponse.setLastPage(fournisseurPage.isLast());
        return fournisseurResponse;
    }
}