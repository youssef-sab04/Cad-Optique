package cad.project.playload;

import cad.project.model.Client;
import cad.project.model.Produit;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class NotificationDTO {


    private Long id;


    private ClientDTO clientDTO;
    private ProduitDTO produitDTO;

    private String type;
    private String description;
    private String message;
    private boolean is_read;
    private LocalDateTime createdAt;
}
