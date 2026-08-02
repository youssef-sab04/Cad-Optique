package cad.project.service;

import cad.project.playload.SalesOrderDTO;
import cad.project.playload.SalesOrderResponse;
import jakarta.validation.Valid;

public interface SalesOrderService {
    SalesOrderDTO addSalesOrder(Long clientId, @Valid SalesOrderDTO salesOrderDTO);

    SalesOrderDTO updateSalesOrder(Long salesOrderId, @Valid SalesOrderDTO salesOrderDTO);

    SalesOrderDTO deleteSalesOrder(Long salesOrderId);

    SalesOrderResponse getAllSalesOrders(Integer pageNumber, Integer pageSize, String sortOrder);

    SalesOrderDTO ValiderOrdre(Long ordreId);
}
