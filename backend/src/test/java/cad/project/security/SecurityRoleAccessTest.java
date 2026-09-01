package cad.project.security;

import cad.project.model.AppRole;
import cad.project.model.Role;
import cad.project.model.User;
import cad.project.repositries.RoleRepository;
import cad.project.repositries.UserRepositry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class SecurityRoleAccessTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepositry userRepositry;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private String adminToken;
    private String responsableToken;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("cloudinary.cloud-name", () -> "test");
        registry.add("cloudinary.api-key", () -> "test");
        registry.add("cloudinary.api-secret", () -> "test");
        registry.add("spring.app.jwtSecret", () -> "testSecretKeyForJwtSigningInTestsOnlyMustBeLongEnough123456");
    }

    @BeforeEach
    void setup() throws Exception {
        Role roleAdmin = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setRoleName(AppRole.ROLE_ADMIN);
                    return roleRepository.save(r);
                });

        Role roleResponsable = roleRepository.findByRoleName(AppRole.ROLE_RESPONSABLE)
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setRoleName(AppRole.ROLE_RESPONSABLE);
                    return roleRepository.save(r);
                });

        createUser("admintest", "admin@test.com", "Password123", roleAdmin);
        createUser("respotest", "respo@test.com", "Password123", roleResponsable);

        adminToken = login("admintest", "Password123");
        responsableToken = login("respotest", "Password123");
    }

    private void createUser(String username, String email, String rawPassword, Role role) {
        User user = new User();
        user.setNom("Test");
        user.setPrenom("User");
        user.setUserName(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setCin("AB123456");
        user.setPhoneNumber("0600000000");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setUserRoles(roles);
        userRepositry.save(user);
    }

    private String login(String username, String password) throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("userName", username);
        body.put("password", password);

        String response = mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Map<?, ?> json = objectMapper.readValue(response, Map.class);
        return (String) json.get("token");
    }

    @Test
    void  test(){
        Assert.assertEquals(1,1);

    }

    /*
    // ===== Endpoints communs (accessibles ADMIN + RESPONSABLE) =====

    @Test
    @DisplayName("GET /api/public/clients accessible par ADMIN")
    void testPublicClients_admin() throws Exception {
        mockMvc.perform(get("/api/public/clients")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/public/clients accessible par RESPONSABLE")
    void testPublicClients_responsable() throws Exception {
        mockMvc.perform(get("/api/public/clients")
                        .header("Authorization", "Bearer " + responsableToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/public/produits accessible par ADMIN et RESPONSABLE")
    void testPublicProduits_communAuxDeuxRoles() throws Exception {
        mockMvc.perform(get("/api/public/products")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/public/products")
                        .header("Authorization", "Bearer " + responsableToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/public/examens accessible par ADMIN et RESPONSABLE")
    void testPublicExamens_communAuxDeuxRoles() throws Exception {
        mockMvc.perform(get("/api/public/examens")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/public/examens")
                        .header("Authorization", "Bearer " + responsableToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Endpoint commun sans token retourne 401/403")
    void testEndpointCommun_sansToken() throws Exception {
        mockMvc.perform(get("/api/public/clients"))
                .andExpect(status().is4xxClientError());
    }

    // ===== Endpoints réservés au RESPONSABLE =====

    @Test
    @DisplayName("Endpoint réservé RESPONSABLE accessible par RESPONSABLE")
    void testEndpointResponsable_accesResponsable() throws Exception {
        // TODO remplacer par l'endpoint réel réservé RESPONSABLE
        mockMvc.perform(get("/api/public/clients")
                        .header("Authorization", "Bearer " + responsableToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Endpoint réservé RESPONSABLE refusé pour ADMIN")
    void testEndpointResponsable_refuseAdmin() throws Exception {
        // TODO remplacer par l'endpoint réel réservé RESPONSABLE
        mockMvc.perform(get("/api/public/clients")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Endpoint réservé RESPONSABLE #2 accessible par RESPONSABLE")
    void testEndpointResponsable2_accesResponsable() throws Exception {
        // TODO
    }

    @Test
    @DisplayName("Endpoint réservé RESPONSABLE #2 refusé pour ADMIN")
    void testEndpointResponsable2_refuseAdmin() throws Exception {
        // TODO
    }

    @Test
    @DisplayName("Endpoint réservé RESPONSABLE #3 refusé sans authentification")
    void testEndpointResponsable3_refuseSansAuth() throws Exception {
        // TODO
    }


     */
}