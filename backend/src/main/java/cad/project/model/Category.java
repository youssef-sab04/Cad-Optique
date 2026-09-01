package cad.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank
    private String nom;

    private String description;
    private Float tva;

    @OneToMany(mappedBy = "category")
    private List<Produit> produitList;

    @CreationTimestamp
    private LocalDateTime createdAt;



}
