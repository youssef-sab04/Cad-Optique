package cad.project.service;


import cad.project.exceptions.APIException;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.Client;
import cad.project.playload.ClientDTO;
import cad.project.playload.ClientResponse;
import cad.project.repositries.ClientRepositry;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImp  implements  ClientService{

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ClientRepositry clientRepositry;

    @Override
    public ClientResponse getAlClients(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Specification<Client> spec = (root, query, cb) -> cb.conjunction();
        if (keyword != null && !keyword.isEmpty()) {
            String kw = keyword.trim().toLowerCase().replaceAll("\\s+", " ");

            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(cb.concat(cb.concat(root.get("nom"), " "), root.get("prenom"))), "%" + kw + "%"),
                    cb.like(cb.lower(cb.concat(cb.concat(root.get("prenom"), " "), root.get("nom"))), "%" + kw + "%")
            ));
        }

        Page<Client> pageClients = clientRepositry.findAll(spec, pageDetails);

        List<Client> clients = pageClients.getContent();

        List<ClientDTO> ClientDTOS = clients.stream()
                .map(Client -> {
                    ClientDTO ClientDTO = modelMapper.map(Client, ClientDTO.class);
                    return ClientDTO;
                })
                .toList();

        if(clients.isEmpty()){
            throw new APIException("Aucun client !");
        }
        ClientResponse clientResponse = new ClientResponse();
        clientResponse.setContent(ClientDTOS);
        clientResponse.setPageNumber(pageClients.getNumber());
        clientResponse.setPageSize(pageClients.getSize());
        clientResponse.setTotalElements(pageClients.getTotalElements());
        clientResponse.setTotalPages(pageClients.getTotalPages());
        clientResponse.setLastPage(pageClients.isLast());
        return  clientResponse;


    }

    @Override
    public ClientDTO updateClient(Long clientId, ClientDTO clientDTO) {
        Client ClientFromDb = clientRepositry.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "ClientId", clientId));

        Client Client = modelMapper.map(clientDTO, Client.class);

        ClientFromDb.setNom(Client.getNom());
        ClientFromDb.setPrenom(Client.getPrenom());
        ClientFromDb.setPhoneNumber(Client.getPhoneNumber());
        ClientFromDb.setAdresse(Client.getAdresse());
        ClientFromDb.setEmail(Client.getEmail());
        ClientFromDb.setMutuelle(Client.getMutuelle());
        ClientFromDb.setDateNaissance(Client.getDateNaissance());




        Client savedClient = clientRepositry.save(ClientFromDb);
        
        return modelMapper.map(savedClient , ClientDTO.class);
    }

    @Override
    public ClientDTO deleteClient(Long clientId) {
        Client client = clientRepositry.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "clientId", clientId));

        clientRepositry.delete(client);
        return modelMapper.map(client, ClientDTO.class);

    }

    @Override
    public ClientResponse getAlClientsP_N(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder , String nom , String prenom) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Specification<Client> spec = (root, query, cb) -> cb.conjunction();
        if (nom != null && !nom.isEmpty() && prenom != null && !prenom.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("nom")), nom.toLowerCase() ),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("prenom")), prenom.toLowerCase())
                    )
            );
        }


        Page<Client> pageClients = clientRepositry.findAll(spec, pageDetails);

        List<Client> clients = pageClients.getContent();

        List<ClientDTO> ClientDTOS = clients.stream()
                .map(Client -> {
                    ClientDTO ClientDTO = modelMapper.map(Client, ClientDTO.class);
                    return ClientDTO;
                })
                .toList();

        if(clients.isEmpty()){
            throw new APIException("Aucun client !");
        }
        ClientResponse clientResponse = new ClientResponse();
        clientResponse.setContent(ClientDTOS);
        clientResponse.setPageNumber(pageClients.getNumber());
        clientResponse.setPageSize(pageClients.getSize());
        clientResponse.setTotalElements(pageClients.getTotalElements());
        clientResponse.setTotalPages(pageClients.getTotalPages());
        clientResponse.setLastPage(pageClients.isLast());
        return  clientResponse;
    }

    @Override
    public ClientDTO getClient(Long clientId) {
        Client client = clientRepositry.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "clientId", clientId));

        return modelMapper.map(client, ClientDTO.class);
    }

    @Override
    public ClientDTO addClient(ClientDTO clientDTO) {

        Client client = modelMapper.map(clientDTO , Client.class);
        Client clientSaved = clientRepositry.save(client);

        return modelMapper.map(clientSaved , ClientDTO.class);
    }
}
