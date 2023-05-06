package ua.illia.estore.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;
import ua.illia.estore.model.orders.Order;
import ua.illia.estore.model.security.Customer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {

    Page<Order> findAll(Specification<Order> specification, Pageable pageable);

    Order getById(Long id);

    Optional<Order> getByUid(String uid);

    List<Order> findAll(Specification<Order> specification);

    long countByCreationDateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Order> findAllByCustomerAndCreationDateBetween(Customer customer, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
