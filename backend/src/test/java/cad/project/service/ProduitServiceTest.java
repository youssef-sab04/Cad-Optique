package cad.project.service;

import cad.project.exceptions.APIException;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.Category;
import cad.project.model.Produit;
import cad.project.playload.ProduitDTO;
import cad.project.repositries.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProduitServiceTest {

    @Mock
    private CategoryRepositry categoryRepositry;
    @Mock
    private ProduitRepositry produitRepositry;
    @Mock
    private FileServiceImp fileServiceImp;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private CommandeItemRepositry commandeItemRepositry;
    @Mock
    private DevisItemsRepositry devisItemRepositry;
    @Mock
    private SaleOrderItemsRepositry saleOrderItemsRepositry;
    @Mock
    private CommandeItemServiceImp commandeItemServiceImp;
    @Mock
    private DevisItemServiceImp devisItemServiceImp;
    @Mock
    private SalesOrderItemServiceImp salesOrderItemServiceImp;

    @InjectMocks
    private ProduitServiceImp produitServiceImp;

    @Test
    @DisplayName("Doit lever ResourceNotFoundException quand la catégorie est introuvable")
    void test_addProduit_when_categorie_introuvable() {
        Long categoryId = 99L;

        when(categoryRepositry.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> produitServiceImp.addProduit(categoryId, new ProduitDTO(), null));
    }

    @Test
    @DisplayName("Doit lever APIException quand le prixHT est invalide")
    void test_addProduit_when_prixHT_invalide() throws IOException {
        Long categoryId = 1L;
        Category category = new Category();
        category.setTva(20f);

        Produit produit = new Produit();
        produit.setPrixHT(0.0);

        ProduitDTO dto = new ProduitDTO();
        MultipartFile image = new MockMultipartFile("image", new byte[0]);

        when(categoryRepositry.findById(categoryId)).thenReturn(Optional.of(category));
        when(modelMapper.map(dto, Produit.class)).thenReturn(produit);
        when(fileServiceImp.uploadImage(image)).thenReturn("url");

        assertThrows(APIException.class,
                () -> produitServiceImp.addProduit(categoryId, dto, image));
    }

    @Test
    @DisplayName("Doit calculer le prix TTC avec TVA et remise à la création")
    void test_addProduit_nominal_calcul_prix() throws IOException {
        Long categoryId = 1L;
        Category category = new Category();
        category.setTva(20f);

        Produit produit = new Produit();
        produit.setPrixHT(100.0);
        produit.setDiscount(10.0F);

        ProduitDTO dto = new ProduitDTO();
        MultipartFile image = new MockMultipartFile("image", new byte[0]);

        when(categoryRepositry.findById(categoryId)).thenReturn(Optional.of(category));
        when(modelMapper.map(dto, Produit.class)).thenReturn(produit);
        when(fileServiceImp.uploadImage(image)).thenReturn("url");
        when(produitRepositry.save(produit)).thenReturn(produit);
        when(modelMapper.map(produit, ProduitDTO.class)).thenReturn(new ProduitDTO());

        produitServiceImp.addProduit(categoryId, dto, image);

        // prixAvecTva = 120, finalPrice = 120 - (120*10/100) = 108
        assertEquals(108.0, produit.getPrice());
    }

    @Test
    @DisplayName("Doit lever ResourceNotFoundException quand le produit est introuvable à la mise à jour")
    void test_updateProduct_when_id_introuvable() {
        Long id = 99L;

        when(produitRepositry.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> produitServiceImp.updateProduct(id, new ProduitDTO(), null));
    }

    @Test
    @DisplayName("Doit recalculer le prix sans TVA et avec remise à la mise à jour")
    void test_updateProduct_nominal_calcul_prix() throws IOException {
        Long id = 1L;
        Produit produitFromDb = new Produit();

        Produit produit = new Produit();
        produit.setPrixHT(200.0);
        produit.setDiscount(25.0F);

        ProduitDTO dto = new ProduitDTO();

        when(produitRepositry.findById(id)).thenReturn(Optional.of(produitFromDb));
        when(modelMapper.map(dto, Produit.class)).thenReturn(produit);
        when(produitRepositry.save(produitFromDb)).thenReturn(produitFromDb);
        when(modelMapper.map(produitFromDb, ProduitDTO.class)).thenReturn(new ProduitDTO());

        produitServiceImp.updateProduct(id, dto, null);

        assertEquals(150.0, produitFromDb.getPrice());
    }

    @Test
    @DisplayName("Doit lever ResourceNotFoundException quand le produit est introuvable à la suppression")
    void test_deleteProduct_when_id_introuvable() {
        Long id = 99L;

        when(produitRepositry.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> produitServiceImp.deleteProduct(id));
    }

    @Test
    @DisplayName("Doit supprimer le produit trouvé")
    void test_deleteProduct_nominal() {
        Long id = 1L;
        Produit produit = new Produit();

        when(produitRepositry.findById(id)).thenReturn(Optional.of(produit));
        when(modelMapper.map(produit, ProduitDTO.class)).thenReturn(new ProduitDTO());

        produitServiceImp.deleteProduct(id);

        assertTrue(true);
}
}