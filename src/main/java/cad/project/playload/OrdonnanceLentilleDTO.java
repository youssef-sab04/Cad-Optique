package cad.project.playload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdonnanceLentilleDTO {


    private Long id;

    private ClientDTO clientDTO;


    private String prescripteur;
    private LocalDate dateEmission;
    private LocalDate dateExpiration;

    private Float sphereOd;
    private Float cylindreOd;
    private Integer axeOd;
    private Float rayonOd;
    private Float diametreOd;
    private String matiereOd;

    private Float sphereOg;
    private Float cylindreOg;
    private Integer axeOg;
    private Float rayonOg;
    private Float diametreOg;
    private String matiereOg;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
