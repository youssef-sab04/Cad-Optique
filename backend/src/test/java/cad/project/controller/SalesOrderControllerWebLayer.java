package cad.project.controller;

import cad.project.exceptions.ResourceNotFoundException;
import cad.project.playload.SalesOrderDTO;
import cad.project.playload.SalesOrderResponse;
import cad.project.security.WebSecurityConfig;
import cad.project.security.jwt.JwtUtils;
import cad.project.service.SalesOrderPdfService;
import cad.project.service.SalesOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.security.SecurityConfig;
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
@WebMvcTest(controllers = SalesOrderController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class))
public class SalesOrderControllerWebLayer {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    SalesOrderService salesOrderService;

    @MockitoBean
    JwtUtils jwtUtils;

    @MockitoBean
    SalesOrderPdfService salesOrderPdfService;

    // SalesOrderController
    @Test
    @DisplayName("Get all sales orders returns paginated response")
    void testGetAllSalesOrders_returnsPaginatedResponse() throws Exception {
        SalesOrderResponse response = new SalesOrderResponse();
        response.setContent(List.of(new SalesOrderDTO(), new SalesOrderDTO()));
        response.setPageNumber(0);
        response.setPageSize(10);
        response.setTotalElements(2L);

        when(salesOrderService.getAllSalesOrders(eq(0), eq(10), eq("asc")))
                .thenReturn(response);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/public/salesorders")
                .param("pageNumber", "0")
                .param("pageSize", "10")
                .param("sortOrder", "asc")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        SalesOrderResponse actual = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), SalesOrderResponse.class);

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(2, actual.getContent().size());
    }

    @Test
    @DisplayName("SalesOrder can be created")
    void testAddSalesOrder_whenValidDetailsProvided_returnsCreatedSalesOrder() throws Exception {

        Long clientId = 1L;
        SalesOrderDTO salesOrderDTO = new SalesOrderDTO();
        salesOrderDTO.setDescription("testttt");

        when(salesOrderService.addSalesOrder(eq(clientId), any(SalesOrderDTO.class)))
                .thenReturn(salesOrderDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/admin/salesorder/clients/{clientId}", clientId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(salesOrderDTO));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Create sale fails when client does not exist")
    void testAddSalesOrder_whenClientNotFound_returns404() throws Exception {
        Long clientId = 999L;
        SalesOrderDTO salesOrderDTO = new SalesOrderDTO();

        when(salesOrderService.addSalesOrder(eq(clientId), any(SalesOrderDTO.class)))
                .thenThrow(new ResourceNotFoundException("Client", "id", clientId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/admin/salesorder/clients/{clientId}", clientId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(salesOrderDTO));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Update sale fails when sales does not exist")
    void testUpdateSalesOrder_whenClientNotFound_returns404() throws Exception {
        Long salesOrderId = 999L;

        SalesOrderDTO salesOrderDTO = new SalesOrderDTO();
        salesOrderDTO.setDescription("a  a aa ");

        when(salesOrderService.updateSalesOrder(eq(salesOrderId), any(SalesOrderDTO.class)))
                .thenThrow(new ResourceNotFoundException("Sales Order", "id", salesOrderId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/admin/salesorder/{salesOrderId}", salesOrderId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(salesOrderDTO));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Delete sale fails when sales does not exist")
    void testDeleteSalesOrder_whenClientNotFound_returns404() throws Exception {
        Long salesOrderId = 999L;

        SalesOrderDTO salesOrderDTO = new SalesOrderDTO();
        salesOrderDTO.setDescription("a  a aa ");

        when(salesOrderService.deleteSalesOrder(eq(salesOrderId)))
                .thenThrow(new ResourceNotFoundException("Sales Order", "id", salesOrderId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/admin/salesorder/{salesOrderId}", salesOrderId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }


    @Test
    @DisplayName("Valid sale fails when sales does not exist")
    void test() throws Exception {
        Long salesOrderId = 999L;



        when(salesOrderService.ValiderOrdre(eq(salesOrderId)))
                .thenThrow(new ResourceNotFoundException("Sales Order", "id", salesOrderId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/admin/ordre/{salesOrderId}", salesOrderId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }



}
