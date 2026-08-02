package cad.project.playload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopProduitDTO {
    private Long produitId;
    private String nom;
    private long quantiteVendue;
}