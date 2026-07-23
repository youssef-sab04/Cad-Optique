package cad.project.playload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamenDTO {


    private Long id;


    private LocalDate dateExamen;
    private  ClientDTO clientDTO;

    private Float sphereOd;
    private Float cylindreOd;
    private Integer axeOd;
    private Float ecartOd;


    private Float sphereOg;
    private Float cylindreOg;
    private Integer axeOg;
    private Float ecartOg;

    private Float addition;

    private String remarques;
    private LocalDate prochaineVisite;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
