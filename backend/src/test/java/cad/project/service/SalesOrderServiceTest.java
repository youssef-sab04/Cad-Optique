package cad.project.service;

import cad.project.exceptions.APIException;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.*;
import cad.project.playload.SalesOrderDTO;
import cad.project.repositries.Mouvement_StockRepositry;
import cad.project.repositries.NotificationRepositry;
import cad.project.repositries.ProduitRepositry;
import cad.project.repositries.SaleOrderRepositry;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SalesOrderServiceTest {

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

    @InjectMocks
    private SalesOrderServiceImp salesOrderServiceImp;

    @Test
    @DisplayName("Doit lever ResourceNotFoundException quand l'ID de la commande est introuvable")
    void test_validerOrdre_when_id_introuvable() {
        Long id = 999L;

        when(saleOrderRepositry.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> salesOrderServiceImp.ValiderOrdre(id));
    }


    @Test
    @DisplayName("Doit lever APIException quand le stock est insuffisant")
    void test_validerOrdre_when_stock_Insuffisant() {

        int qte = 10;
        Produit p1 = new Produit();
        p1.setNom("Produit1");
        p1.setQuantity(qte);
        p1.setSeuilMin(5);


        Long id = 80L;
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setId(id);

        List<SalesOrderItems> salesOrderItems = new ArrayList<>();
        SalesOrderItems salesOrderItems1 = new SalesOrderItems();
        salesOrderItems1.setProduit(p1);
        salesOrderItems1.setQuantity(15);


        salesOrderItems.add(salesOrderItems1);

        salesOrder.setOrderItemsList(salesOrderItems);

        when(saleOrderRepositry.findById(id)).thenReturn(Optional.of(salesOrder));

        APIException exception = assertThrows(APIException.class,
                () -> salesOrderServiceImp.ValiderOrdre(id));

        verify(produitRepositry, never()).save(p1);
        assertEquals("Stock insuffisant pour le produit : Produit1", exception.getMessage());
        assertNotEquals(salesOrder.getStatus(), "Valide");
        assertNull(salesOrder.getStatus());
        assertEquals(p1.getQuantity(), qte);

    }

    @Test
    @DisplayName("Doit valider l'ordre et créer un mouvement SORTIE quand le stock est suffisant")
    void test_validerOrdre_when_stock_suffisant() {
        int qte = 10;
        Produit p1 = new Produit();
        p1.setNom("Produit1");
        p1.setQuantity(qte);
        p1.setSeuilMin(5);
        p1.setPrice((double) 90L);

        Long id = 80L;
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setId(id);

        List<SalesOrderItems> salesOrderItems = new ArrayList<>();
        SalesOrderItems salesOrderItems1 = new SalesOrderItems();
        salesOrderItems1.setProduit(p1);
        salesOrderItems1.setPrice(p1.getPrice());
        salesOrderItems1.setQuantity(3);

        salesOrderItems.add(salesOrderItems1);
        salesOrder.setOrderItemsList(salesOrderItems);

        when(saleOrderRepositry.findById(id)).thenReturn(Optional.of(salesOrder));

        SalesOrderDTO dto = new SalesOrderDTO();
        when(modelMapper.map(salesOrder, SalesOrderDTO.class)).thenReturn(dto);

        SalesOrderDTO result = salesOrderServiceImp.ValiderOrdre(id);

        verify(notificationRepositry, never()).save(any());
        verify(produitRepositry).save(p1);

        assertEquals("Valide", salesOrder.getStatus());
        assertEquals(qte - 3, p1.getQuantity());

        ArgumentCaptor<Mouvement_Stock> captor = ArgumentCaptor.forClass(Mouvement_Stock.class);
        verify(mouvementStockRepositry).save(captor.capture());

        Mouvement_Stock mvt = captor.getValue();
        assertEquals("SORTIE", mvt.getType());
        assertEquals(3, mvt.getQuantity());
        assertEquals(90.0, mvt.getPrix_Unit());
        assertEquals(270.0, mvt.getPrix_total());
        assertEquals(p1, mvt.getProduit());

    }


    @Test
    @DisplayName("Doit créer une notification stock_bas quand la quantité atteint le seuil minimal")
    void test_validerOrdre_when_notification_seuil_minimal() {

        int qte = 10;
        Produit p1 = new Produit();
        p1.setNom("Produit1");
        p1.setQuantity(qte);
        p1.setSeuilMin(5);
        p1.setPrice(90.0);

        Long id = 80L;
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setId(id);

        List<SalesOrderItems> salesOrderItems = new ArrayList<>();
        SalesOrderItems salesOrderItems1 = new SalesOrderItems();
        salesOrderItems1.setProduit(p1);
        salesOrderItems1.setPrice(p1.getPrice());
        salesOrderItems1.setQuantity(7);

        salesOrderItems.add(salesOrderItems1);
        salesOrder.setOrderItemsList(salesOrderItems);

        when(saleOrderRepositry.findById(id)).thenReturn(Optional.of(salesOrder));

        SalesOrderDTO dto = new SalesOrderDTO();
        when(modelMapper.map(salesOrder, SalesOrderDTO.class)).thenReturn(dto);

        salesOrderServiceImp.ValiderOrdre(id);

        assertEquals(3, p1.getQuantity());

        ArgumentCaptor<Notification> notifCaptor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepositry).save(notifCaptor.capture());

        Notification notif = notifCaptor.getValue();
        assertEquals("stock_bas", notif.getType());
        assertEquals(p1, notif.getProduit());
    }

}