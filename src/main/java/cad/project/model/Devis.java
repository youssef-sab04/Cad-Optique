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

public class Devis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "devis" , cascade = {CascadeType.PERSIST, CascadeType.MERGE , CascadeType.REMOVE})
    List<DevisItems> devisItemsList;

    @OneToOne
    @JoinColumn(name = "order_id")
    private SalesOrder salesOrder ;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private  Client client;

   private  String  status;
   private  String description;
   private double  Totalprice;

    @CreationTimestamp
    private LocalDateTime createdAt;
}