package cad.project.playload;

import cad.project.model.Produit;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor

public class FournisseurDTO {


    private Long id;
    private String nom;
    private String phoneNumber;
    private String adresse;
    private String email;
    private LocalDateTime createdAt;


}