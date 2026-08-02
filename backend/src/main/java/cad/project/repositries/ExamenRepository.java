package cad.project.repositries;

import cad.project.model.Examen;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.util.List;

public interface ExamenRepository extends JpaRepository<Examen, Long> {

    List<Examen> findByClientId(Long clientId);
    Page<Examen> findAll(Specification<Examen> spec, Pageable pageDetails);

    List<Examen> findByProchaineVisiteBetween(LocalDate today, LocalDate limite);
}
