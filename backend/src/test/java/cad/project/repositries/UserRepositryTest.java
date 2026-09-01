package cad.project.repositries;

import cad.project.model.AppRole;
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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class UserRepositryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    UserRepositry userRepositry;

    private User user;

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
        user = new User();
        user.setNom("Alami");
        user.setPrenom("Sara");
        user.setUserName("salami4i31qer");
        user.setEmail("sara.alami@test.com");
        user.setPassword("Password123");
        user.setCin("AB123456");
        user.setPhoneNumber("0612345678");
    }

    @Test
    @DisplayName("findByUserName retourne l'utilisateur quand il existe")
    void testFindByUserName_found() {
        userRepositry.save(user);

        Optional<User> result = userRepositry.findByUserName("salami4i31qer");

        assertTrue(result.isPresent());
        assertEquals(user.getUserName(), result.get().getUserName());
    }

    @Test
    @DisplayName("findByUserName retourne vide quand l'utilisateur n'existe pas")
    void testFindByUserName_notFound() {
        Optional<User> result = userRepositry.findByUserName("inexistant");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("existsByUserName retourne true quand le username existe")
    void testExistsByUserName_true() {
        userRepositry.save(user);

        boolean exists = userRepositry.existsByUserName("salami4i31qer");

        assertTrue(exists);
    }

    @Test
    @DisplayName("existsByUserName retourne false quand le username n'existe pas")
    void testExistsByUserName_false() {
        boolean exists = userRepositry.existsByUserName("inexistant");

        assertFalse(exists);
    }

    @Test
    @DisplayName("existsByEmail retourne true quand l'email existe")
    void testExistsByEmail_true() {
        userRepositry.save(user);

        boolean exists = userRepositry.existsByEmail("sara.alami@test.com");

        assertTrue(exists);
    }

    @Test
    @DisplayName("existsByEmail retourne false quand l'email n'existe pas")
    void testExistsByEmail_false() {
        boolean exists = userRepositry.existsByEmail("inexistant@test.com");

        assertFalse(exists);
    }

    @Test
    @DisplayName("findByUserRoles_RoleName retourne les utilisateurs ayant le rôle donné")
    void testFindByUserRoles_RoleName() {
        Role role = new Role();
        role.setRoleName(AppRole.ROLE_ADMIN);
        testEntityManager.persist(role);

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setUserRoles(roles);

        userRepositry.save(user);

        Pageable pageable = PageRequest.of(0, 10);
        Page<User> result = userRepositry.findByUserRoles_RoleName(AppRole.ROLE_ADMIN, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(user.getUserName(), result.getContent().get(0).getUserName());
    }

    @Test
    @DisplayName("findByUserRoles_RoleName retourne une page vide quand aucun utilisateur n'a ce rôle")
    void testFindByUserRoles_RoleName_empty() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<User> result = userRepositry.findByUserRoles_RoleName(AppRole.ROLE_ADMIN, pageable);

        assertTrue(result.isEmpty());
    }
}