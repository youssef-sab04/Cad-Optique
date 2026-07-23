package cad.project.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor

public class DevisItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name="devis_id")
    private Devis devis;

    @ManyToOne
    @JoinColumn(name="produit_id")
    private Produit produit;


    private Integer quantity;
    private Double prixHT;
    private Float tva;
    private Float discount;
    private Double price;

    @CreationTimestamp
    private LocalDateTime createdAt;
}