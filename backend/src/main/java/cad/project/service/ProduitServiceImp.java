package cad.project.service;

import cad.project.exceptions.APIException;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.*;
import cad.project.playload.CategoryDTO;
import cad.project.playload.ProductResponse;
import cad.project.playload.ProduitDTO;
import cad.project.repositries.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProduitServiceImp implements  ProduitService {

    @Autowired
    private CategoryRepositry categoryRepositry;

    @Autowired
    private ProduitRepositry produitRepositry;
    @Autowired
    private FileServiceImp fileServiceImp;

    @Autowired
    private CommandeItemRepositry commandeItemRepositry;

    @Autowired
    private DevisItemsRepositry devisItemRepositry;

    @Autowired
    private SaleOrderItemsRepositry saleOrderItemsRepositry;

    @Autowired
    private CommandeItemServiceImp commandeItemServiceImp;

    @Autowired
    private DevisItemServiceImp devisItemServiceImp;

    @Autowired
    private SalesOrderItemServiceImp salesOrderItemServiceImp;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProduitDTO addProduit(Long categoryId, ProduitDTO produitDTO  , MultipartFile image) throws IOException {

        Category category = categoryRepositry.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category" , "CategoryId" , categoryId));

        Produit product = modelMapper.map(produitDTO, Produit.class);

        product.setCategory(category);

        String imageUrl = fileServiceImp.uploadImage(image);
        product.setImage(imageUrl);

        float tva = category.getTva();
        product.setTva(tva);
        if(product.getPrixHT() == null || product.getPrixHT() <= 0 ){
            throw new APIException("Le prix est errones");

        }
        double prixHT = product.getPrixHT();
        double prixAvecTva = prixHT + (prixHT * tva / 100);
        double finalPrice = prixAvecTva;
        if (product.getDiscount() != null && product.getDiscount() > 0) {
            finalPrice -=   (prixAvecTva * product.getDiscount() / 100);
        }
        product.setPrice(finalPrice);

        Produit savedProduct = produitRepositry.save(product);

        return  modelMapper.map(savedProduct, ProduitDTO.class);
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword, String category) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Specification<Produit> spec = (root, query, cb) -> cb.conjunction();
        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("nom")), "%" + keyword.toLowerCase() + "%"));
        }

        if (category != null && !category.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("category").get("nom"), category));
        }

        Page<Produit> pageProducts = produitRepositry.findAll(spec, pageDetails);

        List<Produit> products = pageProducts.getContent();

        List<ProduitDTO> productDTOS = products.stream()
                .map(product -> {
                    ProduitDTO productDTO = modelMapper.map(product, ProduitDTO.class);
                    productDTO.setImage(product.getImage());
                    if(product.getCategory() != null) {
                        productDTO.setCategoryDTO(modelMapper.map(product.getCategory(), CategoryDTO.class));
                    }
                    return productDTO;
                })
                .toList();

        if(products.isEmpty()){
            throw new APIException("Aucun produit");
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());
        return  productResponse;
    }


    @Override
    public ProduitDTO deleteProduct(Long productId) {
        Produit product = produitRepositry.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));


        produitRepositry.delete(product);
        return modelMapper.map(product, ProduitDTO.class);

    }

    @Override
    public ProduitDTO updateProduct(Long productId, ProduitDTO productDTO, MultipartFile image) throws IOException {
        Produit productFromDb = produitRepositry.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        Produit product = modelMapper.map(productDTO, Produit.class);
        productFromDb.setNom(product.getNom());
        productFromDb.setDescription(product.getDescription());
        productFromDb.setCode_barre(product.getCode_barre());
        productFromDb.setQuantity(product.getQuantity());
        productFromDb.setPrixHT(product.getPrixHT());
        productFromDb.setDiscount(product.getDiscount());
        productFromDb.setMarque(product.getMarque());
        productFromDb.setCouleur(product.getCouleur());
        productFromDb.setModele(product.getModele());
        productFromDb.setIndice(product.getIndice());
        productFromDb.setDiametre(product.getDiametre());
        productFromDb.setSeuilMin(product.getSeuilMin());
        productFromDb.setTraitement(product.getTraitement());
        if (image != null && !image.isEmpty()) {
            String imageUrl = fileServiceImp.uploadImage(image);
            productFromDb.setImage(imageUrl);
        }
        double tva = 0;
        double prixTVA = product.getPrixHT() + product.getPrixHT() * tva * 0.01;
        productFromDb.setPrice(prixTVA);
        double finalPrice = 0;
        if (product.getDiscount() != null && product.getDiscount() > 0) {
            finalPrice = prixTVA - (prixTVA * product.getDiscount() / 100);
            productFromDb.setPrice(finalPrice);
        }
        Produit savedProduct = produitRepositry.save(productFromDb);

        List<CommandeItem> commandeItems = commandeItemRepositry.findByProduitId(productId);
        commandeItems.stream()
                .filter(item -> !"LIVREE".equals(item.getCommande().getStatus()))
                .forEach(item -> {
                    item.setPrice(savedProduct.getPrice());
                    commandeItemRepositry.save(item);
                    commandeItemServiceImp.UpdateTotlPrice(item.getCommande().getId());
                });

        List<DevisItems> devisItems = devisItemRepositry.findByProduitId(productId);
        devisItems.stream()
                .filter(item -> !"VALIDE".equals(item.getDevis().getStatus()))
                .forEach(item -> {
                    item.setPrice(savedProduct.getPrice());
                    devisItemRepositry.save(item);
                    devisItemServiceImp.UpdateTotlPrice(item.getDevis().getId());
                });

        List<SalesOrderItems> salesOrderItems = saleOrderItemsRepositry.findByProduitId(productId);
        salesOrderItems.stream()
                .filter(item -> !"VALIDE".equals(item.getSalesOrder().getStatus()))
                .forEach(item -> {
                    item.setPrice(savedProduct.getPrice());
                    saleOrderItemsRepositry.save(item);
                    salesOrderItemServiceImp.UpdateTotlPrice(item.getSalesOrder().getId());
                });

        return modelMapper.map(savedProduct, ProduitDTO.class);
    }

}