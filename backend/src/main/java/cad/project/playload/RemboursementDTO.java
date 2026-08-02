package cad.project.playload;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RemboursementDTO {

    private Long id;
    private SalesOrderDTO salesOrderDTO;
    private String status;
    private String description;
    private Double montant_mutuelle;
    private Double montant_patient;
    private LocalDateTime createdAt;
}
