package cad.project.playload;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor

public class PaimentDTO {


    private Long id;
    private  String description ;
    private SalesOrderDTO salesOrderDTO;
    private double  montant_Paye;
    private double  montant_reste;
    private  boolean payment_Total ;
    private  String method;
    private LocalDateTime createdAt;
}