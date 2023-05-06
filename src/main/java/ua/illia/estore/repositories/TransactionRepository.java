package ua.illia.estore.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ua.illia.estore.model.management.Transaction;

public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {
}
