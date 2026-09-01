package cad.project.repositries;

import cad.project.model.Client;
import cad.project.model.Paiment;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class PaimentRepositryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PaimentRepositry paimentRepositry;

    private Client client;
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
        client = new Client();
        client.setNom("Alami");
        client.setPrenom("Sara");
        testEntityManager.persist(client);

        salesOrder = new SalesOrder();
        salesOrder.setClient(client);
        salesOrder.setStatus("Valide");
        testEntityManager.persist(salesOrder);
    }

    @Test
    @DisplayName("findByClient retourne les paiements du client")
    void testFindByClient() {
        Paiment paiment = new Paiment();
        paiment.setClient(client);
        paiment.setSalesOrder(salesOrder);
        paiment.setMontant_Paye(200.0);
        paimentRepositry.save(paiment);

        List<Paiment> result = paimentRepositry.findByClient(client);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("findByClient retourne une liste vide quand le client n'a pas de paiement")
    void testFindByClient_empty() {
        Client autreClient = new Client();
        autreClient.setNom("Bennani");
        autreClient.setPrenom("Yassine");
        testEntityManager.persist(autreClient);

        List<Paiment> result = paimentRepositry.findByClient(autreClient);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findBySalesOrder retourne les paiements de la commande")
    void testFindBySalesOrder() {
        Paiment paiment = new Paiment();
        paiment.setClient(client);
        paiment.setSalesOrder(salesOrder);
        paiment.setMontant_Paye(200.0);
        paimentRepositry.save(paiment);

        List<Paiment> result = paimentRepositry.findBySalesOrder(salesOrder);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("findBySalesOrder retourne une liste vide quand la commande n'a pas de paiement")
    void testFindBySalesOrder_empty() {
        SalesOrder autreOrder = new SalesOrder();
        autreOrder.setClient(client);
        autreOrder.setStatus("EnCours");
        testEntityManager.persist(autreOrder);

        List<Paiment> result = paimentRepositry.findBySalesOrder(autreOrder);

        assertTrue(result.isEmpty());
    }
}
