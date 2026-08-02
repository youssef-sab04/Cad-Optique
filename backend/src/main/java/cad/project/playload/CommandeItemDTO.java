package cad.project.playload;

import cad.project.model.Commande;
import cad.project.model.OrdonnanceLentille;
import cad.project.model.OrdonnanceLunette;
import cad.project.model.Produit;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor

public class CommandeItemDTO {
    private Long id;
    private ProduitDTO produitDTO;
    private CommandeDTO commande;
    private Integer quantity;
    private Double price;
    private LocalDateTime createdAt;
}