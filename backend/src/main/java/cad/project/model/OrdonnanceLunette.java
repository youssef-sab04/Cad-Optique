package cad.project.model;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "ordonnances_lunette")
public class OrdonnanceLunette {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    private String prescripteur;
    private LocalDate dateEmission;
    private LocalDate dateExpiration;


    private Float sphereOd;
    private Float cylindreOd;
    private Integer axeOd;
    private Float additionOd;

    private Float sphereOg;
    private Float cylindreOg;
    private Integer axeOg;
    private Float additionOg;

    private String image;

    @OneToOne(mappedBy = "ordonnanceLunette")
    private  CommandeItem commandeIteme;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}