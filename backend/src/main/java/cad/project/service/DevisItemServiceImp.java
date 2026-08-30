package cad.project.service;

import cad.project.exceptions.APIException;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.Devis;
import cad.project.model.DevisItems;
import cad.project.model.Produit;
import cad.project.model.SalesOrderItems;
import cad.project.playload.DevisItemDTO;
import cad.project.playload.DevisItemResponse;
import cad.project.playload.ProduitDTO;
import cad.project.repositries.DecvisItemsRepositry;
import cad.project.repositries.DevisRepositry;
import cad.project.repositries.ProduitRepositry;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DevisItemServiceImp implements DevisItemService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DecvisItemsRepositry devisItemsRepositry;

    @Autowired
    private DevisRepositry devisRepositry;

    @Autowired
    private ProduitRepositry produitRepositry;

    @Transactional
    @Override
    public DevisItemDTO addDevisItem(Long devisId, Long produitId, DevisItemDTO devisItemDTO) {
        Devis devis = devisRepositry.findById(devisId)
                .orElseThrow(() -> new ResourceNotFoundException("Devis", "devisId", devisId));

        Produit produit = produitRepositry.findById(produitId)
                .orElseThrow(() -> new ResourceNotFoundException("Produit", "produitId", produitId));

        DevisItems devisItem = modelMapper.map(devisItemDTO, DevisItems.class);
        if(devisItemDTO.getQuantity() == null || devisItemDTO.getQuantity() < 1){
            devisItem.setQuantity(1);
        }

        List<DevisItems> devisItemsList = devis.getDevisItemsList();

        devisItemsList.forEach(item -> {
            if (item.getProduit().getId().equals(produitId)) {
                throw new APIException("Produit deja existant");
            }
        });

        devisItem.setDevis(devis);
        devisItem.setProduit(produit);
        devisItem.setTva(produit.getTva());
        devisItem.setPrixHT(produit.getPrixHT());
        if(produit.getDiscount() != null && produit.getDiscount() > 0){
            devisItem.setDiscount(produit.getDiscount());
        }
        devisItem.setPrice(produit.getPrice());
        devis.setTotalprice(devis.getTotalprice() + (devisItem.getPrice() * devisItem.getQuantity()));
        devisRepositry.save(devis);

        DevisItems devisItemSaved = devisItemsRepositry.save(devisItem);

        return modelMapper.map(devisItemSaved, DevisItemDTO.class);
    }

    @Transactional
    @Override
    public DevisItemDTO updateDevisItemQuantity(Long devisItemId, DevisItemDTO devisItemDTO) {
        DevisItems devisItemFromDb = devisItemsRepositry.findById(devisItemId)
                .orElseThrow(() -> new ResourceNotFoundException("DevisItem", "devisItemId", devisItemId));

        if(devisItemDTO.getQuantity() == null || devisItemDTO.getQuantity() < 1){
            throw new APIException("Quantite injuste");
        }

        else if(devisItemFromDb.getQuantity() + devisItemDTO.getQuantity() > devisItemFromDb.getProduit().getQuantity() ){
            throw new APIException("Stock insuffisant");
        }

        devisItemFromDb.setQuantity(devisItemDTO.getQuantity());

        DevisItems devisItemSaved = devisItemsRepositry.save(devisItemFromDb);
        UpdateTotlPrice(devisItemFromDb.getDevis().getId());

        return modelMapper.map(devisItemSaved, DevisItemDTO.class);
    }

    @Transactional
    @Override
    public DevisItemDTO deleteDevisItem(Long devisItemId) {
        DevisItems deletedDevisItem = devisItemsRepositry.findById(devisItemId)
                .orElseThrow(() -> new ResourceNotFoundException("DevisItem", "devisItemId", devisItemId));

        Long devisId = deletedDevisItem.getDevis().getId();

        devisItemsRepositry.delete(deletedDevisItem);
        devisItemsRepositry.flush();

        UpdateTotlPrice(devisId);

        return modelMapper.map(deletedDevisItem, DevisItemDTO.class);
    }

    @Override
    public DevisItemResponse getAllDevisItems(Integer pageNumber, Integer pageSize, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by("createdAt").ascending()
                : Sort.by("createdAt").descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<DevisItems> devisItemPage = devisItemsRepositry.findAll(pageDetails);

        List<DevisItems> devisItems = devisItemPage.getContent();
        if (devisItems.isEmpty())
            throw new APIException("No DevisItem Created till now");

        List<DevisItemDTO> devisItemDTOS = devisItems.stream()
                .map(item -> modelMapper.map(item, DevisItemDTO.class))
                .toList();

        DevisItemResponse response = new DevisItemResponse();
        response.setContent(devisItemDTOS);
        response.setPageNumber(devisItemPage.getNumber());
        response.setPageSize(devisItemPage.getSize());
        response.setTotalElements(devisItemPage.getTotalElements());
        response.setTotalPages(devisItemPage.getTotalPages());
        response.setLastPage(devisItemPage.isLast());
        return response;
    }

    @Override
    public List<DevisItemDTO> getDevisItemsByDevisId(Long devisId) {
        devisRepositry.findById(devisId)
                .orElseThrow(() -> new ResourceNotFoundException("Devis", "devisId", devisId));

        List<DevisItems> devisItems = devisItemsRepositry.findByDevisId(devisId);

        return devisItems.stream()
                .map(item -> {
                    DevisItemDTO dto = modelMapper.map(item, DevisItemDTO.class);
                    dto.setProduitDTO(modelMapper.map(item.getProduit(), ProduitDTO.class));
                    return dto;
                })
                .toList();
    }

    public void UpdateTotlPrice(Long devisId){
        Devis devis = devisRepositry.findById(devisId)
                .orElseThrow(() -> new ResourceNotFoundException("Devis", "devisId", devisId));
        List<DevisItems> devisItems = devis.getDevisItemsList();
        devis.setTotalprice(0.00);

        devisItems.forEach(devisItem -> {
            devis.setTotalprice(devis.getTotalprice() + (devisItem.getQuantity() * devisItem.getPrice()));
        });

        devisRepositry.save(devis);
    }
}