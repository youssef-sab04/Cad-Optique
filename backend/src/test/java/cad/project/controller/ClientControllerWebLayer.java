package cad.project.controller;

import cad.project.exceptions.ResourceNotFoundException;
import cad.project.playload.ClientDTO;
import cad.project.playload.ClientResponse;
import cad.project.security.WebSecurityConfig;
import cad.project.security.jwt.JwtUtils;
import cad.project.service.ClientService;
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
@WebMvcTest(controllers = ClientController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class))
public class ClientControllerWebLayer {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    ClientService clientService;

    @MockitoBean
    JwtUtils jwtUtils;

    @Test
    @DisplayName("Client can be created")
    void testAddClient_whenValid_returnsCreated() throws Exception {
        ClientDTO dto = new ClientDTO();
        dto.setNom("Alaoui");
        dto.setPrenom("Sara");

        when(clientService.addClient(any(ClientDTO.class))).thenReturn(dto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/admin/client")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        ClientDTO created = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), ClientDTO.class);

        Assertions.assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals("Alaoui", created.getNom());
    }

    @Test
    @DisplayName("Create client fails when nom is missing")
    void testAddClient_whenNomMissing_returns400() throws Exception {
        ClientDTO dto = new ClientDTO();
        dto.setNom("");
        dto.setPrenom("Sara");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/admin/client")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Get client by id succeeds")
    void testGetClient_whenValid_returnsClient() throws Exception {
        Long clientId = 1L;
        ClientDTO dto = new ClientDTO();
        dto.setNom("Alaoui");

        when(clientService.getClient(eq(clientId))).thenReturn(dto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/public/clients/{clientId}", clientId)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Get client fails when client does not exist")
    void testGetClient_whenNotFound_returns404() throws Exception {
        Long clientId = 999L;

        when(clientService.getClient(eq(clientId)))
                .thenThrow(new ResourceNotFoundException("Client", "id", clientId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/public/clients/{clientId}", clientId)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Update client fails when client does not exist")
    void testUpdateClient_whenNotFound_returns404() throws Exception {
        Long clientId = 999L;
        ClientDTO dto = new ClientDTO();
        dto.setNom("Alaoui");
        dto.setPrenom("Sara");

        when(clientService.updateClient(eq(clientId), any(ClientDTO.class)))
                .thenThrow(new ResourceNotFoundException("Client", "id", clientId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/admin/clients/{clientId}", clientId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Delete client fails when client does not exist")
    void testDeleteClient_whenNotFound_returns404() throws Exception {
        Long clientId = 999L;

        when(clientService.deleteClient(eq(clientId)))
                .thenThrow(new ResourceNotFoundException("Client", "id", clientId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/admin/clients/{clientId}", clientId)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Get all clients returns paginated response")
    void testGetAllClients_returnsPaginatedResponse() throws Exception {
        ClientResponse response = new ClientResponse();
        response.setContent(List.of(new ClientDTO(), new ClientDTO()));
        response.setPageNumber(0);
        response.setPageSize(10);
        response.setTotalElements(2L);

        when(clientService.getAlClients(eq(0), eq(10), eq("id"), eq("asc"), eq(null)))
                .thenReturn(response);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/public/clients")
                .param("pageNumber", "0")
                .param("pageSize", "10")
                .param("sortBy", "id")
                .param("sortOrder", "asc")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        ClientResponse actual = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), ClientResponse.class);

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(2, actual.getContent().size());
    }

    @Test
    @DisplayName("Get clients by nom/prenom returns paginated response")
    void testGetAllClientsPN_returnsPaginatedResponse() throws Exception {
        ClientResponse response = new ClientResponse();
        response.setContent(List.of(new ClientDTO()));
        response.setPageNumber(0);
        response.setPageSize(10);
        response.setTotalElements(1L);

        when(clientService.getAlClientsP_N(eq(0), eq(10), eq("id"), eq("asc"), eq("Alaoui"), eq("Sara")))
                .thenReturn(response);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/public/Clients/P-N")
                .param("pageNumber", "0")
                .param("pageSize", "10")
                .param("sortBy", "id")
                .param("sortOrder", "asc")
                .param("nom", "Alaoui")
                .param("prenom", "Sara")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        ClientResponse actual = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), ClientResponse.class);

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(1, actual.getContent().size());
    }
}