package cad.project.service;

import cad.project.exceptions.APIException;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.*;
import cad.project.playload.ClientDTO;
import cad.project.playload.CommandeDTO;
import cad.project.playload.SalesOrderDTO;
import cad.project.playload.SalesOrderResponse;
import cad.project.repositries.*;
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
public class SalesOrderServiceImp implements SalesOrderService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SaleOrderRepositry saleOrderRepositry;

    @Autowired
    private ClientRepositry clientRepositry;

    @Autowired
    private Mouvement_StockRepositry mouvementStockRepositry;

    @Autowired
    private ProduitRepositry produitRepositry;

    @Autowired
    private NotificationRepositry notificationRepositry;

    @Override
    public SalesOrderDTO addSalesOrder(Long clientId, SalesOrderDTO salesOrderDTO) {
        Client client = clientRepositry.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "clientId", clientId));

        SalesOrder salesOrder = modelMapper.map(salesOrderDTO, SalesOrder.class);
        salesOrder.setTotalprice(0.0);
        salesOrder.setStatus("en cours");
        salesOrder.setClient(client);

        SalesOrder salesOrderSaved = saleOrderRepositry.save(salesOrder);
        SalesOrderDTO salesOrderDTOSaved = modelMapper.map(salesOrderSaved, SalesOrderDTO.class);
        salesOrderDTOSaved.setClientDTO(modelMapper.map(client , ClientDTO.class));

        return salesOrderDTOSaved;
    }

    @Override
    public SalesOrderDTO updateSalesOrder(Long salesOrderId, SalesOrderDTO salesOrderDTO) {
        SalesOrder salesOrderFromDb = saleOrderRepositry.findById(salesOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("SalesOrder", "salesOrderId", salesOrderId));

        salesOrderFromDb.setStatus(salesOrderDTO.getStatus());
        salesOrderFromDb.setDescription(salesOrderDTO.getDescription());
        salesOrderFromDb.setAdresse(salesOrderDTO.getAdresse());
        salesOrderFromDb.setTotalprice(salesOrderDTO.getTotalprice());

        SalesOrder salesOrderSaved = saleOrderRepositry.save(salesOrderFromDb);

        return modelMapper.map(salesOrderSaved, SalesOrderDTO.class);
    }

    @Override
    public SalesOrderDTO deleteSalesOrder(Long salesOrderId) {
        SalesOrder deletedSalesOrder = saleOrderRepositry.findById(salesOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("SalesOrder", "salesOrderId", salesOrderId));

        SalesOrderDTO salesOrderDTO = modelMapper.map(deletedSalesOrder, SalesOrderDTO.class);

        Client client = deletedSalesOrder.getClient();
        if (client != null && client.getSalesOrders() != null) {
            client.getSalesOrders().remove(deletedSalesOrder);
            clientRepositry.save(client);
        } else {
            saleOrderRepositry.delete(deletedSalesOrder);
        }

        return salesOrderDTO;
    }

    @Override
    public SalesOrderResponse getAllSalesOrders(Integer pageNumber, Integer pageSize, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by("createdAt").ascending()
                : Sort.by("createdAt").descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<SalesOrder> salesOrderPage = saleOrderRepositry.findAll(pageDetails);

        List<SalesOrder> salesOrders = salesOrderPage.getContent();
        if (salesOrders.isEmpty())
            throw new APIException("No SalesOrder Created till now");


        List<SalesOrderDTO> salesOrderDTOS = salesOrders.stream()
                .map(salesOrder -> {
                    SalesOrderDTO dto = modelMapper.map(salesOrder, SalesOrderDTO.class);
                    dto.setClientDTO(modelMapper.map(salesOrder.getClient(), ClientDTO.class));
                    return dto;
                })
                .toList();

        SalesOrderResponse salesOrderResponse = new SalesOrderResponse();
        salesOrderResponse.setContent(salesOrderDTOS);
        salesOrderResponse.setPageNumber(salesOrderPage.getNumber());
        salesOrderResponse.setPageSize(salesOrderPage.getSize());
        salesOrderResponse.setTotalElements(salesOrderPage.getTotalElements());
        salesOrderResponse.setTotalPages(salesOrderPage.getTotalPages());
        salesOrderResponse.setLastPage(salesOrderPage.isLast());
        return salesOrderResponse;
    }

    @Transactional
    @Override
    public SalesOrderDTO ValiderOrdre(Long ordreId) {
        SalesOrder salesOrder = saleOrderRepositry.findById(ordreId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "OrderId", ordreId));

        List<SalesOrderItems> salesOrderItems = salesOrder.getOrderItemsList();
        salesOrderItems.forEach(salesorderItems -> {

            Produit produit = salesorderItems.getProduit();
            if (produit.getQuantity() < salesorderItems.getQuantity()) {
                throw new APIException("Stock insuffisant pour le produit : " + produit.getNom());
            }
            produit.setQuantity(produit.getQuantity() - salesorderItems.getQuantity());
            produitRepositry.save(produit);

            if (produit.getSeuilMin() != null && produit.getQuantity() <= produit.getSeuilMin()) {
                Notification notification = new Notification();
                notification.setProduit(produit);
                notification.setType(produit.getQuantity() == 0 ? "stock_epuise" : "stock_bas");
                notification.setMessage("Le produit " + produit.getNom() + " a atteint une quantite de " + produit.getQuantity());
                notification.setDescription("Seuil minimum : " + produit.getSeuilMin());
                notification.setProduit(produit);
                notification.set_read(false);
                notificationRepositry.save(notification);
            }

            Mouvement_Stock mouvementStock = new Mouvement_Stock();
            mouvementStock.setSalesOrderItems(salesorderItems);
            mouvementStock.setProduit(salesorderItems.getProduit());
            mouvementStock.setQuantity(salesorderItems.getQuantity());
            mouvementStock.setType("SORTIE");
            mouvementStock.setPrix_Unit(salesorderItems.getPrice());
            mouvementStock.setPrix_total(salesorderItems.getQuantity() * salesorderItems.getPrice() );
            mouvementStockRepositry.save(mouvementStock);


        });

        salesOrder.setStatus("Valide");

        saleOrderRepositry.save(salesOrder);

        return modelMapper.map(salesOrder, SalesOrderDTO.class);
    }






}



