package cad.project.service;

import cad.project.exceptions.APIException;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.Client;
import cad.project.model.Devis;
import cad.project.model.SalesOrder;
import cad.project.model.DevisItems;
import cad.project.model.SalesOrderItems;
import cad.project.model.Produit;
import cad.project.model.Notification;
import cad.project.model.Mouvement_Stock;

import cad.project.playload.ClientDTO;
import cad.project.playload.DevisDTO;
import cad.project.playload.DevisResponse;
import cad.project.repositries.*;
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
public class DevisServiceImp implements DevisService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DevisRepositry devisRepositry;

    @Autowired
    private ClientRepositry clientRepositry;

    @Autowired
    private ProduitRepositry produitRepositry;

    @Autowired
    private SaleOrderRepositry saleOrderRepositry;

    @Autowired
    private SaleOrderItemsRepositry saleOrderItemsRepositry;

    @Autowired
    private NotificationRepositry notificationRepositry;

    @Autowired
    private Mouvement_StockRepositry mouvementStockRepositry;

    @Autowired
    private DecvisItemsRepositry decvisItemsRepositry;





    @Override
    public DevisDTO addDevis(Long clientId, DevisDTO devisDTO) {
        Client client = clientRepositry.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "clientId", clientId));

        Devis devis = modelMapper.map(devisDTO, Devis.class);
        devis.setTotalprice(0.0);
        devis.setClient(client);

        Devis devisSaved = devisRepositry.save(devis);

        return modelMapper.map(devisSaved, DevisDTO.class);
    }

    @Override
    public DevisDTO updateDevis(Long devisId, DevisDTO devisDTO) {
        Devis devisFromDb = devisRepositry.findById(devisId)
                .orElseThrow(() -> new ResourceNotFoundException("Devis", "devisId", devisId));

        devisFromDb.setStatus(devisDTO.getStatus());
        devisFromDb.setDescription(devisDTO.getDescription());
        devisFromDb.setTotalprice(devisDTO.getTotalprice());

        Devis devisSaved = devisRepositry.save(devisFromDb);

        return modelMapper.map(devisSaved, DevisDTO.class);
    }

    @Override
    public DevisDTO deleteDevis(Long devisId) {
        Devis deletedDevis = devisRepositry.findById(devisId)
                .orElseThrow(() -> new ResourceNotFoundException("Devis", "devisId", devisId));

        DevisDTO devisDTO = modelMapper.map(deletedDevis, DevisDTO.class);

        if (deletedDevis.getDevisItemsList() != null) {
            decvisItemsRepositry.deleteAll(deletedDevis.getDevisItemsList());
        }

        if (deletedDevis.getSalesOrder() != null) {
            deletedDevis.setSalesOrder(null);
            devisRepositry.save(deletedDevis);
        }

        devisRepositry.delete(deletedDevis);

        return devisDTO;
    }
    @Override
    public DevisResponse getAllDevis(Integer pageNumber, Integer pageSize, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by("createdAt").ascending()
                : Sort.by("createdAt").descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Devis> devisPage = devisRepositry.findAll(pageDetails);

        List<Devis> devisList = devisPage.getContent();
        if (devisList.isEmpty())
            throw new APIException("No Devis Created till now");

        List<DevisDTO> devisDTOS = devisList.stream()
                .map(devis -> {
                    DevisDTO dto =   modelMapper.map(devis, DevisDTO.class);
                    dto.setClientDTO(modelMapper.map(devis.getClient() , ClientDTO.class));
                    return dto;

                })
                .toList();

        DevisResponse devisResponse = new DevisResponse();
        devisResponse.setContent(devisDTOS);
        devisResponse.setPageNumber(devisPage.getNumber());
        devisResponse.setPageSize(devisPage.getSize());
        devisResponse.setTotalElements(devisPage.getTotalElements());
        devisResponse.setTotalPages(devisPage.getTotalPages());
        devisResponse.setLastPage(devisPage.isLast());
        return devisResponse;
    }

    @Override
    public DevisDTO cancelDevis(Long devisId) {
        Devis devis = devisRepositry.findById(devisId)
                .orElseThrow(() -> new ResourceNotFoundException("Devis", "devisId", devisId));

        if ("Valide".equals(devis.getStatus())) {
            throw new APIException("Un devis valide ne peut pas etre annule");
        }

        devis.setStatus("Annulee");
        Devis devisSaved = devisRepositry.save(devis);
        return modelMapper.map(devisSaved, DevisDTO.class);
    }

    @Transactional
    @Override
    public DevisDTO confirmDevis(Long devisId) {
        Devis devis = devisRepositry.findById(devisId)
                .orElseThrow(() -> new ResourceNotFoundException("Devis", "devisId", devisId));

        if ("Valide".equals(devis.getStatus()) || "Annulee".equals(devis.getStatus())) {
            throw new APIException("Ce devis ne peut plus etre confirme");
        }

        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setClient(devis.getClient());
        salesOrder.setDescription(devis.getDescription());
        salesOrder.setStatus("Valide");
        salesOrder.setTotalprice(0.0);
        SalesOrder salesOrderSaved = saleOrderRepositry.save(salesOrder);

        double total = 0.0;

        for (DevisItems devisItem : devis.getDevisItemsList()) {
            Produit produit = devisItem.getProduit();

            if (produit.getQuantity() < devisItem.getQuantity()) {
                throw new APIException("Stock insuffisant pour le produit : " + produit.getNom());
            }
        }

        for (DevisItems devisItem : devis.getDevisItemsList()) {
            Produit produit = devisItem.getProduit();



            SalesOrderItems item = new SalesOrderItems();
            item.setSalesOrder(salesOrderSaved);
            item.setProduit(produit);
            item.setQuantity(devisItem.getQuantity());
            item.setPrixHT(devisItem.getPrixHT());
            item.setTva(devisItem.getTva());
            item.setDiscount(devisItem.getDiscount());
            item.setPrice(devisItem.getPrice());
            saleOrderItemsRepositry.save(item);

            produit.setQuantity(produit.getQuantity() - devisItem.getQuantity());
            produitRepositry.save(produit);

            if (produit.getSeuilMin() != null && produit.getQuantity() <= produit.getSeuilMin()) {
                Notification notification = new Notification();
                notification.setProduit(produit);
                notification.setType(produit.getQuantity() == 0 ? "stock_epuise" : "stock_bas");
                notification.setMessage("Le produit " + produit.getNom() + " a atteint une quantite de " + produit.getQuantity());
                notification.setDescription("Seuil minimum : " + produit.getSeuilMin());
                notification.set_read(false);
                notificationRepositry.save(notification);
            }

            Mouvement_Stock mouvementStock = new Mouvement_Stock();
            mouvementStock.setSalesOrderItems(item);
            mouvementStock.setProduit(produit);
            mouvementStock.setQuantity(devisItem.getQuantity());
            mouvementStock.setType("SORTIE");
            mouvementStockRepositry.save(mouvementStock);

            total += devisItem.getPrice() * devisItem.getQuantity();
        }

        salesOrderSaved.setTotalprice(total);
        saleOrderRepositry.save(salesOrderSaved);

        devis.setSalesOrder(salesOrderSaved);
        devis.setStatus("Valide");
        devis.setTotalprice(total);
        Devis devisSaved = devisRepositry.save(devis);

        return modelMapper.map(devisSaved, DevisDTO.class);
    }


}
