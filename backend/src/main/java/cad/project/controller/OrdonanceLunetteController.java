package cad.project.controller;

import cad.project.config.AppConstants;
import cad.project.playload.ClientDTO;
import cad.project.playload.ClientResponse;
import cad.project.playload.OrdonnanceLunetteDTO;
import cad.project.playload.ProduitDTO;
import cad.project.service.OrdonanceLunetteService;
import cad.project.service.OrdonnanceOcrParser;
import cad.project.service.VisionOcrService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class OrdonanceLunetteController {

    @Autowired
    OrdonanceLunetteService ordonanceLunetteService;

    @Autowired
    VisionOcrService visionOcrService;

    @Autowired
    OrdonnanceOcrParser ordonnanceOcrParser;


    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Ajouter une ordoance a un client")
    @PostMapping("admin/client/ordonance-lunette/{clientId}")
    public ResponseEntity<OrdonnanceLunetteDTO> ajouterOrL(@Valid @RequestBody OrdonnanceLunetteDTO ordonnanceLunetteDTO,
                                                           @PathVariable Long clientId){
        OrdonnanceLunetteDTO OrdonnanceLunetteDTOSaved =  ordonanceLunetteService.AddOrdLun(clientId , ordonnanceLunetteDTO);
        return new ResponseEntity<>(OrdonnanceLunetteDTOSaved , HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Modifier une ordoance")
    @PutMapping("admin/ordonnance-lunette/{ordonanceId}")
    public ResponseEntity<OrdonnanceLunetteDTO> modifierOrL(@Valid @RequestBody OrdonnanceLunetteDTO ordonnanceLunetteDTO,
                                                           @PathVariable Long ordonanceId){
        OrdonnanceLunetteDTO OrdonnanceLunetteDTOUpdated =  ordonanceLunetteService.UpdateOrdLun(ordonanceId , ordonnanceLunetteDTO);
        return new ResponseEntity<>(OrdonnanceLunetteDTOUpdated , HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Supprimer une ordoance")
    @DeleteMapping("admin/ordonnance-lunette/{ordonanceId}")
    public ResponseEntity<OrdonnanceLunetteDTO> supprimerOrL(@PathVariable Long ordonanceId){
        OrdonnanceLunetteDTO OrdonnanceLunetteDTODeleted =  ordonanceLunetteService.DeleteOrdLun(ordonanceId);
        return new ResponseEntity<>(OrdonnanceLunetteDTODeleted , HttpStatus.OK);
    }


    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Retourner les ordonance avec pagination")
    @GetMapping("/public/ordonances-lunette")
    public ResponseEntity<ClientResponse> getAllOrdL(

            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder,
            @RequestParam(name = "keyword", required = false) String keyword




    ){
        ClientResponse clientResponse = ordonanceLunetteService.getAlOrdonance(pageNumber, pageSize, sortOrder , keyword );
        return new ResponseEntity<>(clientResponse,HttpStatus.OK);    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Récupérer une ordonnance lunette par id")
    @GetMapping("/public/ordonance-lunette/{ordonanceId}")
    public ResponseEntity<OrdonnanceLunetteDTO> getOrdLById(@PathVariable Long ordonanceId){
        OrdonnanceLunetteDTO ordonnanceLunetteDTO = ordonanceLunetteService.getOrdLunById(ordonanceId);
        return new ResponseEntity<>(ordonnanceLunetteDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @PostMapping(value = "/admin/ordonance-lunette/scan/{clientId}", consumes = {"multipart/form-data"})
    public ResponseEntity<OrdonnanceLunetteDTO> addProduit (
            @PathVariable Long clientId ,
            @RequestPart("image") MultipartFile image ) throws IOException {
        OrdonnanceLunetteDTO ordonnanceLunetteDTO = ordonanceLunetteService.addScanOrd(clientId ,  image);
        return new ResponseEntity<>(ordonnanceLunetteDTO, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @PostMapping(value = "/admin/ordonance-lunette/scan-preview", consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, Object>> scanPreview(@RequestPart("image") MultipartFile image) throws IOException {
        String text = visionOcrService.extractText(image.getBytes());
        OrdonnanceLunetteDTO dto = ordonnanceOcrParser.parse(text);

        Map<String, Object> response = new HashMap<>();
        response.put("ocrText", text);
        response.put("dto", dto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @PostMapping(value = "/admin/client/ordonance-lunette-with-image/{clientId}", consumes = {"multipart/form-data"})
    public ResponseEntity<OrdonnanceLunetteDTO> ajouterOrLAvecImage(
            @RequestPart("ordonnance") OrdonnanceLunetteDTO ordonnanceLunetteDTO,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @PathVariable Long clientId) throws IOException {
        OrdonnanceLunetteDTO saved = ordonanceLunetteService.AddOrdLunAvecImage(clientId, ordonnanceLunetteDTO, image);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

}
