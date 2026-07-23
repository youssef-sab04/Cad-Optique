package cad.project.repositries;

import cad.project.model.AppRole;
import cad.project.model.Examen;
import cad.project.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository  extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
