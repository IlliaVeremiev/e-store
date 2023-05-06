package ua.illia.estore.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;
import ua.illia.estore.model.warehouse.Warehouse;

import java.util.List;

public interface WarehouseRepository extends PagingAndSortingRepository<Warehouse, Long> {

    Page<Warehouse> findAll(Specification<Warehouse> specification, Pageable pageable);

    List<Warehouse> findAll();

    List<Warehouse> findAllByIdIn(List<Long> id);
}
