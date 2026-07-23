package cad.project.repositries;

import cad.project.model.Remboursement;
import cad.project.model.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RemboursementRepositry extends JpaRepository<Remboursement, Long> {
    Optional<Remboursement> findBySalesOrder(SalesOrder salesOrder);
}
