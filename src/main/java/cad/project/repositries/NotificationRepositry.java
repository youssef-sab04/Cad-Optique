package cad.project.repositries;

import cad.project.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepositry extends JpaRepository<Notification ,  Long> {

    Page<Notification> findByClientIsNull(Pageable pageDetails);

    Page<Notification> findByClientId(Long clientId, Pageable pageDetails);

    List<Notification> findByProduitId(Long produitId);

    boolean existsByClientIdAndTypeAndMessage(Long id, String rappelOrdonnanceLunette, String s);

    Page<Notification> findByProduitIsNull(Pageable pageDetails);

    @Query("SELECT n FROM Notification n WHERE n.client IS NOT NULL AND " +
            "(LOWER(n.client.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(n.client.prenom) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Notification> findAllClientByKeyword(String keyword, Pageable pageDetails);

    Page<Notification> findByClientIsNullAndProduitNomContainingIgnoreCase(String keyword, Pageable pageDetails);
}
