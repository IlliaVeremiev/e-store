package ua.illia.estore.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;
import ua.illia.estore.model.product.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Long> {

    Page<Brand> findAll(Specification<Brand> specification, Pageable pageable);

    Optional<Brand> findByUid(String uid);

    List<Brand> findByUidIn(List<String> uid);

    List<Brand> findAllByIdIn(List<Long> id);
}
