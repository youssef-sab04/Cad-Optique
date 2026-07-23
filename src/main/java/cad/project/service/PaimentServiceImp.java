package cad.project.service;

import cad.project.exceptions.APIException;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.Client;
import cad.project.model.Paiment;
import cad.project.model.SalesOrder;
import org.springframework.data.domain.Page;
import cad.project.playload.PaimentDTO;
import cad.project.playload.PaimentResponse;
import cad.project.repositries.ClientRepositry;
import cad.project.repositries.PaimentRepositry;
import cad.project.repositries.SaleOrderRepositry;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaimentServiceImp implements  PaimentService{
    
    @Autowired 
    private ModelMapper modelMapper;
    
    @Autowired
    private PaimentRepositry paimentRepositry;
    
    @Autowired
    private SaleOrderRepositry saleOrderRepositry;

    @Autowired
    private ClientRepositry clientRepositry;
    @Override
    public PaimentDTO AddFstPaiment(PaimentDTO paimentDTO, Long orderId) {
        SalesOrder salesOrder = saleOrderRepositry.findById(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("Order" , "OrderId" , orderId));

        Paiment paiment = modelMapper.map(paimentDTO , Paiment.class);
        paiment.setClient(salesOrder.getClient());
        paiment.setSalesOrder(salesOrder);

        if(paiment.getMontant_Paye() == salesOrder.getTotalprice()){
            salesOrder.setStatus("Paiment totale");
            salesOrder.setMontantReste(0.00);

        }
        else {

            if(paiment.getMontant_Paye() < 0){
                throw new APIException("Montant Invalide");
            }
            else if(paiment.getMontant_Paye() > salesOrder.getTotalprice()){
                throw new APIException("Montant sup a montant total");
            }

            salesOrder.setStatus("Paiment Partiell");
            salesOrder.setMontantReste(salesOrder.getTotalprice() - paiment.getMontant_Paye()  );


        }

        paimentRepositry.save(paiment);
        saleOrderRepositry.save(salesOrder);

        return modelMapper.map(paiment , PaimentDTO.class);
    }

    @Override
    public PaimentDTO AddOtherPaiment(PaimentDTO paimentDTO, Long orderId) {

        SalesOrder salesOrder = saleOrderRepositry.findById(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("Order" , "OrderId" , orderId));

        Paiment paiment = modelMapper.map(paimentDTO , Paiment.class);
        paiment.setClient(salesOrder.getClient());
        paiment.setSalesOrder(salesOrder);

        if(salesOrder.getMontantReste() == 0 && salesOrder.getStatus().equals("Paiment totale")){
            throw new APIException("Order deja paye");
        }
        else if(paiment.getMontant_Paye() < 0){
            throw new APIException("Montant Invalide");
        }
        else if(paiment.getMontant_Paye() > salesOrder.getMontantReste()  ){
            throw new APIException("Erreur");

        }

        else{
            if(paiment.getMontant_Paye() == salesOrder.getMontantReste() ){
                salesOrder.setStatus("Paiment totale");
                salesOrder.setMontantReste(0.00);
            }

            else {
                salesOrder.setStatus("Paiment Partiell");
                salesOrder.setMontantReste(salesOrder.getMontantReste() - paiment.getMontant_Paye()  );
            }
        }

        paimentRepositry.save(paiment);
        saleOrderRepositry.save(salesOrder);

        return  modelMapper.map(paiment, PaimentDTO.class);
    }

    @Override
    public PaimentResponse getAllPaiments(Integer pageNumber, Integer pageSize, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by("dateExamen").ascending()
                : Sort.by("dateExamen").descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Paiment> pagePaiments = paimentRepositry.findAll(pageDetails);
        List<Paiment> paiments = pagePaiments.getContent();

        List<PaimentDTO> paimentDTOS = paiments.stream()
                .map(p -> modelMapper.map(p, PaimentDTO.class))
                .toList();

        PaimentResponse paimentResponse = new PaimentResponse();
        paimentResponse.setContent(paimentDTOS);
        paimentResponse.setPageNumber(pagePaiments.getNumber());
        paimentResponse.setPageSize(pagePaiments.getSize());
        paimentResponse.setTotalElements(pagePaiments.getTotalElements());
        paimentResponse.setTotalPages(pagePaiments.getTotalPages());
        paimentResponse.setLastPage(pagePaiments.isLast());

        return paimentResponse;
    }

    @Override
    public List<PaimentDTO> getPaimentsByOrder(Long orderId) {
        SalesOrder salesOrder = saleOrderRepositry.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "OrderId", orderId));

        List<Paiment> paiments = paimentRepositry.findBySalesOrder(salesOrder);

        return paiments.stream()
                .map(p -> modelMapper.map(p, PaimentDTO.class))
                .toList();
    }

    @Override
    public List<PaimentDTO> getPaimentsByClient(Long clientId) {
        Client client = clientRepositry.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "ClientId", clientId));

        List<Paiment> paiments = paimentRepositry.findByClient(client);

        return paiments.stream()
                .map(p -> modelMapper.map(p, PaimentDTO.class))
                .toList();
    }
}
