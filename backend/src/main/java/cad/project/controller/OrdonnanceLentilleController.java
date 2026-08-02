package cad.project.controller;

import cad.project.config.AppConstants;
import cad.project.playload.ClientResponse;
import cad.project.playload.OrdonnanceLentilleDTO;
import cad.project.service.OrdonnanceLentilleService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrdonnanceLentilleController {

    @Autowired
    private OrdonnanceLentilleService ordonnanceLentilleService;

    @Operation(summary = "Ajouter une ordonnance lentille à un client")
    @PostMapping("/admin/client/ordonnance-lentille/{clientId}")
    public ResponseEntity<OrdonnanceLentilleDTO> ajouterOrLent(
            @Valid @RequestBody OrdonnanceLentilleDTO ordonnanceLentilleDTO,
            @PathVariable Long clientId) {

        OrdonnanceLentilleDTO saved = ordonnanceLentilleService.AddOrdLent(clientId, ordonnanceLentilleDTO);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @Operation(summary = "Modifier une ordonnance lentille")
    @PutMapping("/admin/ordonnance-lentille/{ordonnanceId}")
    public ResponseEntity<OrdonnanceLentilleDTO> modifierOrLent(
            @Valid @RequestBody OrdonnanceLentilleDTO ordonnanceLentilleDTO,
            @PathVariable Long ordonnanceId) {

        OrdonnanceLentilleDTO updated = ordonnanceLentilleService.UpdateOrdLent(ordonnanceId, ordonnanceLentilleDTO);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @Operation(summary = "Supprimer une ordonnance lentille")
    @DeleteMapping("/admin/ordonnance-lentille/{ordonnanceId}")
    public ResponseEntity<OrdonnanceLentilleDTO> supprimerOrLent(@PathVariable Long ordonnanceId) {

        OrdonnanceLentilleDTO deleted = ordonnanceLentilleService.DeleteOrdLent(ordonnanceId);
        return new ResponseEntity<>(deleted, HttpStatus.OK);
    }

    @Operation(summary = "Retourner les ordonnances lentille avec pagination")
    @GetMapping("/public/ordonnances-lentille")
    public ResponseEntity<ClientResponse> getAllOrdonnancesLentille(

            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder,
            @RequestParam(name = "keyword", required = false) String keyword

    ) {

        ClientResponse clientResponse = ordonnanceLentilleService.getAllOrdonnances(
                pageNumber, pageSize, sortOrder, keyword
        );

        return new ResponseEntity<>(clientResponse, HttpStatus.OK);
    }

    @Operation(summary = "Récupérer une ordonnance lentille par id")
    @GetMapping("/public/ordonance-lentille/{ordonanceId}")
    public ResponseEntity<OrdonnanceLentilleDTO> getOrdLeById(@PathVariable Long ordonanceId){
        OrdonnanceLentilleDTO ordonnanceLentilleDTO = ordonnanceLentilleService.getOrdLenById(ordonanceId);
        return new ResponseEntity<>(ordonnanceLentilleDTO, HttpStatus.OK);
    }
    


}