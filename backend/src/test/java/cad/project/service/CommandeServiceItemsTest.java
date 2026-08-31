package cad.project.service;

import cad.project.exceptions.APIException;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.*;
import cad.project.playload.CommandeItemDTO;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommandeServiceItemsTest {

    @Mock
    private CommandeRepositry commandeRepositry;
    @Mock
    private ProduitRepositry produitRepositry;
    @Mock
    private NotificationRepositry notificationRepositry;
    @Mock
    private Mouvement_StockRepositry mouvementStockRepositry;
    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CommandeItemRepositry commandeItemRepositry;

    @InjectMocks
    private CommandeServiceImp commandeServiceImp;

    @InjectMocks
    private CommandeItemServiceImp commandeItemServiceImp;

    @Test
    @DisplayName("Doit lever ResourceNotFoundException quand la commande est introuvable")
    void test_addCommandeItem_when_commande_introuvable() {

        Long id = 99L;

        CommandeItemDTO commandeItemDTO = new CommandeItemDTO();


         assertThrows(ResourceNotFoundException.class,
                () -> commandeItemServiceImp.addCommandeItem(id, id, null , null ,  commandeItemDTO ));

    }

    @Test
    @DisplayName("Doit lever ResourceNotFoundException quand le produit est introuvable")
    void test_addCommandeItem_when_produit_introuvable() {

        Long id = 99L;

        Commande commande = new Commande();
        CommandeItemDTO commandeItemDTO = new CommandeItemDTO();

        when(commandeRepositry.findById(id)).thenReturn(Optional.of(commande));

        assertThrows(ResourceNotFoundException.class,
                () -> commandeItemServiceImp.addCommandeItem(id, id, null , null ,  commandeItemDTO ));

    }

    @Test
    @DisplayName("Ajout puis update de quantité recalcule le totalprice de la commande")
    void test_add_puis_update_quantite_recalcule_totalprice() {

        Long id = 999L;

        Commande commande = new Commande();
        commande.setId(id);
        commande.setTotalprice(0.00);
        commande.setCommandeItems(new ArrayList<>());

        Produit p1 = new Produit();
        p1.setId(id);
        p1.setNom("Produit1");

        CommandeItemDTO commandeItemDTO = new CommandeItemDTO();
        commandeItemDTO.setQuantity(5);
        commandeItemDTO.setPrice(500.0);

        when(commandeRepositry.findById(id)).thenReturn(Optional.of(commande));
        when(produitRepositry.findById(id)).thenReturn(Optional.of(p1));

        CommandeItem newItem = new CommandeItem();
        newItem.setQuantity(5);
        newItem.setPrice(500.0);

        when(modelMapper.map(commandeItemDTO, CommandeItem.class)).thenReturn(newItem);
        when(commandeItemRepositry.save(any())).thenReturn(newItem);
        when(modelMapper.map(newItem, CommandeItemDTO.class)).thenReturn(new CommandeItemDTO());

        commandeItemServiceImp.addCommandeItem(id, id, null, null, commandeItemDTO);

        assertEquals(500.0 * 5, commande.getTotalprice());

        commandeItemDTO.setQuantity(-3);

        when(commandeItemRepositry.findById(id)).thenReturn(Optional.of(newItem));

        commande.setCommandeItems(new ArrayList<>(List.of(newItem)));
        newItem.setCommande(commande);

        commandeItemServiceImp.updateCommandeItem(id, commandeItemDTO);

        assertEquals(500.0 * 2, commande.getTotalprice());

    }

    @Test
    @DisplayName("Doit lever ResourceNotFoundException quand le commandeItem est introuvable")
    void test_deleteCommandeItem_when_id_introuvable() {

        Long id = 99L;

        when(commandeItemRepositry.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commandeItemServiceImp.deleteCommandeItem(id));
    }

    @Test
    @DisplayName("Doit supprimer l'item et recalculer le totalprice de la commande")
    void test_deleteCommandeItem_nominal() {

        Long id = 999L;

        Commande commande = new Commande();
        commande.setId(id);
        commande.setTotalprice(500.0);

        CommandeItem itemToDelete = new CommandeItem();
        itemToDelete.setCommande(commande);
        itemToDelete.setQuantity(2);
        itemToDelete.setPrice(250.0);

        commande.setCommandeItems(new ArrayList<>());

        when(commandeItemRepositry.findById(id)).thenReturn(Optional.of(itemToDelete));
        when(commandeRepositry.findById(id)).thenReturn(Optional.of(commande));
        when(modelMapper.map(itemToDelete, CommandeItemDTO.class)).thenReturn(new CommandeItemDTO());

        commandeItemServiceImp.deleteCommandeItem(id);

        assertEquals(0.0, commande.getTotalprice());
    }

}
