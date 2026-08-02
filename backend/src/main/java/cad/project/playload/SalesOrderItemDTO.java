package cad.project.playload;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor

public class SalesOrderItemDTO {
    private Long id;
    private SalesOrderDTO salesOrderDTO;
    private ProduitDTO produitDTO;
    private Integer quantity;
    private Double prixHT;
    private Float tva;
    private Float discount;
    private Double price;
    private LocalDateTime createdAt;
}
