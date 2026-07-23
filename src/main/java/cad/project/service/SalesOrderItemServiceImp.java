package cad.project.service;

import cad.project.exceptions.APIException;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.Mouvement_Stock;
import cad.project.model.Produit;
import cad.project.model.SalesOrder;
import cad.project.model.SalesOrderItems;
import cad.project.playload.ProduitDTO;
import cad.project.playload.SalesOrderDTO;
import cad.project.playload.SalesOrderItemDTO;
import cad.project.playload.SalesOrderItemResponse;
import cad.project.repositries.ProduitRepositry;
import cad.project.repositries.SaleOrderItemsRepositry;
import cad.project.repositries.SaleOrderRepositry;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SalesOrderItemServiceImp implements SalesOrderItemService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SaleOrderItemsRepositry saleOrderItemsRepositry;

    @Autowired
    private SaleOrderRepositry saleOrderRepositry;

    @Autowired
    private ProduitRepositry produitRepositry;

    @Transactional
    @Override
    public SalesOrderItemDTO addSalesOrderItem(Long salesOrderId, Long produitId, SalesOrderItemDTO salesOrderItemDTO) {
        SalesOrder salesOrder = saleOrderRepositry.findById(salesOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("SalesOrder", "salesOrderId", salesOrderId));

        Produit produit = produitRepositry.findById(produitId)
                .orElseThrow(() -> new ResourceNotFoundException("Produit", "produitId", produitId));



        SalesOrderItems salesOrderItem = modelMapper.map(salesOrderItemDTO, SalesOrderItems.class);
        if(salesOrderItemDTO.getQuantity() == null   || salesOrderItemDTO.getQuantity() < 1 ){
            salesOrderItem.setQuantity(1);
        }
        if(produit.getQuantity() < salesOrderItem.getQuantity()){
            throw new APIException("Stock insuffisant");

        }
        salesOrderItem.setSalesOrder(salesOrder);
        salesOrderItem.setProduit(produit);
        salesOrderItem.setTva(produit.getTva());
        salesOrderItem.setPrixHT(produit.getPrixHT());
        if(produit.getDiscount() != null && produit.getDiscount() > 0){
            salesOrderItem.setDiscount(produit.getDiscount());
        }
        salesOrderItem.setPrice(produit.getPrice());
        salesOrder.setTotalprice(salesOrder.getTotalprice() + (salesOrderItem.getPrice() * salesOrderItem.getQuantity()));
        saleOrderRepositry.save(salesOrder);

        SalesOrderItems salesOrderItemSaved = saleOrderItemsRepositry.save(salesOrderItem);
        SalesOrderItemDTO salesOrderItemSavedDTO = modelMapper.map(salesOrderItemSaved, SalesOrderItemDTO.class);
        salesOrderItemSavedDTO.setSalesOrderDTO(modelMapper.map(salesOrder , SalesOrderDTO.class));
        salesOrderItemSavedDTO.setProduitDTO(modelMapper.map(produit , ProduitDTO.class));
        return  salesOrderItemSavedDTO;

    }

    @Transactional
    @Override
    public SalesOrderItemDTO updateSalesOrderItemQuantity(Long salesOrderItemId, SalesOrderItemDTO salesOrderItemDTO) {
        SalesOrderItems salesOrderItemFromDb = saleOrderItemsRepositry.findById(salesOrderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("SalesOrderItem", "salesOrderItemId", salesOrderItemId));

        if(salesOrderItemDTO.getQuantity() == null    ){
            throw new APIException("Quantite injuste" + salesOrderItemDTO.getQuantity());
        }

        if(salesOrderItemFromDb.getQuantity() + salesOrderItemDTO.getQuantity() > salesOrderItemFromDb.getProduit().getQuantity() ){
            throw new APIException("Stock insuffisant");
        }

        if(salesOrderItemFromDb.getQuantity() + salesOrderItemDTO.getQuantity() == 0 ){
            throw new APIException("Impossible de decrementer");
        }
        salesOrderItemFromDb.setQuantity(salesOrderItemFromDb.getQuantity() + salesOrderItemDTO.getQuantity());

        SalesOrderItems salesOrderItemSaved = saleOrderItemsRepositry.save(salesOrderItemFromDb);
        UpdateTotlPrice(salesOrderItemFromDb.getSalesOrder().getId());


        return modelMapper.map(salesOrderItemSaved, SalesOrderItemDTO.class);
    }

    @Override
    @Transactional
    public SalesOrderItemDTO deleteSalesOrderItem(Long salesOrderItemId) {
        SalesOrderItems deletedSalesOrderItem = saleOrderItemsRepositry.findById(salesOrderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("SalesOrderItem", "salesOrderItemId", salesOrderItemId));

        Long salesOrderId = deletedSalesOrderItem.getSalesOrder().getId();

        saleOrderItemsRepositry.delete(deletedSalesOrderItem);
        saleOrderItemsRepositry.flush();

        UpdateTotlPrice(salesOrderId);

        return modelMapper.map(deletedSalesOrderItem, SalesOrderItemDTO.class);
    }

    @Override
    public SalesOrderItemResponse getAllSalesOrderItems(Integer pageNumber, Integer pageSize, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by("createdAt").ascending()
                : Sort.by("createdAt").descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<SalesOrderItems> salesOrderItemPage = saleOrderItemsRepositry.findAll(pageDetails);

        List<SalesOrderItems> salesOrderItems = salesOrderItemPage.getContent();
        if (salesOrderItems.isEmpty())
            throw new APIException("No SalesOrderItem Created till now");

        List<SalesOrderItemDTO> salesOrderItemDTOS = salesOrderItems.stream()
                .map(item -> modelMapper.map(item, SalesOrderItemDTO.class))
                .toList();

        SalesOrderItemResponse response = new SalesOrderItemResponse();
        response.setContent(salesOrderItemDTOS);
        response.setPageNumber(salesOrderItemPage.getNumber());
        response.setPageSize(salesOrderItemPage.getSize());
        response.setTotalElements(salesOrderItemPage.getTotalElements());
        response.setTotalPages(salesOrderItemPage.getTotalPages());
        response.setLastPage(salesOrderItemPage.isLast());
        return response;
    }

    @Override
    public List<SalesOrderItemDTO> getSalesOrderItemsBySalesOrderId(Long salesOrderId) {
        saleOrderRepositry.findById(salesOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("SalesOrder", "salesOrderId", salesOrderId));

        List<SalesOrderItems> salesOrderItems = saleOrderItemsRepositry.findBySalesOrderId(salesOrderId);

        return salesOrderItems.stream()
                .map(item -> {
                    SalesOrderItemDTO salesOrderItemDTO =   modelMapper.map(item, SalesOrderItemDTO.class);
                    salesOrderItemDTO.setProduitDTO(modelMapper.map(item.getProduit(), ProduitDTO.class));
                    return  salesOrderItemDTO;
                })
                .toList();
    }

    public void UpdateTotlPrice(Long salesOrderId){
        SalesOrder salesOrder = saleOrderRepositry.findById(salesOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("SalesOrder", "salesOrderId", salesOrderId));
        List<SalesOrderItems> salesOrderItems = salesOrder.getOrderItemsList();
        salesOrder.setTotalprice(0.00);

        salesOrderItems.forEach(salesorderItems -> {
            salesOrder.setTotalprice(salesOrder.getTotalprice() + ( salesorderItems.getQuantity() * salesorderItems.getPrice() ));
        });

        saleOrderRepositry.save(salesOrder);

    }

}
