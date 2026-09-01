package cad.project.repositries;

import cad.project.model.Mouvement_Stock;
import cad.project.model.Produit;
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
public class Mouvement_StockRepositryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private Mouvement_StockRepositry mouvementStockRepositry;

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
        produit = new Produit();
        produit.setNom("Verre Essilor");
        testEntityManager.persist(produit);
    }

    private Mouvement_Stock buildMouvement(String type, Integer quantity, LocalDateTime createdAt) {
        Mouvement_Stock m = new Mouvement_Stock();
        m.setProduit(produit);
        m.setType(type);
        m.setQuantity(quantity);
        testEntityManager.persist(m);
        m.setCreatedAt(createdAt);
        return testEntityManager.merge(m);
    }

    @Test
    @DisplayName("findAllByProduit retourne les mouvements du produit donné")
    void testFindAllByProduit() {
        buildMouvement("ENTREE", 10, LocalDateTime.now());

        Page<Mouvement_Stock> result = mouvementStockRepositry.findAllByProduit(produit, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("countMouvementsEntre compte les mouvements dans l'intervalle donné")
    void testCountMouvementsEntre() {
        buildMouvement("ENTREE", 10, LocalDateTime.of(2026, 6, 15, 10, 0));
        buildMouvement("SORTIE", 5, LocalDateTime.of(2025, 6, 15, 10, 0));

        long result = mouvementStockRepositry.countMouvementsEntre(
                LocalDateTime.of(2026, 1, 1, 0, 0), LocalDateTime.of(2027, 1, 1, 0, 0));

        assertEquals(1, result);
    }

    @Test
    @DisplayName("topProduitsVendus classe les produits par quantité vendue décroissante")
    void testTopProduitsVendus() {
        buildMouvement("SORTIE", 10, LocalDateTime.of(2026, 3, 1, 0, 0));
        buildMouvement("ENTREE", 50, LocalDateTime.of(2026, 3, 1, 0, 0));

        List<Object[]> result = mouvementStockRepositry.topProduitsVendus(2026, PageRequest.of(0, 10));

        assertEquals(1, result.size());
        assertEquals(produit.getId(), result.get(0)[0]);
        assertEquals(10L, ((Number) result.get(0)[2]).longValue());
    }
}
