package cad.project.controller;

import cad.project.config.AppConstants;
import cad.project.playload.FournisseurDTO;
import cad.project.playload.FournisseurResponse;
import cad.project.service.FournisseurService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class FournisseurController {

    @Autowired
    FournisseurService fournisseurService;

    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE')")
    @Operation(summary = "Ajouter un fournisseur")
    @PostMapping("/admin/fournisseur")
    public ResponseEntity<FournisseurDTO> addFournisseur(@Valid @RequestBody FournisseurDTO fournisseurDTO){
        FournisseurDTO fournisseurDTOSaved = fournisseurService.addFournisseur(fournisseurDTO);
        return new ResponseEntity<>(fournisseurDTOSaved, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE')")
    @Operation(summary = "Modifier un fournisseur")
    @PutMapping("/admin/fournisseurs/{fournisseurId}")
    public ResponseEntity<FournisseurDTO> updateFournisseur(@Valid @RequestBody FournisseurDTO fournisseurDTO,
                                                            @PathVariable Long fournisseurId){
        FournisseurDTO updatedFournisseurDTO = fournisseurService.updateFournisseur(fournisseurId, fournisseurDTO);
        return new ResponseEntity<>(updatedFournisseurDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE')")
    @Operation(summary = "Supprimer un fournisseur")
    @DeleteMapping("/admin/fournisseurs/{fournisseurId}")
    public ResponseEntity<FournisseurDTO> deleteFournisseur(@PathVariable Long fournisseurId){
        FournisseurDTO deletedFournisseur = fournisseurService.deleteFournisseur(fournisseurId);
        return new ResponseEntity<>(deletedFournisseur, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE')")
    @Operation(summary = "Get Fournisseurs")
    @GetMapping("/public/fournisseurs")
    public ResponseEntity<FournisseurResponse> getAllFournisseurs(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY_NOUN, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ){
        FournisseurResponse fournisseurResponse = fournisseurService.getAllFournisseurs(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(fournisseurResponse, HttpStatus.OK);
    }
}