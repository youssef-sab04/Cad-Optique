package cad.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "clients" , uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phoneNumber")
})
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    private String adresse;

    @Email
    @Column(name = "email")
    private String email;

    private String mutuelle;

    @OneToMany(mappedBy = "client" , cascade = {CascadeType.PERSIST, CascadeType.MERGE , CascadeType.REMOVE})
    private List<OrdonnanceLunette> ordonnanceLunette;

    @OneToMany(mappedBy = "client" , cascade = {CascadeType.PERSIST, CascadeType.MERGE , CascadeType.REMOVE})
    private List<OrdonnanceLentille> ordonnanceLentilles;

    @OneToMany(mappedBy = "client" , cascade = {CascadeType.PERSIST, CascadeType.MERGE , CascadeType.REMOVE})
    private List<Examen> examen;

    @OneToMany(mappedBy = "client" , cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<SalesOrder> salesOrders;

    @OneToMany(mappedBy = "client" , cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Devis> devis ;

    @OneToMany(mappedBy = "client" , cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Paiment> paiments ;

    @OneToMany(mappedBy = "client" , cascade = {CascadeType.PERSIST, CascadeType.MERGE , CascadeType.REMOVE} )
    private List<Notification> notifications ;



    private LocalDate dateNaissance;
    private LocalDate dernierExamen;


    @CreationTimestamp
    private LocalDateTime createdAt;
}