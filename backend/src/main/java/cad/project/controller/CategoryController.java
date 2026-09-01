package cad.project.controller;


import cad.project.config.AppConstants;
import cad.project.playload.CategoryDTO;
import cad.project.playload.CategoryDTO;
import cad.project.playload.CategoryResponse;
import cad.project.service.CategoryService;
import cad.project.service.ProduitService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    ProduitService produitService;

    @Autowired
    CategoryService categoryService;

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Ajouter une category")
    @PostMapping("/admin/category")
    public ResponseEntity<CategoryDTO> addCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        CategoryDTO categoryDTOSaved = categoryService.addCategory(categoryDTO);
        return new ResponseEntity<>(categoryDTOSaved, HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Modifier un category")
    @PutMapping("/admin/categorys/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                                  @PathVariable Long categoryId){
        CategoryDTO updatedCategoryDTO = categoryService.updateCategory(categoryId, categoryDTO);
        return new ResponseEntity<>(updatedCategoryDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Supprimer un category")
    @DeleteMapping("/admin/categorys/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId){
        CategoryDTO deletedCategory = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(deletedCategory, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESPONSABLE')")
    @Operation(summary = "Get Categories")
    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber" ,defaultValue = AppConstants.PAGE_NUMBER , required = false) Integer pageNumber,
            @RequestParam(name = "pageSize" ,defaultValue = AppConstants.PAGE_SIZE , required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY_NOUN, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ){
        CategoryResponse categoryResponse = categoryService.getAllCategories( pageNumber ,  pageSize , sortBy , sortOrder);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }




}
