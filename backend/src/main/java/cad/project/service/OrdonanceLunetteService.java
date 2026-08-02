package cad.project.service;

import cad.project.playload.ClientResponse;
import cad.project.playload.OrdonnanceLunetteDTO;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

public interface OrdonanceLunetteService {


    OrdonnanceLunetteDTO AddOrdLun(Long clientId, @Valid OrdonnanceLunetteDTO ordonnanceLunetteDTO);

    ClientResponse getAlOrdonance(Integer pageNumber, Integer pageSize, String sortOrder , String keyword);

    OrdonnanceLunetteDTO UpdateOrdLun(Long ordonanceId, @Valid OrdonnanceLunetteDTO ordonnanceLunetteDTO);

    OrdonnanceLunetteDTO DeleteOrdLun(Long ordonanceId);

    OrdonnanceLunetteDTO getOrdLunById(Long ordonanceId);

    OrdonnanceLunetteDTO addScanOrd(Long clientId , MultipartFile image) throws IOException;

    OrdonnanceLunetteDTO AddOrdLunAvecImage(Long clientId, OrdonnanceLunetteDTO dto, MultipartFile image) throws IOException;
}
