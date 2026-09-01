package cad.project.controller;

import cad.project.exceptions.ResourceNotFoundException;
import cad.project.playload.CommandeItemDTO;
import cad.project.playload.CommandeItemResponse;
import cad.project.security.WebSecurityConfig;
import cad.project.security.jwt.JwtUtils;
import cad.project.service.CommandeItemService;
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
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = CommandeItemController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class))
public class CommandeItemControllerWebLayer {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    CommandeItemService commandeItemService;

    @MockitoBean
    JwtUtils jwtUtils;



    @Test
    @DisplayName("Get all commande items returns paginated response")
    void testGetAllCommandeItems_returnsPaginatedResponse() throws Exception {
        CommandeItemResponse response = new CommandeItemResponse();
        response.setContent(List.of(new CommandeItemDTO(), new CommandeItemDTO()));
        response.setPageNumber(0);
        response.setPageSize(10);
        response.setTotalElements(2L);

        when(commandeItemService.getAllCommandeItems(eq(0), eq(10), eq("id"), eq("asc")))
                .thenReturn(response);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/public/commandeItems")
                .param("pageNumber", "0")
                .param("pageSize", "10")
                .param("sortBy", "id")
                .param("sortOrder", "asc")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        CommandeItemResponse actual = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), CommandeItemResponse.class);

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(2, actual.getContent().size());
    }

    @Test
    @DisplayName("CommandeItem can be created")
    void testAddCommandeItem_whenValid_returnsCreated() throws Exception {
        Long commandeId = 1L;
        Long produitId = 1L;
        CommandeItemDTO dto = new CommandeItemDTO();

        when(commandeItemService.addCommandeItem(eq(commandeId), eq(produitId), isNull(), isNull(), any(CommandeItemDTO.class)))
                .thenReturn(dto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/admin/commandeItem/commandes/{commandeId}/produits/{produitId}", commandeId, produitId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Add item fails when commande does not exist")
    void testAddCommandeItem_whenCommandeNotFound_returns404() throws Exception {
        Long commandeId = 999L;
        Long produitId = 1L;
        CommandeItemDTO dto = new CommandeItemDTO();

        when(commandeItemService.addCommandeItem(eq(commandeId), eq(produitId), isNull(), isNull(), any(CommandeItemDTO.class)))
                .thenThrow(new ResourceNotFoundException("Commande", "id", commandeId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/admin/commandeItem/commandes/{commandeId}/produits/{produitId}", commandeId, produitId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Add item fails when produit does not exist")
    void testAddCommandeItem_whenProduitNotFound_returns404() throws Exception {
        Long commandeId = 1L;
        Long produitId = 999L;
        CommandeItemDTO dto = new CommandeItemDTO();

        when(commandeItemService.addCommandeItem(eq(commandeId), eq(produitId), isNull(), isNull(), any(CommandeItemDTO.class)))
                .thenThrow(new ResourceNotFoundException("Produit", "id", produitId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/admin/commandeItem/commandes/{commandeId}/produits/{produitId}", commandeId, produitId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("CommandeItem can be updated")
    void testUpdateCommandeItem_whenValid_returnsUpdated() throws Exception {
        Long commandeItemId = 1L;
        CommandeItemDTO dto = new CommandeItemDTO();

        when(commandeItemService.updateCommandeItem(eq(commandeItemId), any(CommandeItemDTO.class)))
                .thenReturn(dto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/admin/commandeItems/{commandeItemId}", commandeItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Update item fails when item does not exist")
    void testUpdateCommandeItem_whenNotFound_returns404() throws Exception {
        Long commandeItemId = 999L;
        CommandeItemDTO dto = new CommandeItemDTO();

        when(commandeItemService.updateCommandeItem(eq(commandeItemId), any(CommandeItemDTO.class)))
                .thenThrow(new ResourceNotFoundException("Commande Item", "id", commandeItemId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/admin/commandeItems/{commandeItemId}", commandeItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("CommandeItem can be deleted")
    void testDeleteCommandeItem_whenValid_returnsDeleted() throws Exception {
        Long commandeItemId = 1L;
        CommandeItemDTO dto = new CommandeItemDTO();

        when(commandeItemService.deleteCommandeItem(eq(commandeItemId))).thenReturn(dto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/admin/commandeItems/{commandeItemId}", commandeItemId)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Delete item fails when item does not exist")
    void testDeleteCommandeItem_whenNotFound_returns404() throws Exception {
        Long commandeItemId = 999L;

        when(commandeItemService.deleteCommandeItem(eq(commandeItemId)))
                .thenThrow(new ResourceNotFoundException("Commande Item", "id", commandeItemId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/admin/commandeItems/{commandeItemId}", commandeItemId)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Get items by commande returns list")
    void testGetCommandeItemsByCommandeId_returnsItems() throws Exception {
        Long commandeId = 1L;

        when(commandeItemService.getCommandeItemsByCommandeId(eq(commandeId)))
                .thenReturn(List.of(new CommandeItemDTO(), new CommandeItemDTO()));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/public/commandeItems/commandes/{commandeId}", commandeId)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();
        CommandeItemDTO[] items = new ObjectMapper().readValue(responseBody, CommandeItemDTO[].class);

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(2, items.length);
    }
}