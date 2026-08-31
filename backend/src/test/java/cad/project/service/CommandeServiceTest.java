package cad.project.service;

import cad.project.exceptions.APIException;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.*;
import cad.project.playload.CommandeDTO;
import cad.project.playload.CommandeItemDTO;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommandeServiceTest {

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

    @InjectMocks
    private CommandeServiceImp commandeServiceImp;

    @Test
    @DisplayName("Doit lever ResourceNotFoundException quand l'ID de la commande est introuvable")
    void test_validerOrdre_when_id_introuvable() {
        Long id = 999L;

        when(commandeRepositry.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commandeServiceImp.ValiderCommande(
                        id));
    }


    @Test
    @DisplayName("Tester echec de validation d'une commande validee")
    void test() {


        Long id  = 99L;
        Commande commande = new Commande();
        commande.setStatus("LIVREE");
        commande.setId(id);

        when(commandeRepositry.findById(id)).thenReturn(Optional.of(commande));


        APIException exception =   assertThrows(APIException.class,
                () -> commandeServiceImp.ValiderCommande(id));

        assertEquals(exception.getMessage() , "Commande déjà validée");
    }



    @Test
    @DisplayName("Doit valider l'ordre et créer un mouvement ENTREE quand la commande est validée")
    void test_validerOrdre_nominal() {
        int qte = 10;

        Produit p1 = new Produit();
        p1.setNom("Produit1");
        p1.setQuantity(qte);
        p1.setSeuilMin(5);
        p1.setPrice((double) 90L);

        Long id = 80L;
        Commande commande = new Commande();
        commande.setId(id);

        List<CommandeItem> commandeItems = new ArrayList<>();
        CommandeItem commandeItem1 = new CommandeItem();
        commandeItem1 .setProduit(p1);
        commandeItem1 .setPrice(320.0);
        commandeItem1 .setQuantity(3);

        commandeItems.add(commandeItem1);
        commande.setCommandeItems(commandeItems);

        when(commandeRepositry.findById(id)).thenReturn(Optional.of(commande));

        CommandeDTO dto = new CommandeDTO();

        when(modelMapper.map(commande, CommandeDTO.class)).thenReturn(dto);


        CommandeDTO result = commandeServiceImp.ValiderCommande(id);

        verify(produitRepositry).save(p1);

        assertEquals("LIVREE", commande.getStatus());
        assertEquals(qte + 3, p1.getQuantity());
        assertEquals(320.0, p1.getPrixAchat());


        ArgumentCaptor<Mouvement_Stock> captor = ArgumentCaptor.forClass(Mouvement_Stock.class);
        verify(mouvementStockRepositry).save(captor.capture());

        Mouvement_Stock mvt = captor.getValue();
        assertEquals("ENTREE", mvt.getType());
        assertEquals(3, mvt.getQuantity());
        assertEquals(320.0, mvt.getPrix_Unit());
        assertEquals(960.0, mvt.getPrix_total());
        assertEquals(p1, mvt.getProduit());

    }



}
