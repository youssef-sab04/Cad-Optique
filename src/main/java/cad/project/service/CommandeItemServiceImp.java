package cad.project.service;

import cad.project.exceptions.APIException;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.*;
import cad.project.playload.CommandeItemDTO;
import cad.project.playload.CommandeItemResponse;
import cad.project.playload.ProduitDTO;
import cad.project.repositries.CommandeItemRepositry;
import cad.project.repositries.CommandeRepositry;
import cad.project.repositries.OrdonanceLunetteRepositry;
import cad.project.repositries.OrdonnanceLentilleRepository;
import cad.project.repositries.ProduitRepositry;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandeItemServiceImp implements CommandeItemService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CommandeItemRepositry commandeItemRepositry;

    @Autowired
    private CommandeRepositry commandeRepositry;

    @Autowired
    private ProduitRepositry produitRepositry;

    @Autowired
    private OrdonanceLunetteRepositry ordonanceLunetteRepositry;

    @Autowired
    private OrdonnanceLentilleRepository ordonnanceLentilleRepository;

    @Override
    public CommandeItemDTO addCommandeItem(Long commandeId, Long produitId, Long ordonnanceLunetteId, Long ordonnanceLentilleId, CommandeItemDTO commandeItemDTO) {
        Commande commande = commandeRepositry.findById(commandeId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande", "commandeId", commandeId));

        Produit produit = produitRepositry.findById(produitId)
                .orElseThrow(() -> new ResourceNotFoundException("Produit", "produitId", produitId));

        CommandeItem commandeItem = modelMapper.map(commandeItemDTO, CommandeItem.class);
        System.out.println("qte" +commandeItem.getQuantity());
        System.out.println("price" +commandeItem.getPrice());

        commande.setTotalprice(commande.getTotalprice() + (commandeItem.getQuantity() * commandeItem.getPrice()));

        commandeItem.setCommande(commande);
        commandeItem.setProduit(produit);


        if (ordonnanceLunetteId != null) {
            OrdonnanceLunette ordonnanceLunette = ordonanceLunetteRepositry.findById(ordonnanceLunetteId)
                    .orElseThrow(() -> new ResourceNotFoundException("OrdonnanceLunette", "ordonnanceLunetteId", ordonnanceLunetteId));
            commandeItem.setOrdonnanceLunette(ordonnanceLunette);
        }

        if (ordonnanceLentilleId != null) {
            OrdonnanceLentille ordonnanceLentille = ordonnanceLentilleRepository.findById(ordonnanceLentilleId)
                    .orElseThrow(() -> new ResourceNotFoundException("OrdonnanceLentille", "ordonnanceLentilleId", ordonnanceLentilleId));
            commandeItem.setOrdonnanceLentille(ordonnanceLentille);
        }

        CommandeItem commandeItemSaved = commandeItemRepositry.save(commandeItem);

        return modelMapper.map(commandeItemSaved, CommandeItemDTO.class);
    }

    @Override
    public CommandeItemDTO updateCommandeItem(Long commandeItemId, CommandeItemDTO commandeItemDTO) {
        CommandeItem commandeItemFromDb = commandeItemRepositry.findById(commandeItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CommandeItem", "commandeItemId", commandeItemId));


        if(commandeItemDTO.getQuantity() == null    ){
            throw new APIException("Quantite injuste" + commandeItemDTO.getQuantity());
        }


        if(commandeItemFromDb.getQuantity() + commandeItemDTO.getQuantity() == 0 ){
            throw new APIException("Impossible de decrementer");
        }

        commandeItemFromDb.setQuantity(commandeItemFromDb.getQuantity() + commandeItemDTO.getQuantity());
        //commandeItemFromDb.setPrice(commandeItemDTO.getPrice());

        CommandeItem commandeItemSaved = commandeItemRepositry.save(commandeItemFromDb);
        UpdateTotlPrice(commandeItemFromDb.getCommande().getId());


        return modelMapper.map(commandeItemSaved, CommandeItemDTO.class);
    }
    @Override
    @Transactional
    public CommandeItemDTO deleteCommandeItem(Long commandeItemId) {
        CommandeItem deletedCommandeItem = commandeItemRepositry.findById(commandeItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CommandeItem", "commandeItemId", commandeItemId));

        Long commandeId = deletedCommandeItem.getCommande().getId();

        commandeItemRepositry.delete(deletedCommandeItem);
        commandeItemRepositry.flush();

        UpdateTotlPrice(commandeId);

        return modelMapper.map(deletedCommandeItem, CommandeItemDTO.class);
    }

    @Override
    public CommandeItemResponse getAllCommandeItems(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by("createdAt").ascending()
                : Sort.by("createdAt").descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<CommandeItem> commandeItemPage = commandeItemRepositry.findAll(pageDetails);

        List<CommandeItem> commandeItems = commandeItemPage.getContent();
        if (commandeItems.isEmpty())
            throw new APIException("No CommandeItem Created till now");

        List<CommandeItemDTO> commandeItemDTOS = commandeItems.stream()
                .map(commandeItem -> modelMapper.map(commandeItem, CommandeItemDTO.class))
                .toList();

        CommandeItemResponse commandeItemResponse = new CommandeItemResponse();
        commandeItemResponse.setContent(commandeItemDTOS);
        commandeItemResponse.setPageNumber(commandeItemPage.getNumber());
        commandeItemResponse.setPageSize(commandeItemPage.getSize());
        commandeItemResponse.setTotalElements(commandeItemPage.getTotalElements());
        commandeItemResponse.setTotalPages(commandeItemPage.getTotalPages());
        commandeItemResponse.setLastPage(commandeItemPage.isLast());
        return commandeItemResponse;
    }

    @Override
    public List<CommandeItemDTO> getCommandeItemsByCommandeId(Long commandeId) {
        commandeRepositry.findById(commandeId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande", "commandeId", commandeId));

        List<CommandeItem> commandeItems = commandeItemRepositry.findByCommandeId(commandeId);

        return commandeItems.stream()
                .map(commandeItem -> {
                CommandeItemDTO dto =    modelMapper.map(commandeItem, CommandeItemDTO.class);
                dto.setProduitDTO(modelMapper.map(commandeItem.getProduit() , ProduitDTO.class));

                return  dto;
                })
                .toList();
    }

    public void UpdateTotlPrice(Long commandeId){
        Commande commande = commandeRepositry.findById(commandeId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande", "CommandeId", commandeId));
        List<CommandeItem> commandeItems = commande.getCommandeItems();
        commande.setTotalprice(0.00);

        commandeItems.forEach(commandeItem -> {
            double price = commandeItem.getPrice() != null ? commandeItem.getPrice() : 0.0;
            commande.setTotalprice(commande.getTotalprice() + (commandeItem.getQuantity() * price));
        });

        commandeRepositry.save(commande);
    }
}