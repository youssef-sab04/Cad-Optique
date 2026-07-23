package cad.project.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Mouvement_Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String description;
    private Integer quantity;
    private  String type ;
    private  double prix_Unit;
    private  double prix_total;



    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;

    @OneToOne
    @JoinColumn(name = "CommandeItem_id")
    private  CommandeItem commandeItem;

    @OneToOne
    @JoinColumn(name = "SaleOrderItem_id")
    private  SalesOrderItems salesOrderItems;



    @CreationTimestamp
    private LocalDateTime createdAt;



}
