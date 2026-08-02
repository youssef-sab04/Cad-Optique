package cad.project.service;

import cad.project.playload.RemboursementDTO;
import cad.project.playload.RemboursementResponse;
import jakarta.validation.Valid;

public interface RemboursementService {

    RemboursementDTO addRemboursement(Long orderId, @Valid RemboursementDTO remboursementDTO);

    RemboursementDTO updateRemboursement(Long remboursementId, @Valid RemboursementDTO remboursementDTO);

    RemboursementDTO deleteRemboursement(Long remboursementId);

    RemboursementResponse getAllRemboursements(Integer pageNumber, Integer pageSize, String sortOrder);

    RemboursementDTO getRemboursementByOrder(Long orderId);

    RemboursementDTO marquerRecu(Long remboursementId);
}
