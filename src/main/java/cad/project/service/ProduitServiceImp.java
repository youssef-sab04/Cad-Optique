package cad.project.service;

import cad.project.exceptions.APIException;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.Category;
import cad.project.model.Produit;
import cad.project.playload.CategoryDTO;
import cad.project.playload.ProductResponse;
import cad.project.playload.ProduitDTO;
import cad.project.repositries.CategoryRepositry;
import cad.project.repositries.ProduitRepositry;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${project.image}")
    private String path;

    @Value("${image.base.url}")
    private String imageBaseUrl;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProduitDTO addProduit(Long categoryId, ProduitDTO produitDTO  , MultipartFile image) throws IOException {

        Category category = categoryRepositry.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category" , "CategoryId" , categoryId));

        Produit product = modelMapper.map(produitDTO, Produit.class);

        product.setCategory(category);

        String fileName = fileServiceImp.uploadImage(path, image);
        product.setImage(fileName);

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

    private String constructImageUrl(String imageName) {
        return imageBaseUrl.endsWith("/") ? imageBaseUrl + imageName : imageBaseUrl + "/" + imageName;
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
                    productDTO.setImage(constructImageUrl(product.getImage()));
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
    public ProduitDTO updateProduct(Long productId, ProduitDTO productDTO, MultipartFile image) throws IOException {

        Produit productFromDb = produitRepositry.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        Produit product = modelMapper.map(productDTO, Produit.class);

        // set
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
            String fileName = fileServiceImp.uploadImage(path, image);
            productFromDb.setImage(fileName);
        }

        double tva =  0 ;  //product.getCategory().getTva();
        double prixTVA = product.getPrixHT() + product.getPrixHT() * tva * 0.01;
        productFromDb.setPrice(prixTVA);



        double finalPrice = 0;
        if (product.getDiscount() != null && product.getDiscount() > 0) {
             finalPrice = prixTVA - (prixTVA * product.getDiscount() / 100);
             productFromDb.setPrice(finalPrice);
        }





        Produit savedProduct = produitRepositry.save(productFromDb);



        return modelMapper.map(savedProduct, ProduitDTO.class);
    }

    @Override
    public ProduitDTO deleteProduct(Long productId) {
        Produit product = produitRepositry.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));


        produitRepositry.delete(product);
        return modelMapper.map(product, ProduitDTO.class);

    }
}
