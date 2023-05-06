package ua.illia.estore.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;
import ua.illia.estore.model.security.Customer;

import java.util.Optional;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    Page<Customer> findAll(Specification<Customer> specification, Pageable pageable);

    Optional<Customer> findByUid(String uid);
}
