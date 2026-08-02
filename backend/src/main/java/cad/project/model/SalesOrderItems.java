package cad.project.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor

public class SalesOrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name="Order_id")
    private SalesOrder salesOrder;

    @ManyToOne
    @JoinColumn(name="produit_id")
    private Produit produit;

    @OneToOne(mappedBy = "salesOrderItems"  , cascade = {CascadeType.PERSIST, CascadeType.MERGE , CascadeType.REMOVE})
    private Mouvement_Stock mouvementStock;


    private Integer quantity;
    private Double prixHT;
    private Float tva;
    private Float discount;
    private Double price;

    @CreationTimestamp
    private LocalDateTime createdAt;
}