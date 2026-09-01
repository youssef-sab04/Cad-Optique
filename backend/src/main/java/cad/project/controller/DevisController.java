package cad.project.controller;

import cad.project.config.AppConstants;
import cad.project.playload.DevisDTO;
import cad.project.playload.DevisResponse;
import cad.project.service.DevisPdfService;
import cad.project.service.DevisService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DevisController {

    @Autowired
    DevisService devisService;

    @Autowired
    DevisPdfService devisPdfService;

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Ajouter un devis")
    @PostMapping("/admin/devis/clients/{clientId}")
    public ResponseEntity<DevisDTO> addDevis(@Valid @RequestBody DevisDTO devisDTO,
                                                       @PathVariable Long clientId){
        DevisDTO devisDTOSaved = devisService.addDevis(clientId, devisDTO);
        return new ResponseEntity<>(devisDTOSaved, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Modifier un devis")
    @PutMapping("/admin/devis/{devisId}")
    public ResponseEntity<DevisDTO> updateDevis(@Valid @RequestBody DevisDTO devisDTO,
                                                          @PathVariable Long devisId){
        DevisDTO updatedDevisDTO = devisService.updateDevis(devisId, devisDTO);
        return new ResponseEntity<>(updatedDevisDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Supprimer un devis")
    @DeleteMapping("/admin/devis/{devisId}")
    public ResponseEntity<DevisDTO> deleteDevis(@PathVariable Long devisId){
        DevisDTO deletedDevis = devisService.deleteDevis(devisId);
        return new ResponseEntity<>(deletedDevis, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Get Devis")
    @GetMapping("/public/devis")
    public ResponseEntity<DevisResponse> getAllDevis(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ){
        DevisResponse devisResponse = devisService.getAllDevis(pageNumber, pageSize, sortOrder);
        return new ResponseEntity<>(devisResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Annuler un devis")
    @PostMapping("/admin/devis/{devisId}/cancel")
    public ResponseEntity<DevisDTO> cancelDevis(@PathVariable Long devisId){
        DevisDTO devisDTO = devisService.cancelDevis(devisId);
        return new ResponseEntity<>(devisDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Confirmer un devis")
    @PostMapping("/admin/devis/{devisId}/confirm")
    public ResponseEntity<DevisDTO> confirmDevis(@PathVariable Long devisId){
        DevisDTO devisDTO = devisService.confirmDevis(devisId);
        return new ResponseEntity<>(devisDTO, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Generer le PDF du devis")
    @GetMapping("/admin/devis/{devisId}/pdf")
    public ResponseEntity<byte[]> generateDevisPdf(@PathVariable Long devisId) {
        byte[] pdf = devisPdfService.generateDevisPdf(devisId);
        return ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=devis-" + devisId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }


}
