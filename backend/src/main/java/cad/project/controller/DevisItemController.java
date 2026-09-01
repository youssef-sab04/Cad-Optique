package cad.project.controller;

import cad.project.config.AppConstants;
import cad.project.playload.DevisItemDTO;
import cad.project.playload.DevisItemResponse;
import cad.project.service.DevisItemService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DevisItemController {

    @Autowired
    DevisItemService devisItemService;

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Ajouter un item a un devis")
    @PostMapping("/admin/devisitems/devis/{devisId}/produits/{produitId}")
    public ResponseEntity<DevisItemDTO> addDevisItem(
            @Valid @RequestBody DevisItemDTO devisItemDTO,
            @PathVariable Long devisId,
            @PathVariable Long produitId
    ){
        DevisItemDTO saved = devisItemService.addDevisItem(devisId, produitId, devisItemDTO);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Modifier un item")
    @PutMapping("/admin/devisitems/{devisItemId}")
    public ResponseEntity<DevisItemDTO> updateDevisItem(@Valid @RequestBody DevisItemDTO devisItemDTO,
                                                                   @PathVariable Long devisItemId){
        DevisItemDTO updated = devisItemService.updateDevisItemQuantity(devisItemId, devisItemDTO);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Supprimer un item")
    @DeleteMapping("/admin/devisitems/{devisItemId}")
    public ResponseEntity<DevisItemDTO> deleteDevisItem(@PathVariable Long devisItemId){
        DevisItemDTO deleted = devisItemService.deleteDevisItem(devisItemId);
        return new ResponseEntity<>(deleted, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Get DevisItems")
    @GetMapping("/public/devisitems")
    public ResponseEntity<DevisItemResponse> getAllDevisItems(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ){
        DevisItemResponse response = devisItemService.getAllDevisItems(pageNumber, pageSize, sortOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Get DevisItems par devis")
    @GetMapping("/public/devisitems/devis/{devisId}")
    public ResponseEntity<List<DevisItemDTO>> getDevisItemsByDevisId(@PathVariable Long devisId){
        List<DevisItemDTO> items = devisItemService.getDevisItemsByDevisId(devisId);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
}
