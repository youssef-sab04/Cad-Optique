package cad.project.playload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class ClientResponse {
    private List<ClientDTO> Content;
    private List<OrdonnanceLunetteDTO> ordonnanceLunetteDTOS;
    private List<OrdonnanceLentilleDTO> ordonnanceLentilleDTOS;
    private List<ExamenDTO> examenDTOS;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
}
