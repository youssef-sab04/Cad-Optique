package cad.project.controller;

import cad.project.config.AppConstants;
import cad.project.playload.ClientResponse;
import cad.project.playload.ExamenDTO;
import cad.project.playload.ProduitDTO;
import cad.project.service.ExamenService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ExamenController {

    @Autowired
    private ExamenService examenService;

    @Operation(summary = "Ajouter un examen à un client")
    @PostMapping("/admin/client/examen/{clientId}")
    public ResponseEntity<ExamenDTO> ajouterExamen(
            @Valid @RequestBody ExamenDTO examenDTO,
            @PathVariable Long clientId) {

        ExamenDTO saved = examenService.AddExamen(clientId, examenDTO);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @Operation(summary = "Get examen par id")
    @GetMapping("/admin/client/examen/{examenId}")
    public ResponseEntity<ExamenDTO> getExamen(@PathVariable Long examenId) {

        ExamenDTO saved = examenService.getExamen(examenId);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @Operation(summary = "Modifier un examen")
    @PutMapping("/admin/examen/{examenId}")
    public ResponseEntity<ExamenDTO> modifierExamen(
            @Valid @RequestBody ExamenDTO examenDTO,
            @PathVariable Long examenId) {

        ExamenDTO updated = examenService.UpdateExamen(examenId, examenDTO);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @Operation(summary = "Supprimer un examen")
    @DeleteMapping("/admin/examen/{examenId}")
    public ResponseEntity<ExamenDTO> supprimerExamen(@PathVariable Long examenId) {

        ExamenDTO deleted = examenService.DeleteExamen(examenId);
        return new ResponseEntity<>(deleted, HttpStatus.OK);
    }

    @Operation(summary = "Retourner les examens avec pagination")
    @GetMapping("/public/examens")
    public ResponseEntity<ClientResponse> getAllExamens(

            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder,
            @RequestParam(name = "keyword", required = false) String keyword

            ) {

        ClientResponse clientResponse = examenService.getAllExamens(
                pageNumber, pageSize,  sortOrder, keyword
        );

        return new ResponseEntity<>(clientResponse, HttpStatus.OK);
    }


}