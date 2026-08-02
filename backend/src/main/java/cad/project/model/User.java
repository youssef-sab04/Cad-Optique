package cad.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String nom;

    @NotBlank
    private String prenom;

    @NotBlank
    @Size(max = 20)
    @Column(name = "username" , unique = true)
    private String userName;
    @NotBlank
    @Size(max = 50)
    @Email
    @Column(name = "email")
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @NotBlank
    @Size(max = 120)
    private String cin;

    private String phoneNumber;

    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> userRoles;

    @CreationTimestamp
    private LocalDateTime createdAt;


    public User(@NotBlank @Size(min = 3, max = 20) String username, @NotBlank @Size(max = 50) @Email String email, String encode) {
    }
}
