package cad.project.repositries;

import cad.project.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientRepositry extends JpaRepository<Client, Long> {
    Page<Client> findAll(Specification<Client> spec, Pageable pageDetails);

    @Query("SELECT COUNT(c) FROM Client c WHERE YEAR(c.createdAt) = :annee")
    long countNouveauxClientsByYear(@Param("annee") int annee);
}