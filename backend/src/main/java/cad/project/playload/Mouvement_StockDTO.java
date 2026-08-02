package cad.project.playload;

import cad.project.model.CommandeItem;
import cad.project.model.Produit;
import cad.project.model.SalesOrderItems;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Mouvement_StockDTO {


    private Long id;
    private String description;
    private int quantity;
    private  String type ;

    private ProduitDTO produitDTO;
    private CommandeItemDTO commandeItemDTO;

    private  double prix_Unit;
    private  double prix_total;

    private SalesOrderItemDTO salesOrderItemDTO;
    private LocalDateTime createdAt;



}
