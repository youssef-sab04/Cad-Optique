package cad.project.controller;

import cad.project.exceptions.ResourceNotFoundException;
import cad.project.playload.DevisItemDTO;
import cad.project.playload.DevisItemResponse;
import cad.project.security.WebSecurityConfig;
import cad.project.security.jwt.JwtUtils;
import cad.project.service.DevisItemService;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = DevisItemController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class))
public class DevisItemControllerWebLayer {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    DevisItemService devisItemService;

    @MockitoBean
    JwtUtils jwtUtils;

    // DevisItemController
    @Test
    @DisplayName("Get all devis items returns paginated response")
    void testGetAllDevisItems_returnsPaginatedResponse() throws Exception {
        DevisItemResponse response = new DevisItemResponse();
        response.setContent(List.of(new DevisItemDTO(), new DevisItemDTO()));
        response.setPageNumber(0);
        response.setPageSize(10);
        response.setTotalElements(2L);

        when(devisItemService.getAllDevisItems(eq(0), eq(10), eq("asc")))
                .thenReturn(response);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/public/devisitems")
                .param("pageNumber", "0")
                .param("pageSize", "10")
                .param("sortOrder", "asc")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        DevisItemResponse actual = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), DevisItemResponse.class);

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(2, actual.getContent().size());
    }

    @Test
    @DisplayName("DevisItem can be created")
    void testAddDevisItem_whenValid_returnsCreatedItem() throws Exception {
        Long devisId = 1L;
        Long produitId = 1L;
        DevisItemDTO dto = new DevisItemDTO();

        when(devisItemService.addDevisItem(eq(devisId), eq(produitId), any(DevisItemDTO.class)))
                .thenReturn(dto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/admin/devisitems/devis/{devisId}/produits/{produitId}", devisId, produitId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Add item fails when devis does not exist")
    void testAddDevisItem_whenDevisNotFound_returns404() throws Exception {
        Long devisId = 999L;
        Long produitId = 1L;
        DevisItemDTO dto = new DevisItemDTO();

        when(devisItemService.addDevisItem(eq(devisId), eq(produitId), any(DevisItemDTO.class)))
                .thenThrow(new ResourceNotFoundException("Devis", "id", devisId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/admin/devisitems/devis/{devisId}/produits/{produitId}", devisId, produitId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Add item fails when produit does not exist")
    void testAddDevisItem_whenProduitNotFound_returns404() throws Exception {
        Long devisId = 1L;
        Long produitId = 999L;
        DevisItemDTO dto = new DevisItemDTO();

        when(devisItemService.addDevisItem(eq(devisId), eq(produitId), any(DevisItemDTO.class)))
                .thenThrow(new ResourceNotFoundException("Produit", "id", produitId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/admin/devisitems/devis/{devisId}/produits/{produitId}", devisId, produitId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("DevisItem can be updated")
    void testUpdateDevisItem_whenValid_returnsUpdated() throws Exception {
        Long devisItemId = 1L;
        DevisItemDTO dto = new DevisItemDTO();

        when(devisItemService.updateDevisItemQuantity(eq(devisItemId), any(DevisItemDTO.class)))
                .thenReturn(dto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/admin/devisitems/{devisItemId}", devisItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Update item fails when item does not exist")
    void testUpdateDevisItem_whenNotFound_returns404() throws Exception {
        Long devisItemId = 999L;
        DevisItemDTO dto = new DevisItemDTO();

        when(devisItemService.updateDevisItemQuantity(eq(devisItemId), any(DevisItemDTO.class)))
                .thenThrow(new ResourceNotFoundException("Devis Item", "id", devisItemId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/admin/devisitems/{devisItemId}", devisItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("DevisItem can be deleted")
    void testDeleteDevisItem_whenValid_returnsDeleted() throws Exception {
        Long devisItemId = 1L;
        DevisItemDTO dto = new DevisItemDTO();

        when(devisItemService.deleteDevisItem(eq(devisItemId))).thenReturn(dto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/admin/devisitems/{devisItemId}", devisItemId)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Delete item fails when item does not exist")
    void testDeleteDevisItem_whenNotFound_returns404() throws Exception {
        Long devisItemId = 999L;

        when(devisItemService.deleteDevisItem(eq(devisItemId)))
                .thenThrow(new ResourceNotFoundException("Devis Item", "id", devisItemId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/admin/devisitems/{devisItemId}", devisItemId)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Get items by devis returns list")
    void testGetDevisItemsByDevisId_returnsItems() throws Exception {
        Long devisId = 1L;
        DevisItemDTO item1 = new DevisItemDTO();
        DevisItemDTO item2 = new DevisItemDTO();

        when(devisItemService.getDevisItemsByDevisId(eq(devisId)))
                .thenReturn(List.of(item1, item2));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/public/devisitems/devis/{devisId}", devisId)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();
        DevisItemDTO[] items = new ObjectMapper().readValue(responseBody, DevisItemDTO[].class);

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(2, items.length);
    }
}