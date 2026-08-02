package cad.project.service;

import cad.project.playload.ProductResponse;
import cad.project.playload.ProduitDTO;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProduitService {
    ProduitDTO addProduit(Long categoryId, @Valid ProduitDTO produitDTO, MultipartFile image) throws IOException ;

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword, String category);

    ProduitDTO updateProduct(Long productId, @Valid ProduitDTO productDTO , MultipartFile image) throws IOException ;

    ProduitDTO deleteProduct(Long productId);
}
