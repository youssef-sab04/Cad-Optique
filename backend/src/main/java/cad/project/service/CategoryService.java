package cad.project.service;

import cad.project.playload.CategoryDTO;
import cad.project.playload.CategoryResponse;
import jakarta.validation.Valid;

public interface CategoryService {
    CategoryDTO addCategory(@Valid CategoryDTO categoryDTO);

    CategoryDTO updateCategory(Long categoryId, @Valid CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(Long categoryId);

    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}
