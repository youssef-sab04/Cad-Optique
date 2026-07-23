package cad.project.playload;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor

public class CommandeDTO {


    private Long id;
    private FournisseurDTO fournisseurDTO;
    private  String  status;
    private  String description;
    private double  Totalprice;



    private LocalDateTime createdAt;
}