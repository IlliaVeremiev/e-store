package ua.illia.estore.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;
import ua.illia.estore.model.product.Category;

import java.util.Optional;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

    @EntityGraph(Category.Graph.BASIC)
    Optional<Category> findByUid(String uid);

    @EntityGraph(Category.Graph.BASIC)
    Optional<Category> findById(Long id);

    @EntityGraph(Category.Graph.BASIC)
    Page<Category> findAll(Specification<Category> specification, Pageable pageable);
}
