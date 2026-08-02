package cad.project.controller;

import cad.project.config.AppConstants;
import cad.project.playload.CommandeItemDTO;
import cad.project.playload.CommandeItemResponse;
import cad.project.service.CommandeItemService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommandeItemController {

    @Autowired
    CommandeItemService commandeItemService;

    @Operation(summary = "Ajouter un item a une commande")
    @PostMapping("/admin/commandeItem/commandes/{commandeId}/produits/{produitId}")
    public ResponseEntity<CommandeItemDTO> addCommandeItem(
            @Valid @RequestBody CommandeItemDTO commandeItemDTO,
            @PathVariable Long commandeId,
            @PathVariable Long produitId,
            @RequestParam(required = false) Long ordonnanceLunetteId,
            @RequestParam(required = false) Long ordonnanceLentilleId
    ){
        CommandeItemDTO saved = commandeItemService.addCommandeItem(commandeId, produitId, ordonnanceLunetteId, ordonnanceLentilleId, commandeItemDTO);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @Operation(summary = "Modifier un item")
    @PutMapping("/admin/commandeItems/{commandeItemId}")
    public ResponseEntity<CommandeItemDTO> updateCommandeItem(@Valid @RequestBody CommandeItemDTO commandeItemDTO,
                                                              @PathVariable Long commandeItemId){
        CommandeItemDTO updated = commandeItemService.updateCommandeItem(commandeItemId, commandeItemDTO);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @Operation(summary = "Supprimer un item")
    @DeleteMapping("/admin/commandeItems/{commandeItemId}")
    public ResponseEntity<CommandeItemDTO> deleteCommandeItem(@PathVariable Long commandeItemId){
        CommandeItemDTO deleted = commandeItemService.deleteCommandeItem(commandeItemId);
        return new ResponseEntity<>(deleted, HttpStatus.OK);
    }

    @Operation(summary = "Get CommandeItems")
    @GetMapping("/public/commandeItems")
    public ResponseEntity<CommandeItemResponse> getAllCommandeItems(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY_NOUN, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ){
        CommandeItemResponse response = commandeItemService.getAllCommandeItems(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get CommandeItems par commande")
    @GetMapping("/public/commandeItems/commandes/{commandeId}")
    public ResponseEntity<List<CommandeItemDTO>> getCommandeItemsByCommandeId(@PathVariable Long commandeId){
        List<CommandeItemDTO> items = commandeItemService.getCommandeItemsByCommandeId(commandeId);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
}