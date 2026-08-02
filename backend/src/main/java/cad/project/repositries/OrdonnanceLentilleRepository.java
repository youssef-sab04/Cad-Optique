package cad.project.repositries;

import cad.project.model.OrdonnanceLentille;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OrdonnanceLentilleRepository extends JpaRepository<OrdonnanceLentille, Long> {
    List<OrdonnanceLentille> findByClientId(Long clientId);
    Page<OrdonnanceLentille> findAll(Specification<OrdonnanceLentille> spec, Pageable pageDetails);

    List<OrdonnanceLentille> findByDateExpirationBetween(LocalDate today, LocalDate limite);
}
