package cad.project.repositries;

import cad.project.model.AppRole;
import cad.project.model.Role;
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

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class RoleRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private RoleRepository roleRepository;

    private Role role;

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
        role = new Role();
        role.setRoleName(AppRole.ROLE_ADMIN);
    }

    @Test
    @DisplayName("findByRoleName retourne le rôle quand il existe")
    void testFindByRoleName_found() {
        roleRepository.save(role);

        Optional<Role> result = roleRepository.findByRoleName(AppRole.ROLE_ADMIN);

        assertTrue(result.isPresent());
        assertEquals(AppRole.ROLE_ADMIN, result.get().getRoleName());
    }

    @Test
    @DisplayName("findByRoleName retourne vide quand le rôle n'existe pas")
    void testFindByRoleName_notFound() {
        Optional<Role> result = roleRepository.findByRoleName(AppRole.ROLE_RESPONSABLE);

        assertTrue(result.isEmpty());
    }
}
