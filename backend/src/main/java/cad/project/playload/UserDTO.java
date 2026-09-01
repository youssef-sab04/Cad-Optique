package cad.project.playload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;
    private String userName;
    private String email;
    private String phoneNumber;

    private String cin;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private Set<String> roles;
    private LocalDateTime createdAt;
}