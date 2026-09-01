package cad.project.repositries;

import cad.project.model.Client;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class SaleOrderRepositryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private SaleOrderRepositry saleOrderRepositry;

    private Client client;

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
    }

    private SalesOrder buildSalesOrder(String status, double totalprice, double montantReste, LocalDateTime createdAt) {
        SalesOrder order = new SalesOrder();
        order.setClient(client);
        order.setStatus(status);
        order.setTotalprice(totalprice);
        order.setMontantReste(montantReste);
        testEntityManager.persist(order);
        order.setCreatedAt(createdAt);
        return testEntityManager.merge(order);
    }

    @Test
    @DisplayName("sumCaByYear additionne le CA des commandes validées de l'année")
    void testSumCaByYear() {
        buildSalesOrder("Valide", 1000.0, 0.0, LocalDateTime.of(2026, 4, 1, 0, 0));
        buildSalesOrder("EnCours", 500.0, 0.0, LocalDateTime.of(2026, 4, 1, 0, 0));

        double result = saleOrderRepositry.sumCaByYear(2026);

        assertEquals(1000.0, result);
    }

    @Test
    @DisplayName("countVentesByYear compte uniquement les commandes validées de l'année")
    void testCountVentesByYear() {
        buildSalesOrder("Valide", 1000.0, 0.0, LocalDateTime.of(2026, 4, 1, 0, 0));
        buildSalesOrder("Valide", 200.0, 0.0, LocalDateTime.of(2025, 4, 1, 0, 0));

        long result = saleOrderRepositry.countVentesByYear(2026);

        assertEquals(1, result);
    }

    @Test
    @DisplayName("sumMontantResteByYear additionne le montant restant dû de l'année")
    void testSumMontantResteByYear() {
        buildSalesOrder("Valide", 1000.0, 300.0, LocalDateTime.of(2026, 4, 1, 0, 0));
        buildSalesOrder("Valide", 500.0, 100.0, LocalDateTime.of(2025, 4, 1, 0, 0));

        double result = saleOrderRepositry.sumMontantResteByYear(2026);

        assertEquals(300.0, result);
    }

    @Test
    @DisplayName("statsVentesParMois regroupe le nombre de ventes et le CA par mois")
    void testStatsVentesParMois() {
        buildSalesOrder("Valide", 1000.0, 0.0, LocalDateTime.of(2026, 7, 1, 0, 0));
        buildSalesOrder("Valide", 500.0, 0.0, LocalDateTime.of(2026, 7, 15, 0, 0));

        List<Object[]> result = saleOrderRepositry.statsVentesParMois(2026);

        assertEquals(1, result.size());
        Object[] row = result.get(0);
        assertEquals(7, row[0]);
        assertEquals(2L, row[1]);
        assertEquals(1500.0, ((Number) row[2]).doubleValue());
    }
}
