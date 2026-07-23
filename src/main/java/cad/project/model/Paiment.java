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

public class Paiment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String description ;

    @ManyToOne
    @JoinColumn(name="order_id")
    private  SalesOrder salesOrder;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private  Client client;

    @NotBlank
    private double  montant_Paye;

    private  String method;

    @CreationTimestamp
    private LocalDateTime createdAt;
}