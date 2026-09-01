package cad.project.repositries;

import cad.project.model.Client;
import cad.project.model.Devis;
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
public class DevisRepositryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private DevisRepositry devisRepositry;

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

    private Devis buildDevis(String status, LocalDateTime createdAt) {
        Devis devis = new Devis();
        devis.setClient(client);
        devis.setStatus(status);
        testEntityManager.persist(devis);
        devis.setCreatedAt(createdAt);
        return testEntityManager.merge(devis);
    }

    @Test
    @DisplayName("countDevisByYear compte tous les devis de l'année donnée")
    void testCountDevisByYear() {
        buildDevis("Valide", LocalDateTime.of(2026, 3, 1, 0, 0));
        buildDevis("EnAttente", LocalDateTime.of(2025, 3, 1, 0, 0));

        long result = devisRepositry.countDevisByYear(2026);

        assertEquals(1, result);
    }

    @Test
    @DisplayName("countDevisValideByYear ne compte que les devis validés de l'année")
    void testCountDevisValideByYear() {
        buildDevis("Valide", LocalDateTime.of(2026, 3, 1, 0, 0));
        buildDevis("EnAttente", LocalDateTime.of(2026, 4, 1, 0, 0));

        long result = devisRepositry.countDevisValideByYear(2026);

        assertEquals(1, result);
    }

    @Test
    @DisplayName("countDevisEnAttente exclut les devis Valide et Annulee")
    void testCountDevisEnAttente() {
        buildDevis("Valide", LocalDateTime.now());
        buildDevis("Annulee", LocalDateTime.now());
        buildDevis("EnAttente", LocalDateTime.now());

        long result = devisRepositry.countDevisEnAttente();

        assertEquals(1, result);
    }

    @Test
    @DisplayName("statsDevisParMois regroupe le nombre de devis et de devis validés par mois")
    void testStatsDevisParMois() {
        buildDevis("Valide", LocalDateTime.of(2026, 5, 1, 0, 0));
        buildDevis("EnAttente", LocalDateTime.of(2026, 5, 15, 0, 0));

        List<Object[]> result = devisRepositry.statsDevisParMois(2026);

        assertEquals(1, result.size());
        Object[] row = result.get(0);
        assertEquals(5, row[0]);
        assertEquals(2L, row[1]);
        assertEquals(1L, ((Number) row[2]).longValue());
    }
}
