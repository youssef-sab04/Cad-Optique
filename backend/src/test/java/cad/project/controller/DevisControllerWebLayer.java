package cad.project.controller;

import cad.project.exceptions.ResourceNotFoundException;
import cad.project.playload.DevisDTO;
import cad.project.playload.DevisResponse;
import cad.project.security.WebSecurityConfig;
import cad.project.security.jwt.JwtUtils;
import cad.project.service.DevisPdfService;
import cad.project.service.DevisService;
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
@WebMvcTest(controllers = DevisController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class))
public class DevisControllerWebLayer {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    DevisService devisService;

    @MockitoBean
    DevisPdfService devisPdfService;

    @MockitoBean
    JwtUtils jwtUtils;

    @Test
    @DisplayName("Get all devis returns paginated response")
    void testGetAllDevis_returnsPaginatedResponse() throws Exception {
        DevisResponse response = new DevisResponse();
        response.setContent(List.of(new DevisDTO(), new DevisDTO()));
        response.setPageNumber(0);
        response.setPageSize(10);
        response.setTotalElements(2L);

        when(devisService.getAllDevis(eq(0), eq(10), eq("asc")))
                .thenReturn(response);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/public/devis")
                .param("pageNumber", "0")
                .param("pageSize", "10")
                .param("sortOrder", "asc")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        DevisResponse actual = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), DevisResponse.class);

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(2, actual.getContent().size());
    }

    @Test
    @DisplayName("Devis can be created")
    void testAddDevis_whenValidDetailsProvided_returnsCreatedDevis() throws Exception {
        Long clientId = 1L;
        DevisDTO dto = new DevisDTO();

        when(devisService.addDevis(eq(clientId), any(DevisDTO.class))).thenReturn(dto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/admin/devis/clients/{clientId}", clientId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Create devis fails when client does not exist")
    void testAddDevis_whenClientNotFound_returns404() throws Exception {
        Long clientId = 999L;
        DevisDTO dto = new DevisDTO();

        when(devisService.addDevis(eq(clientId), any(DevisDTO.class)))
                .thenThrow(new ResourceNotFoundException("Client", "id", clientId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/admin/devis/clients/{clientId}", clientId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Update devis fails when devis does not exist")
    void testUpdateDevis_whenNotFound_returns404() throws Exception {
        Long devisId = 999L;
        DevisDTO dto = new DevisDTO();

        when(devisService.updateDevis(eq(devisId), any(DevisDTO.class)))
                .thenThrow(new ResourceNotFoundException("Devis", "id", devisId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/admin/devis/{devisId}", devisId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Delete devis fails when devis does not exist")
    void testDeleteDevis_whenNotFound_returns404() throws Exception {
        Long devisId = 999L;

        when(devisService.deleteDevis(eq(devisId)))
                .thenThrow(new ResourceNotFoundException("Devis", "id", devisId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/admin/devis/{devisId}", devisId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Cancel devis fails when devis does not exist")
    void testCancelDevis_whenNotFound_returns404() throws Exception {
        Long devisId = 999L;

        when(devisService.cancelDevis(eq(devisId)))
                .thenThrow(new ResourceNotFoundException("Devis", "id", devisId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/admin/devis/{devisId}/cancel", devisId)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Confirm devis succeeds")
    void testConfirmDevis_whenValid_returnsCreated() throws Exception {
        Long devisId = 1L;
        DevisDTO dto = new DevisDTO();

        when(devisService.confirmDevis(eq(devisId))).thenReturn(dto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/admin/devis/{devisId}/confirm", devisId)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Confirm devis fails when devis does not exist")
    void testConfirmDevis_whenNotFound_returns404() throws Exception {
        Long devisId = 999L;

        when(devisService.confirmDevis(eq(devisId)))
                .thenThrow(new ResourceNotFoundException("Devis", "id", devisId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/admin/devis/{devisId}/confirm", devisId)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }
}