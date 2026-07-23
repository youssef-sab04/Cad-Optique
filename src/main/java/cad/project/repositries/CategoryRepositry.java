package cad.project.repositries;

import cad.project.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepositry extends JpaRepository<Category, Long> {
    //Page<Client> findAll(Specification<Client> spec, Pageable pageDetails);

}
