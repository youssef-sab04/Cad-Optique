package cad.project.service;

import cad.project.playload.DevisDTO;
import cad.project.playload.DevisResponse;
import jakarta.validation.Valid;

public interface DevisService {
    DevisDTO addDevis(Long clientId, @Valid DevisDTO devisDTO);

    DevisDTO updateDevis(Long devisId, @Valid DevisDTO devisDTO);

    DevisDTO deleteDevis(Long devisId);

    DevisResponse getAllDevis(Integer pageNumber, Integer pageSize, String sortOrder);

    DevisDTO cancelDevis(Long devisId);

    DevisDTO confirmDevis(Long devisId);

}
