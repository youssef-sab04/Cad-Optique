package cad.project.service;

import cad.project.exceptions.APIException;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.Remboursement;
import cad.project.model.SalesOrder;
import cad.project.playload.RemboursementDTO;
import cad.project.playload.RemboursementResponse;
import cad.project.repositries.RemboursementRepositry;
import cad.project.repositries.SaleOrderRepositry;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RemboursementServiceImp implements RemboursementService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RemboursementRepositry remboursementRepositry;

    @Autowired
    private SaleOrderRepositry saleOrderRepositry;

    @Override
    public RemboursementDTO addRemboursement(Long orderId, RemboursementDTO remboursementDTO) {
        SalesOrder salesOrder = saleOrderRepositry.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "OrderId", orderId));

        remboursementRepositry.findBySalesOrder(salesOrder).ifPresent(r -> {
            throw new APIException("Un remboursement existe deja pour cette commande");
        });

        if (remboursementDTO.getMontant_mutuelle() == null || remboursementDTO.getMontant_patient() == null) {
            throw new APIException("Montant mutuelle et montant patient requis");
        }
        if (remboursementDTO.getMontant_mutuelle() < 0 || remboursementDTO.getMontant_patient() < 0) {
            throw new APIException("Montant invalide");
        }

        Remboursement remboursement = modelMapper.map(remboursementDTO, Remboursement.class);
        remboursement.setSalesOrder(salesOrder);
        remboursement.setStatus("en_attente");

        Remboursement remboursementSaved = remboursementRepositry.save(remboursement);

        return modelMapper.map(remboursementSaved, RemboursementDTO.class);
    }

    @Override
    public RemboursementDTO updateRemboursement(Long remboursementId, RemboursementDTO remboursementDTO) {
        Remboursement remboursement = remboursementRepositry.findById(remboursementId)
                .orElseThrow(() -> new ResourceNotFoundException("Remboursement", "RemboursementId", remboursementId));

        if (remboursementDTO.getMontant_mutuelle() != null) {
            if (remboursementDTO.getMontant_mutuelle() < 0) {
                throw new APIException("Montant invalide");
            }
            remboursement.setMontant_mutuelle(remboursementDTO.getMontant_mutuelle());
        }

        if (remboursementDTO.getMontant_patient() != null) {
            if (remboursementDTO.getMontant_patient() < 0) {
                throw new APIException("Montant invalide");
            }
            remboursement.setMontant_patient(remboursementDTO.getMontant_patient());
        }

        if (remboursementDTO.getDescription() != null) {
            remboursement.setDescription(remboursementDTO.getDescription());
        }

        Remboursement remboursementSaved = remboursementRepositry.save(remboursement);

        return modelMapper.map(remboursementSaved, RemboursementDTO.class);
    }

    @Override
    public RemboursementDTO deleteRemboursement(Long remboursementId) {
        Remboursement remboursement = remboursementRepositry.findById(remboursementId)
                .orElseThrow(() -> new ResourceNotFoundException("Remboursement", "RemboursementId", remboursementId));

        remboursementRepositry.delete(remboursement);

        return modelMapper.map(remboursement, RemboursementDTO.class);
    }

    @Override
    public RemboursementResponse getAllRemboursements(Integer pageNumber, Integer pageSize, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by("id").ascending() : Sort.by("id").descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Remboursement> pageRemboursements = remboursementRepositry.findAll(pageDetails);
        List<Remboursement> remboursements = pageRemboursements.getContent();

        List<RemboursementDTO> remboursementDTOS = remboursements.stream()
                .map(r -> modelMapper.map(r, RemboursementDTO.class))
                .toList();

        RemboursementResponse remboursementResponse = new RemboursementResponse();
        remboursementResponse.setContent(remboursementDTOS);
        remboursementResponse.setPageNumber(pageRemboursements.getNumber());
        remboursementResponse.setPageSize(pageRemboursements.getSize());
        remboursementResponse.setTotalElements(pageRemboursements.getTotalElements());
        remboursementResponse.setTotalPages(pageRemboursements.getTotalPages());
        remboursementResponse.setLastPage(pageRemboursements.isLast());

        return remboursementResponse;
    }

    @Override
    public RemboursementDTO getRemboursementByOrder(Long orderId) {
        SalesOrder salesOrder = saleOrderRepositry.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "OrderId", orderId));

        Remboursement remboursement = remboursementRepositry.findBySalesOrder(salesOrder)
                .orElseThrow(() -> new ResourceNotFoundException("Remboursement", "OrderId", orderId));

        return modelMapper.map(remboursement, RemboursementDTO.class);
    }

    @Override
    public RemboursementDTO marquerRecu(Long remboursementId) {
        Remboursement remboursement = remboursementRepositry.findById(remboursementId)
                .orElseThrow(() -> new ResourceNotFoundException("Remboursement", "RemboursementId", remboursementId));

        if (remboursement.getStatus().equals("recu")) {
            throw new APIException("Remboursement deja marque comme recu");
        }

        remboursement.setStatus("recu");
        Remboursement remboursementSaved = remboursementRepositry.save(remboursement);

        return modelMapper.map(remboursementSaved, RemboursementDTO.class);
    }
}
