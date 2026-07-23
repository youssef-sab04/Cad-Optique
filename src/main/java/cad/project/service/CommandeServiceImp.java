package cad.project.service;

import cad.project.exceptions.APIException;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.*;
import cad.project.playload.CommandeDTO;
import cad.project.playload.CommandeResponse;
import cad.project.playload.FournisseurDTO;
import cad.project.repositries.CommandeRepositry;
import cad.project.repositries.FournisseurRepositry;
import cad.project.repositries.Mouvement_StockRepositry;
import cad.project.repositries.ProduitRepositry;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommandeServiceImp implements CommandeService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CommandeRepositry commandeRepositry;

    @Autowired
    private FournisseurRepositry fournisseurRepositry;

    @Autowired
    private ProduitRepositry produitRepositry;

    @Autowired
    private Mouvement_StockRepositry mouvementStockRepositry;

    @Override
    public CommandeDTO addCommande(Long fournisseurId, CommandeDTO commandeDTO) {
        Fournisseur fournisseur = fournisseurRepositry.findById(fournisseurId)
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur", "fournisseurId", fournisseurId));

        Commande commande = modelMapper.map(commandeDTO, Commande.class);
        commande.setFournisseur(fournisseur);
        commande.setStatus("EN_COURS");
        commande.setTotalprice(0.00);

        Commande commandeSaved = commandeRepositry.save(commande);

        return modelMapper.map(commandeSaved, CommandeDTO.class);
    }

    @Override
    public CommandeDTO updateCommande(Long commandeId, CommandeDTO commandeDTO) {
        Commande commandeFromDb = commandeRepositry.findById(commandeId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande", "commandeId", commandeId));

        commandeFromDb.setStatus(commandeDTO.getStatus());
        commandeFromDb.setDescription(commandeDTO.getDescription());

        Commande commandeSaved = commandeRepositry.save(commandeFromDb);

        return modelMapper.map(commandeSaved, CommandeDTO.class);
    }

    @Override
    public CommandeDTO deleteCommande(Long commandeId) {
        Commande deletedCommande = commandeRepositry.findById(commandeId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande", "commandeId", commandeId));

        commandeRepositry.delete(deletedCommande);

        return modelMapper.map(deletedCommande, CommandeDTO.class);
    }

    @Override
    public CommandeResponse getAllCommandes(Integer pageNumber, Integer pageSize, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by("createdAt").ascending()
                : Sort.by("createdAt").descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Commande> commandePage = commandeRepositry.findAll(pageDetails);

        List<Commande> commandes = commandePage.getContent();
        if (commandes.isEmpty())
            throw new APIException("No Commande Created till now");

        List<CommandeDTO> commandeDTOS = commandes.stream()
                .map(commande -> {
                 CommandeDTO dto =   modelMapper.map(commande, CommandeDTO.class);
                 dto.setFournisseurDTO(modelMapper.map(commande.getFournisseur() , FournisseurDTO.class));
                 return dto;
                })
                .toList();

        CommandeResponse commandeResponse = new CommandeResponse();
        commandeResponse.setContent(commandeDTOS);
        commandeResponse.setPageNumber(commandePage.getNumber());
        commandeResponse.setPageSize(commandePage.getSize());
        commandeResponse.setTotalElements(commandePage.getTotalElements());
        commandeResponse.setTotalPages(commandePage.getTotalPages());
        commandeResponse.setLastPage(commandePage.isLast());
        return commandeResponse;
    }

    @Override
    public CommandeDTO ValiderCommande(Long commandeId) {

        Commande commande = commandeRepositry.findById(commandeId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande", "commandeId", commandeId));

        if ("LIVREE".equals(commande.getStatus())) {
            throw new APIException("Commande déjà validée");
        }

        commande.setStatus("LIVREE");

        List<CommandeItem> commandeItems = commande.getCommandeItems();

        commandeItems.forEach(commandeItem -> {
            Mouvement_Stock mouvementStock = new Mouvement_Stock();
            mouvementStock.setCommandeItem(commandeItem);
            mouvementStock.setProduit(commandeItem.getProduit());
            mouvementStock.setQuantity(commandeItem.getQuantity());
            mouvementStock.setType("ENTREE");
            mouvementStock.setPrix_Unit(commandeItem.getPrice());
            mouvementStock.setPrix_total(commandeItem.getQuantity() * commandeItem.getPrice() );
            mouvementStockRepositry.save(mouvementStock);

            Produit produit = commandeItem.getProduit();
            produit.setPrixAchat(commandeItem.getPrice());
            produit.setQuantity(produit.getQuantity() + commandeItem.getQuantity());
            produitRepositry.save(produit);
        });

        commandeRepositry.save(commande);

        return modelMapper.map(commande, CommandeDTO.class);
    }
}