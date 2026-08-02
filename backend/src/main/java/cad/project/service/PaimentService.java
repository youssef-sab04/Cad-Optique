package cad.project.service;

import cad.project.playload.PaimentDTO;
import cad.project.playload.PaimentResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface PaimentService {
    PaimentDTO AddFstPaiment(@Valid PaimentDTO paimentDTO, Long orderId);

    PaimentDTO AddOtherPaiment(@Valid PaimentDTO paimentDTO, Long orderId);
    PaimentResponse getAllPaiments(Integer pageNumber, Integer pageSize, String sortOrder);

    List<PaimentDTO> getPaimentsByOrder(Long orderId);

    List<PaimentDTO> getPaimentsByClient(Long clientId);
}
