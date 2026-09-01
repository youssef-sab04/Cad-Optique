package cad.project.repositries;

import cad.project.model.Client;
import cad.project.model.OrdonnanceLentille;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class OrdonnanceLentilleRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private OrdonnanceLentilleRepository ordonnanceLentilleRepository;

    private Client client;
    private OrdonnanceLentille ordonnance;

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

        ordonnance = new OrdonnanceLentille();
        ordonnance.setClient(client);
        ordonnance.setDateEmission(LocalDate.of(2026, 1, 10));
        ordonnance.setDateExpiration(LocalDate.of(2027, 1, 10));
    }

    @Test
    @DisplayName("findByClientId retourne les ordonnances lentille du client")
    void testFindByClientId() {
        ordonnanceLentilleRepository.save(ordonnance);

        List<OrdonnanceLentille> result = ordonnanceLentilleRepository.findByClientId(client.getId());

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("findByClientId retourne une liste vide quand le client n'a pas d'ordonnance")
    void testFindByClientId_empty() {
        List<OrdonnanceLentille> result = ordonnanceLentilleRepository.findByClientId(999L);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findAll avec Specification retourne les ordonnances correspondantes")
    void testFindAll_withSpecification() {
        ordonnanceLentilleRepository.save(ordonnance);

        Pageable pageable = PageRequest.of(0, 10);
        Specification<OrdonnanceLentille> spec = (root, query, cb) -> cb.conjunction();

        Page<OrdonnanceLentille> result = ordonnanceLentilleRepository.findAll(spec, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("findByDateExpirationBetween retourne les ordonnances qui expirent dans l'intervalle")
    void testFindByDateExpirationBetween() {
        ordonnanceLentilleRepository.save(ordonnance);

        List<OrdonnanceLentille> result = ordonnanceLentilleRepository.findByDateExpirationBetween(
                LocalDate.of(2026, 12, 1), LocalDate.of(2027, 2, 1));

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("findByDateExpirationBetween retourne une liste vide hors intervalle")
    void testFindByDateExpirationBetween_empty() {
        ordonnanceLentilleRepository.save(ordonnance);

        List<OrdonnanceLentille> result = ordonnanceLentilleRepository.findByDateExpirationBetween(
                LocalDate.of(2030, 1, 1), LocalDate.of(2031, 1, 1));

        assertTrue(result.isEmpty());
    }
}
