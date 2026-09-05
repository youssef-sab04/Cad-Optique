package cad.project.security;

import cad.project.controller.AuthController;
import cad.project.model.*;
import cad.project.playload.ClientDTO;
import cad.project.playload.UserDTO;
import cad.project.repositries.CommandeRepositry;
import cad.project.repositries.FournisseurRepositry;
import cad.project.repositries.RoleRepository;
import cad.project.repositries.UserRepositry;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
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
import org.springframework.test.web.servlet.MvcResult;
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

    private static final String COOKIE_NAME = "cadOptique";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepositry userRepositry;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private FournisseurRepositry fournisseurRepositry;

    @Autowired
    private CommandeRepositry commandeRepositry;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthController authController;

    @Autowired
    private WebSecurityConfig webSecurityConfig;

    private String adminToken;
    private String responsableToken;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
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

        creerCommande();
    }

    private void creerCommande() {
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setNom("Fournisseur Test");
        fournisseur.setEmail("fournisseur@test.com");
        fournisseur.setPhoneNumber("0600000001");
        fournisseur.setAdresse("Adresse Test");
        fournisseur = fournisseurRepositry.save(fournisseur);

        Commande commande = new Commande();
        commande.setDescription("Commande test");
        commande.setStatus("LIVREE");
        commande.setTotalprice(100.0);
        commande.setFournisseur(fournisseur);
        commandeRepositry.save(commande);
    }

    private void createUser(String username, String email, String rawPassword, Role role) {
        if (userRepositry.existsByUserName(username)) {
            return;
        }
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
        body.put("username", username);
        body.put("password", password);

        MvcResult result = mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andReturn();

        return result.getResponse().getCookie(COOKIE_NAME).getValue();
    }

    private Cookie authCookie(String token) {
        return new Cookie(COOKIE_NAME, token);
    }

    @Test
    @DisplayName("GET /api/public/commandes accessible par ADMIN et RESPONSABLE")
    void testRouteCommune() throws Exception {
        mockMvc.perform(get("/api/public/commandes")
                        .cookie(authCookie(adminToken)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/public/commandes")
                        .cookie(authCookie(responsableToken)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/admin/client accessible par RESPONSABLE")
    void testAddClient_responsable() throws Exception {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setNom("Test");
        clientDTO.setPrenom("Client");
        clientDTO.setEmail("client.test@test.com");
        clientDTO.setPhoneNumber("0611111111");

        mockMvc.perform(post("/api/admin/client")
                        .cookie(authCookie(responsableToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /api/admin/client refusé pour ADMIN")
    void testAddClient_refuseAdmin() throws Exception {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setNom("Test");
        clientDTO.setPrenom("Client");
        clientDTO.setEmail("client.test2@test.com");
        clientDTO.setPhoneNumber("0611111112");

        mockMvc.perform(post("/api/admin/client")
                        .cookie(authCookie(adminToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /api/auth/admin/add_responsable refusé pour RESPONSABLE")
    void testAddResponsable_refuseResponsable() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setPassword("afdjktuwr9");
        userDTO.setNom("Malika");
        userDTO.setPrenom("THiaw");

        mockMvc.perform(post("/api/auth/admin/add_responsable")
                        .cookie(authCookie(responsableToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /api/auth/admin/add_responsable accepté pour ADMIN")
    void testAddResponsable_admin() throws Exception {
        String json = """
                {
                  "nom": "Malika",
                  "prenom": "THiaw",
                  "userName": "malika.thiaw",
                  "email": "malika.thiaw@test.com",
                  "cin": "CD123456",
                  "password": "afdjktuwr9"
                }
                """;

        mockMvc.perform(post("/api/auth/admin/add_responsable")
                        .cookie(authCookie(adminToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }
}