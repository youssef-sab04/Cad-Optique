package cad.project.service;

import cad.project.playload.ClientResponse;
import cad.project.playload.OrdonnanceLentilleDTO;
import jakarta.validation.Valid;

public interface OrdonnanceLentilleService {

    OrdonnanceLentilleDTO AddOrdLent(Long clientId, @Valid OrdonnanceLentilleDTO ordonnanceLentilleDTO);

    ClientResponse getAllOrdonnances(Integer pageNumber, Integer pageSize,  String sortOrder, String keyword);

    OrdonnanceLentilleDTO UpdateOrdLent(Long ordonnanceId, @Valid OrdonnanceLentilleDTO ordonnanceLentilleDTO);

    OrdonnanceLentilleDTO DeleteOrdLent(Long ordonnanceId);

    OrdonnanceLentilleDTO getOrdLenById(Long ordonanceId);
}