package ua.illia.estore.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;
import ua.illia.estore.model.security.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends PagingAndSortingRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    Page<Employee> findAll(Specification<Employee> specification, Pageable pageable);

    List<Employee> findAll();

    Optional<Employee> findByUid(String uid);

    Optional<Employee> findByPayableUid(String payableUid);
}