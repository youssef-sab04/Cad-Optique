package cad.project.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor

public class Remboursement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne()
    @JoinColumn(name = "order_id")
    private  SalesOrder salesOrder;
    private  String  status;
   private  String description;
   private  Double montant_mutuelle;
   private  Double montant_patient;

    @CreationTimestamp
    private LocalDateTime createdAt;
}