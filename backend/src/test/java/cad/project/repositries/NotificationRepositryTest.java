package cad.project.repositries;

import cad.project.model.Client;
import cad.project.model.Notification;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class NotificationRepositryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private NotificationRepositry notificationRepositry;

    private Client client;
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
        client = new Client();
        client.setNom("Alami");
        client.setPrenom("Sara");
        testEntityManager.persist(client);

        produit = new Produit();
        produit.setNom("Verre Essilor");
        testEntityManager.persist(produit);
    }

    private Notification buildNotification(Client c, Produit p, String type, String message) {
        Notification n = new Notification();
        n.setClient(c);
        n.setProduit(p);
        n.setType(type);
        n.setMessage(message);
        return notificationRepositry.save(n);
    }

    @Test
    @DisplayName("findByClientIsNull retourne les notifications sans client")
    void testFindByClientIsNull() {
        buildNotification(null, produit, "STOCK", "Stock faible");
        buildNotification(client, null, "RAPPEL", "Rappel client");

        Page<Notification> result = notificationRepositry.findByClientIsNull(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("findByClientId retourne les notifications du client")
    void testFindByClientId() {
        buildNotification(client, null, "RAPPEL", "Rappel client");

        Page<Notification> result = notificationRepositry.findByClientId(client.getId(), PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("findByProduitId retourne les notifications du produit")
    void testFindByProduitId() {
        buildNotification(null, produit, "STOCK", "Stock faible");

        List<Notification> result = notificationRepositry.findByProduitId(produit.getId());

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("existsByClientIdAndTypeAndMessage retourne true quand la notification existe déjà")
    void testExistsByClientIdAndTypeAndMessage_true() {
        buildNotification(client, null, "rappelOrdonnanceLunette", "Merci de reprendre rendez-vous");

        boolean exists = notificationRepositry.existsByClientIdAndTypeAndMessage(
                client.getId(), "rappelOrdonnanceLunette", "Merci de reprendre rendez-vous");

        assertTrue(exists);
    }

    @Test
    @DisplayName("existsByClientIdAndTypeAndMessage retourne false quand la notification n'existe pas")
    void testExistsByClientIdAndTypeAndMessage_false() {
        boolean exists = notificationRepositry.existsByClientIdAndTypeAndMessage(
                client.getId(), "rappelOrdonnanceLunette", "Message inexistant");

        assertFalse(exists);
    }

    @Test
    @DisplayName("findByProduitIsNull retourne les notifications sans produit")
    void testFindByProduitIsNull() {
        buildNotification(client, null, "RAPPEL", "Rappel client");
        buildNotification(null, produit, "STOCK", "Stock faible");

        Page<Notification> result = notificationRepositry.findByProduitIsNull(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("findAllClientByKeyword retourne les notifications dont le client correspond au mot-clé")
    void testFindAllClientByKeyword() {
        buildNotification(client, null, "RAPPEL", "Rappel client");

        Page<Notification> result = notificationRepositry.findAllClientByKeyword("alami", PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("findByClientIsNullAndProduitNomContainingIgnoreCase filtre par nom de produit")
    void testFindByClientIsNullAndProduitNomContainingIgnoreCase() {
        buildNotification(null, produit, "STOCK", "Stock faible");

        Page<Notification> result = notificationRepositry.findByClientIsNullAndProduitNomContainingIgnoreCase(
                "essilor", PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
    }
}
