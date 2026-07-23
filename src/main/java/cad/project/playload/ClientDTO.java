package cad.project.playload;

import cad.project.model.OrdonnanceLunette;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {

    private Long id;
    private String nom;
    private String prenom;
    private String phoneNumber;
    private String adresse;
    private String email;
    private String mutuelle;
    private LocalDate dateNaissance;
    private LocalDate dernierExamen;
    private LocalDateTime createdAt;
}
