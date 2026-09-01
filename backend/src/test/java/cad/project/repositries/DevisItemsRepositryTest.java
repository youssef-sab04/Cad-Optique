package cad.project.repositries;

import cad.project.model.Client;
import cad.project.model.Devis;
import cad.project.model.DevisItems;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class DevisItemsRepositryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private DevisItemsRepositry devisItemsRepositry;

    private Devis devis;
    private Produit produit;

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
        Client client = new Client();
        client.setNom("Alami");
        client.setPrenom("Sara");
        testEntityManager.persist(client);

        devis = new Devis();
        devis.setClient(client);
        devis.setStatus("EnAttente");
        testEntityManager.persist(devis);

        produit = new Produit();
        produit.setNom("Verre Essilor");
        testEntityManager.persist(produit);
    }

    @Test
    @DisplayName("findByDevisId retourne les lignes du devis")
    void testFindByDevisId() {
        DevisItems item = new DevisItems();
        item.setDevis(devis);
        item.setProduit(produit);
        item.setQuantity(2);
        devisItemsRepositry.save(item);

        List<DevisItems> result = devisItemsRepositry.findByDevisId(devis.getId());

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("findByDevisId retourne une liste vide quand le devis n'a pas de ligne")
    void testFindByDevisId_empty() {
        List<DevisItems> result = devisItemsRepositry.findByDevisId(999L);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findByProduitId retourne les lignes de devis pour ce produit")
    void testFindByProduitId() {
        DevisItems item = new DevisItems();
        item.setDevis(devis);
        item.setProduit(produit);
        item.setQuantity(2);
        devisItemsRepositry.save(item);

        List<DevisItems> result = devisItemsRepositry.findByProduitId(produit.getId());

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("findByProduitId retourne une liste vide quand le produit n'a pas de ligne de devis")
    void testFindByProduitId_empty() {
        List<DevisItems> result = devisItemsRepositry.findByProduitId(999L);

        assertTrue(result.isEmpty());
    }
}
