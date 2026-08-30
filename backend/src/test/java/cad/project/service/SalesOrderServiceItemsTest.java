package cad.project.service;

import cad.project.exceptions.APIException;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.Produit;
import cad.project.model.SalesOrder;
import cad.project.model.SalesOrderItems;
import cad.project.playload.SalesOrderItemDTO;
import cad.project.repositries.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SalesOrderServiceItemsTest {

    @Mock
    private SaleOrderRepositry saleOrderRepositry;
    @Mock
    private ProduitRepositry produitRepositry;
    @Mock
    private NotificationRepositry notificationRepositry;
    @Mock
    private Mouvement_StockRepositry mouvementStockRepositry;
    @Mock
    private ModelMapper modelMapper;

    @Mock
    private SaleOrderItemsRepositry saleOrderItemsRepositry;

    @InjectMocks
    private SalesOrderItemServiceImp salesOrderItemServiceImp;

    @InjectMocks
    private SalesOrderServiceImp salesOrderServiceImp;


    @Test
    @DisplayName("Update de prix total de sales order when add or update an order item")
    void test_add_puis_update_quantite_recalcule_totalprice() {

        Long id = 999L;

        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setId(id);
        salesOrder.setOrderItemsList(new ArrayList<>());

        int qte = 10;
        Produit p1 = new Produit();
        p1.setId(id);
        p1.setNom("Produit1");
        p1.setQuantity(qte);
        p1.setPrice(500.0);

        SalesOrderItemDTO salesOrderItemDTO = new SalesOrderItemDTO();
        salesOrderItemDTO.setQuantity(5);
        salesOrderItemDTO.setId(id);

        when(saleOrderRepositry.findById(id)).thenReturn(Optional.of(salesOrder));
        when(produitRepositry.findById(id)).thenReturn(Optional.of(p1));

        SalesOrderItems newItem = new SalesOrderItems();
        newItem.setQuantity(5);

        when(modelMapper.map(salesOrderItemDTO, SalesOrderItems.class)).thenReturn(newItem);
        when(saleOrderItemsRepositry.save(any())).thenReturn(newItem);
        when(modelMapper.map(newItem, SalesOrderItemDTO.class)).thenReturn(new SalesOrderItemDTO());

        salesOrderItemServiceImp.addSalesOrderItem(id, id, salesOrderItemDTO);

        assertEquals(500.0 * 5, salesOrder.getTotalprice());

        salesOrderItemDTO.setQuantity(2);

        when(saleOrderItemsRepositry.findById(id)).thenReturn(Optional.of(newItem));

        salesOrder.setOrderItemsList(new ArrayList<>(List.of(newItem)));
        newItem.setSalesOrder(salesOrder);

        salesOrderItemServiceImp.updateSalesOrderItemQuantity(id, salesOrderItemDTO);

        assertEquals(500.0 * 2, salesOrder.getTotalprice());

    }

    @Test
    @DisplayName("Create order item with product already exist")
    void test_addSalesOrderItem_when_produit_deja_existant() {

        Long id = 999L;

        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setId(id);

        SalesOrderItems existingItem = new SalesOrderItems();
        Produit p1 = new Produit();
        p1.setId(id);
        existingItem.setProduit(p1);

        salesOrder.setOrderItemsList(new ArrayList<>(List.of(existingItem)));

        SalesOrderItemDTO salesOrderItemDTO = new SalesOrderItemDTO();
        salesOrderItemDTO.setQuantity(2);

        when(saleOrderRepositry.findById(id)).thenReturn(Optional.of(salesOrder));
        when(produitRepositry.findById(id)).thenReturn(Optional.of(p1));

        assertThrows(APIException.class,
                () -> salesOrderItemServiceImp.addSalesOrderItem(id, id, salesOrderItemDTO));
    }



}
