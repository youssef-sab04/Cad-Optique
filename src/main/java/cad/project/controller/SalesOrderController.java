package cad.project.controller;

import cad.project.config.AppConstants;
import cad.project.playload.CommandeDTO;
import cad.project.playload.SalesOrderDTO;
import cad.project.playload.SalesOrderResponse;
import cad.project.service.SalesOrderService;
import cad.project.service.SalesOrderPdfService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SalesOrderController {

    @Autowired
    SalesOrderService salesOrderService;

    @Autowired
    SalesOrderPdfService salesOrderPdfService;

    @Operation(summary = "Ajouter une vente")
    @PostMapping("/admin/salesorder/clients/{clientId}")
    public ResponseEntity<SalesOrderDTO> addSalesOrder(@Valid @RequestBody SalesOrderDTO salesOrderDTO,
                                                       @PathVariable Long clientId){
        SalesOrderDTO salesOrderDTOSaved = salesOrderService.addSalesOrder(clientId, salesOrderDTO);
        return new ResponseEntity<>(salesOrderDTOSaved, HttpStatus.CREATED);
    }

    @Operation(summary = "Modifier une vente")
    @PutMapping("/admin/salesorders/{salesOrderId}")
    public ResponseEntity<SalesOrderDTO> updateSalesOrder(@Valid @RequestBody SalesOrderDTO salesOrderDTO,
                                                          @PathVariable Long salesOrderId){
        SalesOrderDTO updatedSalesOrderDTO = salesOrderService.updateSalesOrder(salesOrderId, salesOrderDTO);
        return new ResponseEntity<>(updatedSalesOrderDTO, HttpStatus.OK);
    }

    @Operation(summary = "Supprimer une vente")
    @DeleteMapping("/admin/salesorders/{salesOrderId}")
    public ResponseEntity<SalesOrderDTO> deleteSalesOrder(@PathVariable Long salesOrderId){
        SalesOrderDTO deletedSalesOrder = salesOrderService.deleteSalesOrder(salesOrderId);
        return new ResponseEntity<>(deletedSalesOrder, HttpStatus.OK);
    }

    @Operation(summary = "Get SalesOrders")
    @GetMapping("/public/salesorders")
    public ResponseEntity<SalesOrderResponse> getAllSalesOrders(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ){
        SalesOrderResponse salesOrderResponse = salesOrderService.getAllSalesOrders(pageNumber, pageSize, sortOrder);
        return new ResponseEntity<>(salesOrderResponse, HttpStatus.OK);
    }

    @Operation(summary = "Generer le recu PDF de la vente")
    @GetMapping("/admin/salesorder/{salesOrderId}/pdf")
    public ResponseEntity<byte[]> generateSalesOrderPdf(@PathVariable Long salesOrderId) {
        byte[] pdf = salesOrderPdfService.generateSalesOrderPdf(salesOrderId);
        return ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=recu-" + salesOrderId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @Operation(summary = "Valider une commande/vente")
    @PostMapping("/admin/ordre/{ordreId}")
    public ResponseEntity<SalesOrderDTO> validerOrdre(@PathVariable Long ordreId){
        SalesOrderDTO salesOrderDTO = salesOrderService.ValiderOrdre(ordreId);
        return new ResponseEntity<>(salesOrderDTO, HttpStatus.OK);
    }



}