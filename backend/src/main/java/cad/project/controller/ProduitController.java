package cad.project.controller;


import cad.project.config.AppConstants;
import cad.project.playload.ProductResponse;
import cad.project.playload.ProduitDTO;
import cad.project.service.CategoryService;
import cad.project.service.ProduitService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProduitController {

    @Autowired
    ProduitService produitService;

    @Autowired
    CategoryService categoryService;

    @PreAuthorize("hasRole('RESPONSABLE')")
    @PostMapping(value = "/admin/produits/{categoryId}", consumes = {"multipart/form-data"})
    public ResponseEntity<ProduitDTO> addProduit (
            @RequestPart("produit") @Valid ProduitDTO produitDTO,
            @RequestPart("image") MultipartFile image,
            @PathVariable Long categoryId) throws IOException {
        ProduitDTO savedProduitDTO = produitService.addProduit(categoryId, produitDTO, image);
        return new ResponseEntity<>(savedProduitDTO, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Get produits + recherger par nom + categorie S ")
    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY_NOUN, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ){
        ProductResponse productResponse = produitService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder, keyword, category);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @PutMapping(value = "/admin/products/{productId}", consumes = {"multipart/form-data"})
    public ResponseEntity<ProduitDTO> updateProduct(
            @RequestPart("produit") @Valid ProduitDTO productDTO,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @PathVariable Long productId) throws IOException {
        ProduitDTO updatedProductDTO = produitService.updateProduct(productId, productDTO, image);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Supprimer Produit")
    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProduitDTO> deleteProduct(@PathVariable Long productId){
        ProduitDTO deletedProduct = produitService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProduct, HttpStatus.OK);
    }


}
