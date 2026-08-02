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
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String description;

    private String image;

    private String code_barre;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    @OneToMany(mappedBy = "produit")
    private List<CommandeItem> commandeItemP;

    @OneToMany(mappedBy = "produit")
    private List<Notification> notifications;

    @OneToMany(mappedBy = "produit")
    List<SalesOrderItems> salesOrderItems;

    @OneToMany(mappedBy = "produit")
    List<DevisItems> devisItems;


    private Integer quantity;
    private Double prixAchat;
    private Double prixHT;
    private Float tva;
    private Float discount;
    private Double price;
    private String marque;
    private String couleur;
    private String modele;
    private Float indice;
    private Float diametre;
    private Integer seuilMin;
    private String traitement;

    @CreationTimestamp
    private LocalDateTime createdAt;




    }




