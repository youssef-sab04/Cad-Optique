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

public class SalesOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "salesOrder"  , cascade = {CascadeType.PERSIST, CascadeType.MERGE , CascadeType.REMOVE})
    List<SalesOrderItems> orderItemsList;

    @OneToOne(mappedBy = "salesOrder")
    private  Devis devis;

    @OneToOne(mappedBy = "salesOrder")
    private  Remboursement remboursement;

    @OneToMany(mappedBy = "salesOrder")
    private  List<Paiment> paimentList;


    @ManyToOne
    @JoinColumn(name = "client_id")
    private  Client client;

   private  String  status;
   private  double  montantReste;
   private  String description;
   private  String adresse;
   private double  Totalprice;

    @CreationTimestamp
    private LocalDateTime createdAt;
}