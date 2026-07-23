package cad.project.repositries;

import cad.project.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeRepositry extends JpaRepository<Commande, Long> {
}
