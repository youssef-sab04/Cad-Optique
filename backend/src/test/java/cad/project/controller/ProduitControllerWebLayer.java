package cad.project.controller;

import cad.project.playload.ProduitDTO;
import cad.project.playload.ProductResponse;
import cad.project.security.WebSecurityConfig;
import cad.project.security.jwt.JwtUtils;
import cad.project.service.CategoryService;
import cad.project.service.ProduitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = ProduitController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class))
public class ProduitControllerWebLayer {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    ProduitService produitService;

    @MockitoBean
    CategoryService categoryService;

    @MockitoBean
    JwtUtils jwtUtils;

    @Test
    @DisplayName("Produit can be created")
    void testAddProduit_whenValid_returnsCreated() throws Exception {
        Long categoryId = 1L;
        ProduitDTO dto = new ProduitDTO();
        dto.setNom("Monture X");
        dto.setPrice(100.0);

        MockMultipartFile produitPart = new MockMultipartFile(
                "produit", "", "application/json",
                new ObjectMapper().writeValueAsBytes(dto));
        MockMultipartFile imagePart = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "fake-image".getBytes());

        when(produitService.addProduit(eq(categoryId), any(ProduitDTO.class), any())).thenReturn(dto);

        MockMultipartHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.multipart("/api/admin/produits/{categoryId}", categoryId);
        requestBuilder.file(produitPart).file(imagePart);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Get all products returns paginated response")
    void testGetAllProducts_returnsPaginatedResponse() throws Exception {
        ProductResponse response = new ProductResponse();
        response.setContent(List.of(new ProduitDTO(), new ProduitDTO()));
        response.setPageNumber(0);
        response.setPageSize(10);
        response.setTotalElements(2L);

        when(produitService.getAllProducts(eq(0), eq(10), eq("id"), eq("asc"), eq(null), eq(null)))
                .thenReturn(response);

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/public/products")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("sortBy", "id")
                        .param("sortOrder", "asc")
        ).andReturn();

        ProductResponse actual = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), ProductResponse.class);

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(2, actual.getContent().size());
    }

    @Test
    @DisplayName("Update product fails when product does not exist")
    void testUpdateProduct_whenNotFound_returns404() throws Exception {
        Long productId = 999L;
        ProduitDTO dto = new ProduitDTO();
        dto.setNom("Monture X");
        dto.setPrice(100.0);

        MockMultipartFile produitPart = new MockMultipartFile(
                "produit", "", "application/json",
                new ObjectMapper().writeValueAsBytes(dto));

        when(produitService.updateProduct(eq(productId), any(ProduitDTO.class), any()))
                .thenThrow(new cad.project.exceptions.ResourceNotFoundException("Produit", "id", productId));

        MockMultipartHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.multipart("/api/admin/products/{productId}", productId);
        requestBuilder.file(produitPart);
        requestBuilder.with(request -> {
            request.setMethod("PUT");
            return request;
        });

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Delete product fails when product does not exist")
    void testDeleteProduct_whenNotFound_returns404() throws Exception {
        Long productId = 999L;

        when(produitService.deleteProduct(eq(productId)))
                .thenThrow(new cad.project.exceptions.ResourceNotFoundException("Produit", "id", productId));

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/admin/products/{productId}", productId)
        ).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Create produit fails when nom is missing")
    void testAddProduit_whenNomMissing_returns400() throws Exception {
        Long categoryId = 1L;
        ProduitDTO dto = new ProduitDTO();
        dto.setNom("");
        dto.setPrice(100.0);

        MockMultipartFile produitPart = new MockMultipartFile(
                "produit", "", "application/json",
                new ObjectMapper().writeValueAsBytes(dto));
        MockMultipartFile imagePart = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "fake-image".getBytes());

        MockMultipartHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.multipart("/api/admin/produits/{categoryId}", categoryId);
        requestBuilder.file(produitPart).file(imagePart);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Create produit fails when price is missing")
    void testAddProduit_whenPriceMissing_returns400() throws Exception {
        Long categoryId = 1L;
        ProduitDTO dto = new ProduitDTO();
        dto.setNom("Monture X");
        dto.setPrice(null);

        MockMultipartFile produitPart = new MockMultipartFile(
                "produit", "", "application/json",
                new ObjectMapper().writeValueAsBytes(dto));
        MockMultipartFile imagePart = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "fake-image".getBytes());

        MockMultipartHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.multipart("/api/admin/produits/{categoryId}", categoryId);
        requestBuilder.file(produitPart).file(imagePart);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }
}