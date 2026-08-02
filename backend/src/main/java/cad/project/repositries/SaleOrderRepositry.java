package cad.project.repositries;

import cad.project.model.Commande;
import cad.project.model.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SaleOrderRepositry extends JpaRepository<SalesOrder, Long> {

    @Query("SELECT COALESCE(SUM(s.Totalprice), 0) FROM SalesOrder s " +
            "WHERE s.status = 'Valide' AND YEAR(s.createdAt) = :annee")
    double sumCaByYear(@Param("annee") int annee);

    @Query("SELECT COUNT(s) FROM SalesOrder s WHERE s.status = 'Valide' AND YEAR(s.createdAt) = :annee")
    long countVentesByYear(@Param("annee") int annee);

    @Query("SELECT COALESCE(SUM(s.montantReste), 0) FROM SalesOrder s WHERE YEAR(s.createdAt) = :annee")
    double sumMontantResteByYear(@Param("annee") int annee);

    // [mois, nombreVentes, ca]
    @Query("SELECT MONTH(s.createdAt), COUNT(s), COALESCE(SUM(s.Totalprice), 0) FROM SalesOrder s " +
            "WHERE s.status = 'Valide' AND YEAR(s.createdAt) = :annee GROUP BY MONTH(s.createdAt)")
    List<Object[]> statsVentesParMois(@Param("annee") int annee);
}