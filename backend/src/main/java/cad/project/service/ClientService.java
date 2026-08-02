package cad.project.service;

import cad.project.playload.ClientDTO;
import cad.project.playload.ClientResponse;
import jakarta.validation.Valid;

public interface ClientService {


    ClientDTO addClient(@Valid ClientDTO clientDTO);

    ClientResponse getAlClients(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword);

    ClientDTO updateClient(Long clientId, @Valid ClientDTO clientDTO);

    ClientDTO deleteClient(Long clientId);

    ClientResponse getAlClientsP_N(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder , String nom , String prenom);

    ClientDTO getClient(Long clientId);
}
