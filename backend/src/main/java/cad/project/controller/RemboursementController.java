package cad.project.controller;

import cad.project.config.AppConstants;
import cad.project.playload.RemboursementDTO;
import cad.project.playload.RemboursementResponse;
import cad.project.service.RemboursementService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RemboursementController {

    @Autowired
    RemboursementService remboursementService;


    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Ajouter un remboursement")
    @PostMapping("/admin/remboursements/orders/{orderId}")
    public ResponseEntity<RemboursementDTO> addRemboursement(@Valid @RequestBody RemboursementDTO remboursementDTO,
                                                               @PathVariable Long orderId) {
        RemboursementDTO remboursementDTOSaved = remboursementService.addRemboursement(orderId, remboursementDTO);
        return new ResponseEntity<>(remboursementDTOSaved, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Modifier un remboursement")
    @PutMapping("/admin/remboursements/{remboursementId}")
    public ResponseEntity<RemboursementDTO> updateRemboursement(@Valid @RequestBody RemboursementDTO remboursementDTO,
                                                                  @PathVariable Long remboursementId) {
        RemboursementDTO updatedRemboursementDTO = remboursementService.updateRemboursement(remboursementId, remboursementDTO);
        return new ResponseEntity<>(updatedRemboursementDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Supprimer un remboursement")
    @DeleteMapping("/admin/remboursements/{remboursementId}")
    public ResponseEntity<RemboursementDTO> deleteRemboursement(@PathVariable Long remboursementId) {
        RemboursementDTO deletedRemboursement = remboursementService.deleteRemboursement(remboursementId);
        return new ResponseEntity<>(deletedRemboursement, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Get Remboursements")
    @GetMapping("/public/remboursements")
    public ResponseEntity<RemboursementResponse> getAllRemboursements(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) {
        RemboursementResponse remboursementResponse = remboursementService.getAllRemboursements(pageNumber, pageSize, sortOrder);
        return new ResponseEntity<>(remboursementResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Get Remboursement par commande")
    @GetMapping("/public/remboursements/orders/{orderId}")
    public ResponseEntity<RemboursementDTO> getRemboursementByOrder(@PathVariable Long orderId) {
        RemboursementDTO remboursementDTO = remboursementService.getRemboursementByOrder(orderId);
        return new ResponseEntity<>(remboursementDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Marquer remboursement comme recu")
    @PutMapping("/admin/remboursements/{remboursementId}/recu")
    public ResponseEntity<RemboursementDTO> marquerRecu(@PathVariable Long remboursementId) {
        RemboursementDTO remboursementDTO = remboursementService.marquerRecu(remboursementId);
        return new ResponseEntity<>(remboursementDTO, HttpStatus.OK);
    }
}
