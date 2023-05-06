package ua.illia.estore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.illia.estore.model.management.Bill;

public interface BillsRepository extends JpaRepository<Bill, Long> {
}
