package cad.project.repositries;

import cad.project.model.Client;
import cad.project.model.Paiment;
import cad.project.model.Produit;
import cad.project.model.SalesOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaimentRepositry extends JpaRepository<Paiment, Long> {

    List<Paiment> findByClient(Client client);

    List<Paiment> findBySalesOrder(SalesOrder salesOrder);
}
