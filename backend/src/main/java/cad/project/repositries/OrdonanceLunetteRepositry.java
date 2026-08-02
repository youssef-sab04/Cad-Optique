package cad.project.repositries;

import cad.project.model.Client;
import cad.project.model.OrdonnanceLunette;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OrdonanceLunetteRepositry extends JpaRepository<OrdonnanceLunette, Long> {
    List<OrdonnanceLunette> findByClientId(Long clientId);
    Page<OrdonnanceLunette> findAll(Specification<OrdonnanceLunette> spec, Pageable pageDetails);

    List<OrdonnanceLunette> findByDateExpirationBetween(LocalDate today, LocalDate limite);
}
