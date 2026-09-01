package cad.project.repositries;

import cad.project.model.Commande;
import cad.project.model.CommandeItem;
import cad.project.model.Fournisseur;
import cad.project.model.Produit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class CommandeItemRepositryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CommandeItemRepositry commandeItemRepositry;

    private Commande commande;
    private Produit produit;
    private CommandeItem commandeItem;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setup() {
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setNom("Verres Plus");
        fournisseur.setPhoneNumber("0522334455");
        testEntityManager.persist(fournisseur);

        commande = new Commande();
        commande.setFournisseur(fournisseur);
        commande.setStatus("EN_COURS");
        testEntityManager.persist(commande);

        produit = new Produit();
        produit.setNom("Verre Essilor");
        testEntityManager.persist(produit);

        commandeItem = new CommandeItem();
        commandeItem.setCommande(commande);
        commandeItem.setProduit(produit);
        commandeItem.setQuantity(5);
        commandeItem.setPrice(100.0);
    }

    @Test
    @DisplayName("findByCommandeId retourne les items liés à la commande")
    void testFindByCommandeId() {
        commandeItemRepositry.save(commandeItem);

        List<CommandeItem> result = commandeItemRepositry.findByCommandeId(commande.getId());

        assertEquals(1, result.size());
        assertEquals(commande.getId(), result.get(0).getCommande().getId());
    }

    @Test
    @DisplayName("findByCommandeId retourne une liste vide quand la commande n'a pas d'item")
    void testFindByCommandeId_empty() {
        List<CommandeItem> result = commandeItemRepositry.findByCommandeId(999L);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findByProduitId retourne les items liés au produit")
    void testFindByProduitId() {
        commandeItemRepositry.save(commandeItem);

        List<CommandeItem> result = commandeItemRepositry.findByProduitId(produit.getId());

        assertEquals(1, result.size());
        assertEquals(produit.getId(), result.get(0).getProduit().getId());
    }

    @Test
    @DisplayName("findByProduitId retourne une liste vide quand le produit n'a pas d'item")
    void testFindByProduitId_empty() {
        List<CommandeItem> result = commandeItemRepositry.findByProduitId(999L);

        assertTrue(result.isEmpty());
    }
}
