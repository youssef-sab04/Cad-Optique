package cad.project.repositries;

import cad.project.model.Client;
import cad.project.model.Remboursement;
import cad.project.model.SalesOrder;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class RemboursementRepositryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private RemboursementRepositry remboursementRepositry;

    private SalesOrder salesOrder;

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
    }

    @Test
    @DisplayName("findBySalesOrder retourne le remboursement lié à la commande")
    void testFindBySalesOrder_found() {
        Remboursement remboursement = new Remboursement();
        remboursement.setSalesOrder(salesOrder);
        remboursement.setStatus("EnCours");
        remboursementRepositry.save(remboursement);

        Optional<Remboursement> result = remboursementRepositry.findBySalesOrder(salesOrder);

        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("findBySalesOrder retourne vide quand aucun remboursement n'est lié")
    void testFindBySalesOrder_notFound() {
        Optional<Remboursement> result = remboursementRepositry.findBySalesOrder(salesOrder);

        assertFalse(result.isPresent());
    }
}
