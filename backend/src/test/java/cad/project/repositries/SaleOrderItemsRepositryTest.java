package cad.project.repositries;

import cad.project.model.Client;
import cad.project.model.Produit;
import cad.project.model.SalesOrder;
import cad.project.model.SalesOrderItems;
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
public class SaleOrderItemsRepositryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private SaleOrderItemsRepositry saleOrderItemsRepositry;

    private SalesOrder salesOrder;
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

        salesOrder = new SalesOrder();
        salesOrder.setClient(client);
        salesOrder.setStatus("Valide");
        testEntityManager.persist(salesOrder);

        produit = new Produit();
        produit.setNom("Verre Essilor");
        testEntityManager.persist(produit);
    }

    @Test
    @DisplayName("findBySalesOrderId retourne les lignes de la commande")
    void testFindBySalesOrderId() {
        SalesOrderItems item = new SalesOrderItems();
        item.setSalesOrder(salesOrder);
        item.setProduit(produit);
        item.setQuantity(3);
        saleOrderItemsRepositry.save(item);

        List<SalesOrderItems> result = saleOrderItemsRepositry.findBySalesOrderId(salesOrder.getId());

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("findBySalesOrderId retourne une liste vide quand la commande n'a pas de ligne")
    void testFindBySalesOrderId_empty() {
        List<SalesOrderItems> result = saleOrderItemsRepositry.findBySalesOrderId(999L);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findByProduitId retourne les lignes de vente pour ce produit")
    void testFindByProduitId() {
        SalesOrderItems item = new SalesOrderItems();
        item.setSalesOrder(salesOrder);
        item.setProduit(produit);
        item.setQuantity(3);
        saleOrderItemsRepositry.save(item);

        List<SalesOrderItems> result = saleOrderItemsRepositry.findByProduitId(produit.getId());

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("findByProduitId retourne une liste vide quand le produit n'a pas de ligne de vente")
    void testFindByProduitId_empty() {
        List<SalesOrderItems> result = saleOrderItemsRepositry.findByProduitId(999L);

        assertTrue(result.isEmpty());
    }
}
