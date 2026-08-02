package cad.project.service;

import cad.project.exceptions.APIException;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.Mouvement_Stock;
import cad.project.model.Produit;
import cad.project.playload.Mouvement_StockResponse;
import cad.project.playload.Mouvement_StockDTO;
import cad.project.playload.ProduitDTO;
import cad.project.repositries.Mouvement_StockRepositry;
import cad.project.repositries.ProduitRepositry;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Mouvement_StockServiceImp implements  Mouvement_StockService{
    
    @Autowired
    private Mouvement_StockRepositry mouvementStockRepositry;
    
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProduitRepositry produitRepositry;
    @Override
    public Mouvement_StockResponse getAllMvt(Integer pageNumber, Integer pageSize, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by("createdAt").ascending()
                : Sort.by("createdAt").descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Mouvement_Stock> mouvementStockPage = mouvementStockRepositry.findAll(pageDetails);

        List<Mouvement_Stock> mouvementStocks = mouvementStockPage.getContent();
        if (mouvementStocks.isEmpty())
            throw new APIException("No Commande Created till now");

        List<Mouvement_StockDTO> mouvementStockDTOS = mouvementStocks.stream()
                .map(mouvementStock -> {
                    Mouvement_StockDTO dto = modelMapper.map(mouvementStock, Mouvement_StockDTO.class);
                    if (mouvementStock.getProduit() != null) {
                        dto.setProduitDTO(modelMapper.map(mouvementStock.getProduit(), ProduitDTO.class));
                    }
                    return dto;
                })
                .toList();

       Mouvement_StockResponse mouvementStockResponse = new Mouvement_StockResponse();
        mouvementStockResponse.setContent(mouvementStockDTOS);
        mouvementStockResponse.setPageNumber(mouvementStockPage.getNumber());
        mouvementStockResponse.setPageSize(mouvementStockPage.getSize());
        mouvementStockResponse.setTotalElements(mouvementStockPage.getTotalElements());
        mouvementStockResponse.setTotalPages(mouvementStockPage.getTotalPages());
        mouvementStockResponse.setLastPage(mouvementStockPage.isLast());
        return mouvementStockResponse;
    }

    @Override
    public Mouvement_StockResponse getAllMvtProd(Long productId, Integer pageNumber, Integer pageSize, String sortOrder) {

        Produit produit = produitRepositry.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produit", "produitId", productId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by("createdAt").ascending()
                : Sort.by("createdAt").descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Mouvement_Stock> mouvementStockPage = mouvementStockRepositry.findAllByProduit(produit, pageDetails);

        List<Mouvement_Stock> mouvementStocks = mouvementStockPage.getContent();
        if (mouvementStocks.isEmpty())
            throw new APIException("No mvt stock Created till now");

        List<Mouvement_StockDTO> mouvementStockDTOS = mouvementStocks.stream()
                .map(mouvementStock -> modelMapper.map(mouvementStock, Mouvement_StockDTO.class))
                .toList();

        Mouvement_StockResponse mouvementStockResponse = new Mouvement_StockResponse();
        mouvementStockResponse.setContent(mouvementStockDTOS);
        mouvementStockResponse.setPageNumber(mouvementStockPage.getNumber());
        mouvementStockResponse.setPageSize(mouvementStockPage.getSize());
        mouvementStockResponse.setTotalElements(mouvementStockPage.getTotalElements());
        mouvementStockResponse.setTotalPages(mouvementStockPage.getTotalPages());
        mouvementStockResponse.setLastPage(mouvementStockPage.isLast());
        return mouvementStockResponse;
    }
}
