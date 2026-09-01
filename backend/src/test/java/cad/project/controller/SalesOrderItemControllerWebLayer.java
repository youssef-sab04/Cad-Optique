package cad.project.controller;

import cad.project.exceptions.ResourceNotFoundException;
import cad.project.playload.SalesOrderItemDTO;
import cad.project.playload.SalesOrderItemResponse;
import cad.project.security.WebSecurityConfig;
import cad.project.security.jwt.JwtUtils;
import cad.project.service.SalesOrderItemService;
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
@WebMvcTest(controllers = SalesOrderItemController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class))
public class SalesOrderItemControllerWebLayer {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    SalesOrderItemService salesOrderItemService;

    @MockitoBean
    JwtUtils jwtUtils;

    // SalesOrderItemController
    @Test
    @DisplayName("Get all sales order items returns paginated response")
    void testGetAllSalesOrderItems_returnsPaginatedResponse() throws Exception {
        SalesOrderItemResponse response = new SalesOrderItemResponse();
        response.setContent(List.of(new SalesOrderItemDTO(), new SalesOrderItemDTO()));
        response.setPageNumber(0);
        response.setPageSize(10);
        response.setTotalElements(2L);

        when(salesOrderItemService.getAllSalesOrderItems(eq(0), eq(10), eq("asc")))
                .thenReturn(response);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/public/salesorderitems")
                .param("pageNumber", "0")
                .param("pageSize", "10")
                .param("sortOrder", "asc")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        SalesOrderItemResponse actual = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), SalesOrderItemResponse.class);

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(2, actual.getContent().size());
    }

    @Test
    @DisplayName("SalesOrderItem can be created")
    void testAddSalesOrderItem_whenValidDetailsProvided_returnsCreatedItem() throws Exception {
        Long salesOrderId = 1L;
        Long produitId = 1L;
        SalesOrderItemDTO dto = new SalesOrderItemDTO();

        when(salesOrderItemService.addSalesOrderItem(eq(salesOrderId), eq(produitId), any(SalesOrderItemDTO.class)))
                .thenReturn(dto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/admin/salesorderitems/salesorders/{salesOrderId}/produits/{produitId}", salesOrderId, produitId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Add item fails when sales order does not exist")
    void testAddSalesOrderItem_whenSalesOrderNotFound_returns404() throws Exception {
        Long salesOrderId = 999L;
        Long produitId = 1L;
        SalesOrderItemDTO dto = new SalesOrderItemDTO();

        when(salesOrderItemService.addSalesOrderItem(eq(salesOrderId), eq(produitId), any(SalesOrderItemDTO.class)))
                .thenThrow(new ResourceNotFoundException("Sales Order", "id", salesOrderId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/admin/salesorderitems/salesorders/{salesOrderId}/produits/{produitId}", salesOrderId, produitId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Add item fails when produit does not exist")
    void testAddSalesOrderItem_whenProduitNotFound_returns404() throws Exception {
        Long salesOrderId = 1L;
        Long produitId = 999L;
        SalesOrderItemDTO dto = new SalesOrderItemDTO();

        when(salesOrderItemService.addSalesOrderItem(eq(salesOrderId), eq(produitId), any(SalesOrderItemDTO.class)))
                .thenThrow(new ResourceNotFoundException("Produit", "id", produitId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/admin/salesorderitems/salesorders/{salesOrderId}/produits/{produitId}", salesOrderId, produitId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("SalesOrderItem can be updated")
    void testUpdateSalesOrderItem_whenValidDetailsProvided_returnsUpdatedItem() throws Exception {
        Long salesOrderItemId = 1L;
        SalesOrderItemDTO dto = new SalesOrderItemDTO();

        when(salesOrderItemService.updateSalesOrderItemQuantity(eq(salesOrderItemId), any(SalesOrderItemDTO.class)))
                .thenReturn(dto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/admin/salesorderitems/{salesOrderItemId}", salesOrderItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Update item fails when item does not exist")
    void testUpdateSalesOrderItem_whenNotFound_returns404() throws Exception {
        Long salesOrderItemId = 999L;
        SalesOrderItemDTO dto = new SalesOrderItemDTO();

        when(salesOrderItemService.updateSalesOrderItemQuantity(eq(salesOrderItemId), any(SalesOrderItemDTO.class)))
                .thenThrow(new ResourceNotFoundException("Sales Order Item", "id", salesOrderItemId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/admin/salesorderitems/{salesOrderItemId}", salesOrderItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("SalesOrderItem can be deleted")
    void testDeleteSalesOrderItem_whenValid_returnsDeletedItem() throws Exception {
        Long salesOrderItemId = 1L;
        SalesOrderItemDTO dto = new SalesOrderItemDTO();

        when(salesOrderItemService.deleteSalesOrderItem(eq(salesOrderItemId)))
                .thenReturn(dto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/admin/salesorderitems/{salesOrderItemId}", salesOrderItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Delete item fails when item does not exist")
    void testDeleteSalesOrderItem_whenNotFound_returns404() throws Exception {
        Long salesOrderItemId = 999L;

        when(salesOrderItemService.deleteSalesOrderItem(eq(salesOrderItemId)))
                .thenThrow(new ResourceNotFoundException("Sales Order Item", "id", salesOrderItemId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/admin/salesorderitems/{salesOrderItemId}", salesOrderItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Get all SalesOrderItems returns list")
    void testGetAllSalesOrderItems_returnsOk() throws Exception {
        when(salesOrderItemService.getAllSalesOrderItems(any(), any(), any()))
                .thenReturn(null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/public/salesorderitems")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Get SalesOrderItems by sales order id returns list")
    void testGetSalesOrderItemsBySalesOrderId_returnsOk() throws Exception {
        Long salesOrderId = 1L;

        when(salesOrderItemService.getSalesOrderItemsBySalesOrderId(eq(salesOrderId)))
                .thenReturn(List.of(new SalesOrderItemDTO()));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/public/salesorderitems/salesorders/{salesOrderId}", salesOrderId)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Get items by sales order returns both added items")
    void testGetSalesOrderItemsBySalesOrderId_returnsTwoItems() throws Exception {
        Long salesOrderId = 1L;

        SalesOrderItemDTO item1 = new SalesOrderItemDTO();
        item1.setId(1L);

        SalesOrderItemDTO item2 = new SalesOrderItemDTO();
        item2.setId(2L);

        when(salesOrderItemService.getSalesOrderItemsBySalesOrderId(eq(salesOrderId)))
                .thenReturn(List.of(item1, item2));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/public/salesorderitems/salesorders/{salesOrderId}", salesOrderId)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        SalesOrderItemDTO[] items = new ObjectMapper().readValue(responseBody, SalesOrderItemDTO[].class);

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(2, items.length);
        Assertions.assertEquals(1L, items[0].getId());
        Assertions.assertEquals(2L, items[1].getId());
    }
}