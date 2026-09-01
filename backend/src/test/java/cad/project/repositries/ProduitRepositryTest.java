package cad.project.repositries;

import cad.project.model.Client;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class ProduitRepositryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    ProduitRepositry produitRepositry;

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
        produit.setNom("Verre Essilor Varilux");
        produit.setDescription("Verre progressif haute résistance");
        produit.setImage("verre.jpg");
        produit.setCode_barre("1234567890123");
        produit.setQuantity(3);
        produit.setSeuilMin(5);
        produit.setPrixAchat(200.0);
        produit.setPrixHT(250.0);
        produit.setTva(20f);
        produit.setDiscount(0f);
        produit.setPrice(300.0);
        produit.setMarque("Essilor");
        produit.setCouleur("Transparent");
        produit.setModele("Varilux X");
        produit.setIndice(1.6f);
        produit.setDiametre(65f);
        produit.setSeuilMin(10);
        produit.setTraitement("Anti-reflet");
    }

    @Test
    @DisplayName("findAll avec Specification retourne les produits correspondants")
    void testFindAll_withSpecification() {

        produitRepositry.save(produit);

        Pageable pageable = PageRequest.of(0, 10);
        Specification<Produit> spec = (root, query, cb) -> cb.conjunction();

        Page<Produit> result = produitRepositry.findAll(spec, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(produit.getNom(), result.getContent().get(0).getNom());
    }

    @Test
    @DisplayName("countStockFaible compte les produits sous le seuil minimum")
    void testCountStockFaible() {

        produit.setQuantity(3);
        produit.setSeuilMin(5);
        produitRepositry.save(produit);

        long result = produitRepositry.countStockFaible();

        assertEquals(1, result);
    }

    @Test
    @DisplayName("valeurTotaleStock calcule la valeur du stock au prix d'achat")
    void testValeurTotaleStock() {

        produit.setQuantity(3);
        produit.setPrixAchat(200.0);
        produitRepositry.save(produit);

        double result = produitRepositry.valeurTotaleStock();

        assertEquals(600.0, result);
    }



}