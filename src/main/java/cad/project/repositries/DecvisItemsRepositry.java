package cad.project.repositries;

import cad.project.model.DevisItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DecvisItemsRepositry extends JpaRepository<DevisItems, Long> {
    List<DevisItems> findByDevisId(Long id);
}