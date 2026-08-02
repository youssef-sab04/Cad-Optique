package cad.project.service;

import cad.project.playload.FournisseurDTO;
import cad.project.playload.FournisseurResponse;
import jakarta.validation.Valid;

public interface FournisseurService {
    FournisseurDTO addFournisseur(@Valid FournisseurDTO fournisseurDTO);

    FournisseurDTO updateFournisseur(Long fournisseurId, @Valid FournisseurDTO fournisseurDTO);

    FournisseurDTO deleteFournisseur(Long fournisseurId);

    FournisseurResponse getAllFournisseurs(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}