package cad.project.controller;

import cad.project.config.AppConstants;
import cad.project.playload.SalesOrderItemDTO;
import cad.project.playload.SalesOrderItemResponse;
import cad.project.service.SalesOrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SalesOrderItemController {

    @Autowired
    SalesOrderItemService salesOrderItemService;

    @Operation(summary = "Ajouter un item a une vente")
    @PostMapping("/admin/salesorderitems/salesorders/{salesOrderId}/produits/{produitId}")
    public ResponseEntity<SalesOrderItemDTO> addSalesOrderItem(
            @Valid @RequestBody SalesOrderItemDTO salesOrderItemDTO,
            @PathVariable Long salesOrderId,
            @PathVariable Long produitId
    ){
        SalesOrderItemDTO saved = salesOrderItemService.addSalesOrderItem(salesOrderId, produitId, salesOrderItemDTO);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @Operation(summary = "Modifier un item")
    @PutMapping("/admin/salesorderitems/{salesOrderItemId}")
    public ResponseEntity<SalesOrderItemDTO> updateSalesOrderItem(@Valid @RequestBody SalesOrderItemDTO salesOrderItemDTO,
                                                                   @PathVariable Long salesOrderItemId){
        SalesOrderItemDTO updated = salesOrderItemService.updateSalesOrderItemQuantity(salesOrderItemId, salesOrderItemDTO);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @Operation(summary = "Supprimer un item")
    @DeleteMapping("/admin/salesorderitems/{salesOrderItemId}")
    public ResponseEntity<SalesOrderItemDTO> deleteSalesOrderItem(@PathVariable Long salesOrderItemId){
        SalesOrderItemDTO deleted = salesOrderItemService.deleteSalesOrderItem(salesOrderItemId);
        return new ResponseEntity<>(deleted, HttpStatus.OK);
    }

    @Operation(summary = "Get SalesOrderItems")
    @GetMapping("/public/salesorderitems")
    public ResponseEntity<SalesOrderItemResponse> getAllSalesOrderItems(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ){
        SalesOrderItemResponse response = salesOrderItemService.getAllSalesOrderItems(pageNumber, pageSize, sortOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get SalesOrderItems par vente")
    @GetMapping("/public/salesorderitems/salesorders/{salesOrderId}")
    public ResponseEntity<List<SalesOrderItemDTO>> getSalesOrderItemsBySalesOrderId(@PathVariable Long salesOrderId){
        List<SalesOrderItemDTO> items = salesOrderItemService.getSalesOrderItemsBySalesOrderId(salesOrderId);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
}
