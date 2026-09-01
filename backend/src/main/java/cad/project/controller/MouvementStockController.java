package cad.project.controller;


import cad.project.config.AppConstants;
import cad.project.playload.Mouvement_StockResponse;
import cad.project.service.Mouvement_StockService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MouvementStockController {

   @Autowired
   Mouvement_StockService mouvementStockService;



    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Get All mouvement Stock")
    @GetMapping("/public/mouvements-stock")
    public ResponseEntity<Mouvement_StockResponse> getAllCategories(
            @RequestParam(name = "pageNumber" ,defaultValue = AppConstants.PAGE_NUMBER , required = false) Integer pageNumber,
            @RequestParam(name = "pageSize" ,defaultValue = AppConstants.PAGE_SIZE , required = false) Integer pageSize,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ){
        Mouvement_StockResponse mouvementStockResponse = mouvementStockService.getAllMvt( pageNumber ,  pageSize  , sortOrder);
        return new ResponseEntity<>(mouvementStockResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Get mouvement Stock d'un produit")
    @GetMapping("/public/mouvements-stock/{productId}")
    public ResponseEntity<Mouvement_StockResponse> getAllCategories(
            @PathVariable Long productId,
            @RequestParam(name = "pageNumber" ,defaultValue = AppConstants.PAGE_NUMBER , required = false) Integer pageNumber,
            @RequestParam(name = "pageSize" ,defaultValue = AppConstants.PAGE_SIZE , required = false) Integer pageSize,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ){
        Mouvement_StockResponse mouvementStockResponse = mouvementStockService.getAllMvtProd( productId , pageNumber ,  pageSize  , sortOrder);
        return new ResponseEntity<>(mouvementStockResponse, HttpStatus.OK);
    }




}
