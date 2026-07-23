package cad.project.repositries;

import cad.project.model.Mouvement_Stock;
import cad.project.model.Produit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface Mouvement_StockRepositry extends JpaRepository<Mouvement_Stock, Long> {

    Page<Mouvement_Stock> findAllByProduit(Produit produit, Pageable pageDetails);

    @Query("SELECT COUNT(m) FROM Mouvement_Stock m WHERE m.createdAt >= :debut AND m.createdAt < :fin")
    long countMouvementsEntre(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);

    // [produitId, nom, quantiteVendue] triés du plus vendu au moins vendu
    @Query("SELECT m.produit.id, m.produit.nom, SUM(m.quantity) as qte FROM Mouvement_Stock m " +
            "WHERE m.type = 'SORTIE' AND YEAR(m.createdAt) = :annee " +
            "GROUP BY m.produit.id, m.produit.nom ORDER BY qte DESC")
    List<Object[]> topProduitsVendus(@Param("annee") int annee, Pageable pageable);
}