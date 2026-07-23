package cad.project.playload;

import cad.project.model.Category;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduitDTO {

    private Long id;

    private String nom;

    private String description;

    private String image;

    private String code_barre;

    private  CategoryDTO categoryDTO;

    private Integer quantity;
    private Double prixHT;
    private Double prixAchat;
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

    private LocalDateTime createdAt;

}
