package cad.project.repositries;

import cad.project.model.Devis;
import cad.project.model.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DevisRepositry extends JpaRepository<Devis, Long> {

    @Query("SELECT COUNT(d) FROM Devis d WHERE YEAR(d.createdAt) = :annee")
    long countDevisByYear(@Param("annee") int annee);

    @Query("SELECT COUNT(d) FROM Devis d WHERE d.status = 'Valide' AND YEAR(d.createdAt) = :annee")
    long countDevisValideByYear(@Param("annee") int annee);

    @Query("SELECT COUNT(d) FROM Devis d WHERE d.status <> 'Valide' AND d.status <> 'Annulee'")
    long countDevisEnAttente();

    // [mois, nombreDevis, nombreDevisValide]
    @Query("SELECT MONTH(d.createdAt), COUNT(d), " +
            "SUM(CASE WHEN d.status = 'Valide' THEN 1 ELSE 0 END) " +
            "FROM Devis d WHERE YEAR(d.createdAt) = :annee GROUP BY MONTH(d.createdAt)")
    List<Object[]> statsDevisParMois(@Param("annee") int annee);
}