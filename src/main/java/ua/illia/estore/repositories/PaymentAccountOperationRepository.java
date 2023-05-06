package ua.illia.estore.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ua.illia.estore.model.management.PaymentAccountOperation;

public interface PaymentAccountOperationRepository extends PagingAndSortingRepository<PaymentAccountOperation, Long> {
    Page<PaymentAccountOperation> findAllByPaymentAccountId(long id, Pageable pageable);
}
