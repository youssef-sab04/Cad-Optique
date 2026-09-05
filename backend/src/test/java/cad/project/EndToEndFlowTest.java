package cad.project;

import cad.project.model.*;
import cad.project.repositries.ClientRepositry;
import cad.project.repositries.ProduitRepositry;
import cad.project.repositries.RoleRepository;
import cad.project.repositries.UserRepositry;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EndToEndFlowTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepositry userRepositry;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ClientRepositry clientRepositry;

    @Autowired
    private ProduitRepositry produitRepositry;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final RequestLoggingFilter requestLoggingFilter = new RequestLoggingFilter();
    private final ResponseLoggingFilter responseLoggingFilter = new ResponseLoggingFilter();

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");
    static {
        postgres.start();
    }
    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    private static final String COOKIE_NAME = "cadOptique";
    private String jwtToken;
    private Long clientId;
    private Long produitId;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.filters(requestLoggingFilter ,responseLoggingFilter);


        Role roleResponsable = roleRepository.findByRoleName(AppRole.ROLE_RESPONSABLE)
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setRoleName(AppRole.ROLE_RESPONSABLE);
                    return roleRepository.save(r);
                });

        if (!userRepositry.existsByUserName("respo_e2e")) {
            User user = new User();
            user.setNom("Test");
            user.setPrenom("Respo");
            user.setUserName("respo_e2e");
            user.setEmail("respo_e2e@test.com");
            user.setPassword(passwordEncoder.encode("Password123"));
            user.setCin("EE123456");
            user.setPhoneNumber("0600000002");
            Set<Role> roles = new HashSet<>();
            roles.add(roleResponsable);
            user.setUserRoles(roles);
            userRepositry.save(user);
        }

        Client client = new Client();
        client.setNom("Client");
        client.setPrenom("E2E");
        client.setPhoneNumber("06" + System.nanoTime() % 100000000L);
        client = clientRepositry.save(client);
        clientId = client.getId();

        Produit produit = new Produit();
        produit.setNom("Monture E2E");
        produit.setQuantity(50);
        produit.setPrixHT(100.0);
        produit.setTva(20f);
        produit.setDiscount(0f);
        produit.setPrice(120.0);
        produit = produitRepositry.save(produit);
        produitId = produit.getId();

        login("respo_e2e", "Password123");
    }

    private void login(String username, String password) {
        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);

        jwtToken = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/auth/signin")
                .then()
                .statusCode(200)
                .extract().cookie(COOKIE_NAME);
    }


    @Test
    @Order(1)
    @DisplayName("Création SalesOrder impacte le stock produit")
    void testCreationSalesOrderImpacteStock() {
        int stockAvant = produitRepositry.findById(produitId).orElseThrow().getQuantity();

        Map<String, Object> produitDTO = new HashMap<>();
        produitDTO.put("id", produitId);

        Map<String, Object> itemDTO = new HashMap<>();
        itemDTO.put("quantity", 5);

        Map<String, Object> salesOrderDTO = new HashMap<>();
        salesOrderDTO.put("description", "Vente E2E");

        Long salesOrderId = given()
                .cookie(COOKIE_NAME, jwtToken)
                .contentType(ContentType.JSON)
                .body(salesOrderDTO)
                .when()
                .post("/api/admin/salesorder/clients/{clientId}", clientId)
                .then()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        given()
                .cookie(COOKIE_NAME, jwtToken)
                .contentType(ContentType.JSON)
                .body(itemDTO)
                .when()
                .post("/api/admin/salesorderitems/salesorders/{salesOrderId}/produits/{produitId}", salesOrderId , produitId)
                .then()
                .statusCode(201);

        given()
                .cookie(COOKIE_NAME, jwtToken)
                .when()
                .post("/api/admin/ordre/{ordreId}", salesOrderId)
                .then()
                .statusCode(200);

        int stockApres = produitRepositry.findById(produitId).orElseThrow().getQuantity();
        Assertions.assertEquals(stockAvant - 5, stockApres);
    }

    @Test
    @Order(2)
    @DisplayName("confirmDevis crée un SalesOrder")
    void testConfirmDevisCreeSalesOrder() {
        int stockAvant = produitRepositry.findById(produitId).orElseThrow().getQuantity();

        Map<String, Object> produitDTO = new HashMap<>();
        produitDTO.put("id", produitId);

        Map<String, Object> itemDTO = new HashMap<>();
        itemDTO.put("quantity", 2);

        Map<String, Object> devisDTO = new HashMap<>();
        devisDTO.put("description", "Devis E2E");

        Long devisId = given()
                .cookie(COOKIE_NAME, jwtToken)
                .contentType(ContentType.JSON)
                .body(devisDTO)
                .when()
                .post("/api/admin/devis/clients/{clientId}", clientId)
                .then()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        given()
                .cookie(COOKIE_NAME, jwtToken)
                .contentType(ContentType.JSON)
                .body(itemDTO)
                .when()
                .post("/api/admin/devisitems/devis/{devisId}/produits/{produitId}", devisId , produitId)
                .then()
                .statusCode(201)
                .body("id", notNullValue());

        given()
                .cookie(COOKIE_NAME, jwtToken)
                .when()
                .post("/api/admin/devis/{devisId}/confirm", devisId)
                .then()
                .statusCode(201)
                .body("id", notNullValue());

        int stockApres = produitRepositry.findById(produitId).orElseThrow().getQuantity();
        Assertions.assertEquals(stockAvant - 2, stockApres);
    }

    @Test
    @Order(3)
    @DisplayName("Login JWT puis accès à une route /api/admin protégée")
    void testLoginPuisAccesAdmin() {
        given()
                .cookie(COOKIE_NAME, jwtToken)
                .when()
                .get("/api/public/salesorders")
                .then()
                .statusCode(200);
    }

}