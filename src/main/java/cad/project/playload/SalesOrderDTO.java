package cad.project.playload;

import cad.project.model.Client;
import cad.project.model.SalesOrderItems;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor

public class SalesOrderDTO {


  private Long id;

    List<SalesOrderItemDTO> salesOrderItemDTOS;


    private ClientDTO clientDTO;

   private  String  status; //  payee, partielle, annulee  // envoye, accepte, refuse
   private  String description;
   private  String adresse;
   private double  Totalprice;
    private LocalDateTime createdAt;
}