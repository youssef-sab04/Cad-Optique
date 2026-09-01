package cad.project.repositries;

import cad.project.model.AppRole;
import cad.project.model.Client;
import cad.project.model.Role;
import cad.project.model.User;
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
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class ClientRepositryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    ClientRepositry clientRepositry;

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
        client.setPhoneNumber("0612345678");
        client.setAdresse("12 Rue Hassan II, Fès");
        client.setEmail("sara.alami@test.com");
        client.setDateNaissance(LocalDate.of(1990, 5, 20));
    }

    @Test
    @DisplayName("findAll avec Specification retourne les clients correspondants")
    void testFindAll_withSpecification(){

        clientRepositry.save(client);

        Pageable pageable = PageRequest.of(0, 10);

        Specification<Client> spec = (root, query, cb) -> cb.conjunction();

        Page<Client> result = clientRepositry.findAll(spec, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(client.getNom(), result.getContent().get(0).getNom());
    }

    @Test
    @DisplayName("countNouveauxClientsByYear compte uniquement les clients de l'année donnée")
    void testCountNouveauxClientsByYear() {

        client.setCreatedAt(LocalDateTime.of(2026, 2, 11, 0, 0));
        clientRepositry.save(client);

        Long  result = clientRepositry.countNouveauxClientsByYear(2026);
        Long  result0 = clientRepositry.countNouveauxClientsByYear(2016);


        assertEquals(1, result);
        assertEquals(0, result0);

    }


}