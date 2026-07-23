package cad.project.repositries;

import cad.project.model.CommandeItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandeItemRepositry extends JpaRepository<CommandeItem, Long> {
    List<CommandeItem> findByCommandeId(Long commandeId);
}
