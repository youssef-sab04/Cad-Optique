package cad.project.service;

import cad.project.playload.SalesOrderItemDTO;
import cad.project.playload.SalesOrderItemResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface SalesOrderItemService {
    SalesOrderItemDTO addSalesOrderItem(Long salesOrderId, Long produitId, @Valid SalesOrderItemDTO salesOrderItemDTO);

    SalesOrderItemDTO updateSalesOrderItemQuantity(Long salesOrderItemId, @Valid SalesOrderItemDTO salesOrderItemDTO);

    SalesOrderItemDTO deleteSalesOrderItem(Long salesOrderItemId);

    SalesOrderItemResponse getAllSalesOrderItems(Integer pageNumber, Integer pageSize, String sortOrder);

    List<SalesOrderItemDTO> getSalesOrderItemsBySalesOrderId(Long salesOrderId);
}
