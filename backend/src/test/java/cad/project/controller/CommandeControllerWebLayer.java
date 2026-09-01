package cad.project.controller;

import cad.project.exceptions.ResourceNotFoundException;
import cad.project.playload.CommandeDTO;
import cad.project.playload.CommandeResponse;
import cad.project.security.WebSecurityConfig;
import cad.project.security.jwt.JwtUtils;
import cad.project.service.CommandeService;
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
@WebMvcTest(controllers = CommandeController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class))
public class CommandeControllerWebLayer {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    CommandeService commandeService;

    @MockitoBean
    JwtUtils jwtUtils;

    @Test
    @DisplayName("Get all commandes returns paginated response")
    void testGetAllCommandes_returnsPaginatedResponse() throws Exception {
        CommandeResponse response = new CommandeResponse();
        response.setContent(List.of(new CommandeDTO(), new CommandeDTO()));
        response.setPageNumber(0);
        response.setPageSize(10);
        response.setTotalElements(2L);

        when(commandeService.getAllCommandes(eq(0), eq(10), eq("asc")))
                .thenReturn(response);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/public/commandes")
                .param("pageNumber", "0")
                .param("pageSize", "10")
                .param("sortOrder", "asc")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String body = mvcResult.getResponse().getContentAsString();
        CommandeResponse actual = new ObjectMapper().readValue(body, CommandeResponse.class);

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(2, actual.getContent().size());
    }

    @Test
    @DisplayName("Commande can be created")
    void testAddCommande_whenValid_returnsCreated() throws Exception {
        Long fournisseurId = 1L;
        CommandeDTO dto = new CommandeDTO();

        when(commandeService.addCommande(eq(fournisseurId), any(CommandeDTO.class))).thenReturn(dto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/admin/commande/fournisseurs/{fournisseurId}", fournisseurId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Create commande fails when fournisseur does not exist")
    void testAddCommande_whenFournisseurNotFound_returns404() throws Exception {
        Long fournisseurId = 999L;
        CommandeDTO dto = new CommandeDTO();

        when(commandeService.addCommande(eq(fournisseurId), any(CommandeDTO.class)))
                .thenThrow(new ResourceNotFoundException("Fournisseur", "id", fournisseurId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/admin/commande/fournisseurs/{fournisseurId}", fournisseurId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Valider commande succeeds")
    void testValiderCommande_whenValid_returnsCreated() throws Exception {
        Long commandeId = 1L;
        CommandeDTO dto = new CommandeDTO();

        when(commandeService.ValiderCommande(eq(commandeId))).thenReturn(dto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/admin/commande/{commandeId}", commandeId)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Valider commande fails when commande does not exist")
    void testValiderCommande_whenNotFound_returns404() throws Exception {
        Long commandeId = 999L;

        when(commandeService.ValiderCommande(eq(commandeId)))
                .thenThrow(new ResourceNotFoundException("Commande", "id", commandeId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/admin/commande/{commandeId}", commandeId)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Update commande fails when commande does not exist")
    void testUpdateCommande_whenNotFound_returns404() throws Exception {
        Long commandeId = 999L;
        CommandeDTO dto = new CommandeDTO();

        when(commandeService.updateCommande(eq(commandeId), any(CommandeDTO.class)))
                .thenThrow(new ResourceNotFoundException("Commande", "id", commandeId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/admin/commandes/{commandeId}", commandeId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Delete commande fails when commande does not exist")
    void testDeleteCommande_whenNotFound_returns404() throws Exception {
        Long commandeId = 999L;

        when(commandeService.deleteCommande(eq(commandeId)))
                .thenThrow(new ResourceNotFoundException("Commande", "id", commandeId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/admin/commandes/{commandeId}", commandeId)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }
}