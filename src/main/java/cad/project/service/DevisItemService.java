package cad.project.service;

import cad.project.playload.DevisItemDTO;
import cad.project.playload.DevisItemResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface DevisItemService {
    DevisItemDTO addDevisItem(Long devisId, Long produitId, @Valid DevisItemDTO devisItemDTO);

    DevisItemDTO updateDevisItemQuantity(Long devisItemId, @Valid DevisItemDTO devisItemDTO);

    DevisItemDTO deleteDevisItem(Long devisItemId);

    DevisItemResponse getAllDevisItems(Integer pageNumber, Integer pageSize, String sortOrder);

    List<DevisItemDTO> getDevisItemsByDevisId(Long devisId);
}
