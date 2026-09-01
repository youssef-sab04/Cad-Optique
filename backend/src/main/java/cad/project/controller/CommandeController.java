package cad.project.controller;

import cad.project.config.AppConstants;
import cad.project.playload.CommandeDTO;
import cad.project.playload.CommandeResponse;
import cad.project.service.CommandeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommandeController {

    @Autowired
    CommandeService commandeService;

    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE')")
    @Operation(summary = "Ajouter une commande")
    @PostMapping("/admin/commande/fournisseurs/{fournisseurId}")
    public ResponseEntity<CommandeDTO> addCommande(@Valid @RequestBody CommandeDTO commandeDTO,
                                                   @PathVariable Long fournisseurId){
        CommandeDTO commandeDTOSaved = commandeService.addCommande(fournisseurId, commandeDTO);
        return new ResponseEntity<>(commandeDTOSaved, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE')")
    @Operation(summary = "Valider une commande")
    @PostMapping("/admin/commande/{commandeId}")
    public ResponseEntity<CommandeDTO> ValiderCommande(@PathVariable Long commandeId){
        CommandeDTO commandeDTOSaved = commandeService.ValiderCommande(commandeId);
        return new ResponseEntity<>(commandeDTOSaved, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Modifier une commande")
    @PutMapping("/admin/commandes/{commandeId}")
    public ResponseEntity<CommandeDTO> updateCommande(@Valid @RequestBody CommandeDTO commandeDTO,
                                                      @PathVariable Long commandeId){
        CommandeDTO updatedCommandeDTO = commandeService.updateCommande(commandeId, commandeDTO);
        return new ResponseEntity<>(updatedCommandeDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE')")
    @Operation(summary = "Supprimer une commande")
    @DeleteMapping("/admin/commandes/{commandeId}")
    public ResponseEntity<CommandeDTO> deleteCommande(@PathVariable Long commandeId){
        CommandeDTO deletedCommande = commandeService.deleteCommande(commandeId);
        return new ResponseEntity<>(deletedCommande, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE')")
    @Operation(summary = "Get Commandes")
    @GetMapping("/public/commandes")
    public ResponseEntity<CommandeResponse> getAllCommandes(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ){
        CommandeResponse commandeResponse = commandeService.getAllCommandes(pageNumber, pageSize, sortOrder);
        return new ResponseEntity<>(commandeResponse, HttpStatus.OK);
    }
}