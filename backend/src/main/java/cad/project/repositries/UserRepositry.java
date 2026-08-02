package cad.project.repositries;

import cad.project.model.AppRole;
import cad.project.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepositry extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);

    boolean existsByUserName(String admin);

    boolean existsByEmail(@NotBlank @Size(max = 50) @Email String email);
    Page<User> findByUserRoles_RoleName(AppRole roleName, Pageable pageable);
}
