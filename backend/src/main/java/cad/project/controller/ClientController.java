package cad.project.controller;

import cad.project.config.AppConstants;
import cad.project.playload.ClientDTO;
import cad.project.playload.ClientResponse;
import cad.project.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    ClientService clientService;

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Ajouter un client")
    @PostMapping("/admin/client")
    public ResponseEntity<ClientDTO> addClient(@Valid @RequestBody ClientDTO clientDTO){
        ClientDTO savedClientDTO = clientService.addClient(clientDTO);
        return new ResponseEntity<>(savedClientDTO, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Retourner les clients avec pagination")
    @GetMapping("/public/clients")
    public ResponseEntity<ClientResponse> getAllClients(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY_NOUN,  required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ){
        ClientResponse clientResponse = clientService.getAlClients(pageNumber, pageSize, sortBy, sortOrder, keyword);
        return new ResponseEntity<>(clientResponse,HttpStatus.OK);
    }


    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Retourner les clients avec pagination (Nom & prenom)")
    @GetMapping("/public/Clients/P-N")
    public ResponseEntity<ClientResponse> getAllClients(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY_NOUN, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder,
            @RequestParam(name = "nom" , required = true ) String nom,
            @RequestParam(name = "prenom" , required = true) String prenom

    ){
        ClientResponse clientResponse = clientService.getAlClientsP_N(pageNumber, pageSize, sortBy, sortOrder , nom , prenom);
        return new ResponseEntity<>(clientResponse,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Modifier un client")
    @PutMapping("/admin/clients/{clientId}")
    public ResponseEntity<ClientDTO> updateClient(@Valid @RequestBody ClientDTO clientDTO,
                                                    @PathVariable Long clientId){
        ClientDTO updatedClientDTO = clientService.updateClient(clientId, clientDTO);
        return new ResponseEntity<>(updatedClientDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Afficher un client id")
    @GetMapping("/public/clients/{clientId}")
    public ResponseEntity<ClientDTO> getClient(@PathVariable Long clientId){
        ClientDTO updatedClientDTO = clientService.getClient(clientId);
        return new ResponseEntity<>(updatedClientDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Supprimer un client")
    @DeleteMapping("/admin/clients/{clientId}")
    public ResponseEntity<ClientDTO> deleteClient(@PathVariable Long clientId){
        ClientDTO deletedClient = clientService.deleteClient(clientId);
        return new ResponseEntity<>(deletedClient, HttpStatus.OK);
    }


    
    


}
