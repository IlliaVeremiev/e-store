package ua.illia.estore.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;
import ua.illia.estore.model.management.Contractor;

import java.util.Optional;

public interface ContractorRepository extends PagingAndSortingRepository<Contractor, Long> {

    Page<Contractor> findAll(Specification<Contractor> specification, Pageable pageable);

    Optional<Contractor> findByPayableUid(String payableUid);
}
