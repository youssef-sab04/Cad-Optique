package cad.project.controller;


import cad.project.config.AppConstants;
import cad.project.playload.PaimentDTO;
import cad.project.playload.PaimentResponse;
import cad.project.service.PaimentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PaiementController {

    @Autowired
    PaimentService paimentService;

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Ajouter un premiere paiment ")
    @PostMapping("/admin/paiment/{orderId}")
    public ResponseEntity<PaimentDTO> addFstPaiment(@Valid @RequestBody PaimentDTO paimentDTO,
                                                 @PathVariable Long orderId){
        PaimentDTO paimentDTOSaved = paimentService.AddFstPaiment(paimentDTO , orderId);
        return new ResponseEntity<>(paimentDTOSaved, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Ajouter un premiere paiment ")
    @PostMapping("/admin/paiments/{orderId}")
    public ResponseEntity<PaimentDTO> addOtherPaiment(@Valid @RequestBody PaimentDTO paimentDTO,
                                                    @PathVariable Long orderId){
        PaimentDTO paimentDTOSaved = paimentService.AddOtherPaiment(paimentDTO , orderId);
        return new ResponseEntity<>(paimentDTOSaved, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Get Paiments")
    @GetMapping("/public/paiments")
    public ResponseEntity<PaimentResponse> getAllPaiments(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ){
        PaimentResponse paimentResponse = paimentService.getAllPaiments(pageNumber, pageSize, sortOrder);
        return new ResponseEntity<>(paimentResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Get Paiments par commande")
    @GetMapping("/public/paiments/orders/{orderId}")
    public ResponseEntity<List<PaimentDTO>> getPaimentsByOrder(@PathVariable Long orderId){
        List<PaimentDTO> paimentDTOS = paimentService.getPaimentsByOrder(orderId);
        return new ResponseEntity<>(paimentDTOS, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Get Paiments par client")
    @GetMapping("/public/paiments/clients/{clientId}")
    public ResponseEntity<List<PaimentDTO>> getPaimentsByClient(@PathVariable Long clientId){
        List<PaimentDTO> paimentDTOS = paimentService.getPaimentsByClient(clientId);
        return new ResponseEntity<>(paimentDTOS, HttpStatus.OK);
    }
}
