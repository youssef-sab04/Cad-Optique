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
@Table(name = "examens")
public class Examen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private LocalDate dateExamen;

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

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
