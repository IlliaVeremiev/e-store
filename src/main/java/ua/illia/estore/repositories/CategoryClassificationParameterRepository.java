package ua.illia.estore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.illia.estore.model.product.Category;
import ua.illia.estore.model.product.CategoryClassificationParameter;

import java.util.List;

public interface CategoryClassificationParameterRepository extends JpaRepository<CategoryClassificationParameter, Long> {

    List<CategoryClassificationParameter> findAllByCategory(Category category);
}
