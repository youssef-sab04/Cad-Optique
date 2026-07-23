package cad.project.service;

import cad.project.playload.CommandeDTO;
import cad.project.playload.CommandeResponse;
import jakarta.validation.Valid;

public interface CommandeService {
    CommandeDTO addCommande(Long fournisseurId, @Valid CommandeDTO commandeDTO);

    CommandeDTO updateCommande(Long commandeId, @Valid CommandeDTO commandeDTO);

    CommandeDTO deleteCommande(Long commandeId);

    CommandeResponse getAllCommandes(Integer pageNumber, Integer pageSize, String sortOrder);

    CommandeDTO ValiderCommande(Long commandeId);
}