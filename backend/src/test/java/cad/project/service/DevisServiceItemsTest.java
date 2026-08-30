package cad.project.service;

import cad.project.exceptions.APIException;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.*;
import cad.project.playload.DevisDTO;
import cad.project.playload.DevisItemDTO;
import cad.project.playload.SalesOrderItemDTO;
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
public class DevisServiceItemsTest {

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
    private DecvisItemsRepositry devisItemsRepositry;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DevisItemServiceImp devisItemServiceImp;

    @Test
    @DisplayName("Create order item with product already exist")
    void test_addDevisItem_when_produit_deja_existant() {

        Long id = 999L;

       Devis devis = new Devis();
       devis.setId(id);

       DevisItems existingdevisItems = new DevisItems();
        Produit p1 = new Produit();
        p1.setId(id);
        existingdevisItems.setProduit(p1);

        devis.setDevisItemsList(new ArrayList<>(List.of(existingdevisItems)));

        DevisItemDTO devisItemDTO = new DevisItemDTO();
        devisItemDTO.setQuantity(2);

        when(devisRepositry.findById(id)).thenReturn(Optional.of(devis));
        when(produitRepositry.findById(id)).thenReturn(Optional.of(p1));

        assertThrows(APIException.class,
                () -> devisItemServiceImp.addDevisItem(id, id, devisItemDTO));
    }


    @Test
    @DisplayName("Update de prix total du devis when add or update un devis item")
    void test_add_puis_update_quantite_recalcule_totalprice() {

        Long id = 999L;

        Devis devis = new Devis();
        devis.setId(id);
        devis.setTotalprice(0.0);
        devis.setDevisItemsList(new ArrayList<>());

        int qte = 10;
        Produit p1 = new Produit();
        p1.setId(id);
        p1.setNom("Produit1");
        p1.setQuantity(qte);
        p1.setPrice(500.0);

        DevisItemDTO devisItemDTO = new DevisItemDTO();
        devisItemDTO.setQuantity(5);
        devisItemDTO.setId(id);

        when(devisRepositry.findById(id)).thenReturn(Optional.of(devis));
        when(produitRepositry.findById(id)).thenReturn(Optional.of(p1));

        DevisItems newItem = new DevisItems();
        newItem.setQuantity(5);
        newItem.setProduit(p1);

        when(modelMapper.map(devisItemDTO, DevisItems.class)).thenReturn(newItem);
        when(devisItemsRepositry.save(any())).thenReturn(newItem);
        when(modelMapper.map(newItem, DevisItemDTO.class)).thenReturn(new DevisItemDTO());

        devisItemServiceImp.addDevisItem(id, id, devisItemDTO);

        assertEquals(500.0 * 5, devis.getTotalprice());

        devisItemDTO.setQuantity(2);

        when(devisItemsRepositry.findById(id)).thenReturn(Optional.of(newItem));

        devis.setDevisItemsList(new ArrayList<>(List.of(newItem)));
        newItem.setDevis(devis);

        devisItemServiceImp.updateDevisItemQuantity(id, devisItemDTO);

        assertEquals(500.0 * 2, devis.getTotalprice());
    }




    }




