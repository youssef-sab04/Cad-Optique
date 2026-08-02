package cad.project.service;

import cad.project.playload.ClientResponse;
import cad.project.playload.ExamenDTO;
import jakarta.validation.Valid;

public interface ExamenService {

    ExamenDTO AddExamen(Long clientId, @Valid ExamenDTO examenDTO);

    ClientResponse getAllExamens(Integer pageNumber, Integer pageSize,  String sortOrder, String keyword);

    ExamenDTO UpdateExamen(Long examenId, @Valid ExamenDTO examenDTO);

    ExamenDTO DeleteExamen(Long examenId);

    ExamenDTO getExamen(Long examenId);
}