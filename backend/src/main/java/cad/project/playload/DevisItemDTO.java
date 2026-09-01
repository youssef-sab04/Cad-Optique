package cad.project.playload;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor

public class DevisItemDTO {
    private Long id;
    private DevisDTO devisDTO;
    private ProduitDTO produitDTO;

    @PositiveOrZero
    private Integer quantity;

    private Double prixHT;
    private Float tva;
    private Float discount;
    private Double price;
    private LocalDateTime createdAt;
}
