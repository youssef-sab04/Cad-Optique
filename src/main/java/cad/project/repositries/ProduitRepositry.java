package cad.project.repositries;

import cad.project.model.Produit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProduitRepositry extends JpaRepository<Produit, Long> {
    Page<Produit> findAll(Specification<Produit> spec, Pageable pageDetails);

    @Query("SELECT COUNT(p) FROM Produit p WHERE p.quantity <= p.seuilMin")
    long countStockFaible();

    // valeur du stock au prix d'achat (coût du stock détenu)
    @Query("SELECT COALESCE(SUM(p.quantity * p.prixAchat), 0) FROM Produit p")
    double valeurTotaleStock();
}