package cad.project.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor

public class CommandeItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_commande")
    private Commande commande;

    @ManyToOne
    @JoinColumn(name = "id_product")
    private Produit produit;

    @OneToOne
    @JoinColumn(name = "ordonnance_lunette_id")
    private OrdonnanceLunette ordonnanceLunette;

    @OneToOne
    @JoinColumn(name = "ordonnance_lentille_id")
    private OrdonnanceLentille ordonnanceLentille;

    private Integer quantity;
    private Double price;



    @CreationTimestamp
    private LocalDateTime createdAt;
}