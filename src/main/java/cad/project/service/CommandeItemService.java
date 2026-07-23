package cad.project.service;

import cad.project.playload.CommandeItemDTO;
import cad.project.playload.CommandeItemResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface CommandeItemService {
    CommandeItemDTO addCommandeItem(Long commandeId, Long produitId, Long ordonnanceLunetteId, Long ordonnanceLentilleId, @Valid CommandeItemDTO commandeItemDTO);

    CommandeItemDTO updateCommandeItem(Long commandeItemId, @Valid CommandeItemDTO commandeItemDTO);

    CommandeItemDTO deleteCommandeItem(Long commandeItemId);

    CommandeItemResponse getAllCommandeItems(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    List<CommandeItemDTO> getCommandeItemsByCommandeId(Long commandeId);
}