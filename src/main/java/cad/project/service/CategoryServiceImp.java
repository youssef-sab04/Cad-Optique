package cad.project.service;

import cad.project.exceptions.APIException;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.Category;
import cad.project.model.Produit;
import cad.project.playload.CategoryDTO;
import cad.project.playload.CategoryResponse;
import cad.project.repositries.CategoryRepositry;
import cad.project.repositries.ProduitRepositry;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImp  implements  CategoryService{
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private CategoryRepositry categoryRepositry;

    @Autowired
    private ProduitRepositry produitRepositry;
    
    
    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO , Category.class);
        Category categorySaved = categoryRepositry.save(category);

        return modelMapper.map(categorySaved , CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Category categoryFromDb = categoryRepositry.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        Category category = modelMapper.map(categoryDTO, Category.class);

        if (category.getNom() != null) {
            categoryFromDb.setNom(category.getNom());
        }
        if (category.getDescription() != null) {
            categoryFromDb.setDescription(category.getDescription());
        }

        if (categoryDTO.getTva() != null) {
            categoryFromDb.setTva(categoryDTO.getTva());
            List<Produit> produits = categoryFromDb.getProduitList();
            for (Produit p : produits) {
                p.setTva(categoryDTO.getTva());
            }
            produitRepositry.saveAll(produits);
        }

        categoryRepositry.save(categoryFromDb);

        return modelMapper.map(categoryFromDb, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category deletedCategory = categoryRepositry.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId" , categoryId));

        categoryRepositry.delete(deletedCategory);
        CategoryDTO deletedCategoryDTO = modelMapper.map(deletedCategory , CategoryDTO.class);

        return deletedCategoryDTO;
    }

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber , pageSize , sortByAndOrder);
        Page<Category> categoryPage = categoryRepositry.findAll(pageDetails);

        List<Category> categories = categoryPage.getContent();
        if(categories.isEmpty())
            throw new APIException("No Category Created till now");

        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }
}
