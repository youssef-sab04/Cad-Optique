package cad.project.playload;

import cad.project.model.DevisItems;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor

public class DevisDTO {


  private Long id;

    private List<DevisItemDTO> devisItemDTOS;
    private SalesOrderDTO salesOrderDTO;


    private ClientDTO clientDTO;

   private  String  status; // envoye, accepte, refuse
   private  String description;
   private double  Totalprice;
   private LocalDateTime createdAt;
}