package cad.project.repositries;

import cad.project.model.SalesOrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleOrderItemsRepositry extends JpaRepository<SalesOrderItems, Long> {
    List<SalesOrderItems> findBySalesOrderId(Long salesOrderId);

    List<SalesOrderItems> findByProduitId(Long produitId);
}
