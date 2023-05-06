package ua.illia.estore.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;
import ua.illia.estore.model.product.Product;

import java.util.List;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long>, StoreProductRepositoryFragment {

    @EntityGraph(Product.Graph.BASIC)
    Page<Product> findAll(Specification<Product> specification, Pageable pageable);

    Product getById(long id);

    List<Product> findAllByIdIn(List<Long> id);
}
