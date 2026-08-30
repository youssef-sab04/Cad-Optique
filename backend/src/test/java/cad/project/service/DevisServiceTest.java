package cad.project.service;

import cad.project.exceptions.APIException;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.*;
import cad.project.playload.DevisDTO;
import cad.project.playload.SalesOrderDTO;
import cad.project.repositries.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
public class DevisServiceTest {

    @Mock
    private SaleOrderRepositry saleOrderRepositry;

    @Mock
    private DevisRepositry devisRepositry;

    @Mock
    private ProduitRepositry produitRepositry;
    @Mock
    private NotificationRepositry notificationRepositry;
    @Mock
    private Mouvement_StockRepositry mouvementStockRepositry;

    @Mock
    private SaleOrderItemsRepositry saleOrderItemsRepositry;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DevisServiceImp devisServiceImp;

    @Test
    @DisplayName("Doit lever ResourceNotFoundException quand l'ID de devis est introuvable")
    void test_validerOrdre_when_id_introuvable() {
        Long id = 999L;

        when(devisRepositry.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> devisServiceImp.confirmDevis(id));
    }

    @Test
    @DisplayName("Doit confirmer le devis et creer un sales order Valide")
    void test_confirmDevis_when_stock_suffisant() {
        Long id = 999L;

        int qte = 10;

        Produit p1 = new Produit();
        p1.setNom("Produit1");
        p1.setQuantity(qte);
        p1.setSeuilMin(5);
        p1.setPrice(590.0);

        Produit p2 = new Produit();
        p2.setNom("Produit2");
        p2.setQuantity(qte + 4);
        p2.setSeuilMin(5);
        p2.setPrice(50.0);


        SalesOrder salesOrder = new SalesOrder();

        Devis devis = new Devis();
        devis.setId(id);

        DevisItems devisItems1 = new DevisItems();
        devisItems1.setProduit(p1);
        devisItems1.setQuantity(3);
        devisItems1.setPrice(p1.getPrice());

        DevisItems devisItems2 = new DevisItems();
        devisItems2.setProduit(p2);
        devisItems2.setQuantity(3);
        devisItems2.setPrice(p2.getPrice());


        List<DevisItems> devisItemsList = new ArrayList<>();
        devisItemsList.add(devisItems1);
        devisItemsList.add(devisItems2);

        devis.setDevisItemsList(devisItemsList);

        when(devisRepositry.findById(id)).thenReturn(Optional.of(devis));

        when(saleOrderRepositry.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        when(devisRepositry.save(any())).thenReturn(devis);

        DevisDTO devisDTO = new DevisDTO();
        when(modelMapper.map(devis, DevisDTO.class)).thenReturn(devisDTO);


        devisServiceImp.confirmDevis(id);


        assertEquals("Valide", devis.getStatus());
        assertEquals(7, p1.getQuantity());
        assertEquals(11, p2.getQuantity());
        assertEquals(590.0 * 3 + 50.0 * 3, devis.getTotalprice());




        ArgumentCaptor<Mouvement_Stock> captor = ArgumentCaptor.forClass(Mouvement_Stock.class);
        verify(mouvementStockRepositry, times(2)).save(captor.capture());

        List<Mouvement_Stock> mvts = captor.getAllValues();

        Mouvement_Stock mvt1 = mvts.get(0);
        assertEquals("SORTIE", mvt1.getType());
        assertEquals(3, mvt1.getQuantity());
        assertEquals(p1, mvt1.getProduit());

        Mouvement_Stock mvt2 = mvts.get(1);
        assertEquals("SORTIE", mvt2.getType());
        assertEquals(3, mvt2.getQuantity());
        assertEquals(p2, mvt2.getProduit());

        ArgumentCaptor<SalesOrder> soCaptor = ArgumentCaptor.forClass(SalesOrder.class);
        verify(saleOrderRepositry, times(2)).save(soCaptor.capture());

        SalesOrder salesOrderFinal = soCaptor.getValue();
        assertEquals("Valide", salesOrderFinal.getStatus());
        assertEquals(590.0 * 3 + 50.0 * 3, salesOrderFinal.getTotalprice());

    }

    @Test
    @DisplayName("Doit lever APIException et ne rien creer quand le stock est insuffisant")
    void test_confirmDevis_when_stock_insuffisant() {
        Long id = 999L;

        int qte = 10;

        Produit p1 = new Produit();
        p1.setNom("Produit1");
        p1.setQuantity(qte);
        p1.setSeuilMin(5);
        p1.setPrice(590.0);

        Produit p2 = new Produit();
        p2.setNom("Produit2");
        p2.setQuantity(qte);
        p2.setSeuilMin(5);
        p2.setPrice(50.0);

        Devis devis = new Devis();
        devis.setId(id);

        DevisItems devisItems1 = new DevisItems();
        devisItems1.setProduit(p1);
        devisItems1.setQuantity(3);
        devisItems1.setPrice(p1.getPrice());

        DevisItems devisItems2 = new DevisItems();
        devisItems2.setProduit(p2);
        devisItems2.setQuantity(15);
        devisItems2.setPrice(p2.getPrice());

        List<DevisItems> devisItemsList = new ArrayList<>();
        devisItemsList.add(devisItems1);
        devisItemsList.add(devisItems2);

        devis.setDevisItemsList(devisItemsList);

        when(devisRepositry.findById(id)).thenReturn(Optional.of(devis));
        when(saleOrderRepositry.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        assertThrows(APIException.class, () -> devisServiceImp.confirmDevis(id));

        assertNull(devis.getStatus());
        assertEquals(qte, p1.getQuantity());
        assertEquals(qte, p2.getQuantity());

        verify(devisRepositry, never()).save(any());
        verify(mouvementStockRepositry, never()).save(any());
        verify(notificationRepositry, never()).save(any());
    }



    }




