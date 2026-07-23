package cad.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor

public class Fournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nom;

    @NotBlank

    private String phoneNumber;

    private String adresse;

    @Email
    private String email;

    @OneToMany(mappedBy = "fournisseur")
    List<Produit>produits;

    @OneToMany(mappedBy = "fournisseur")
    List<Commande> commandes;

    @CreationTimestamp
    private LocalDateTime createdAt;
}