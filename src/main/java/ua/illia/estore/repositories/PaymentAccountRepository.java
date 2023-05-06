package ua.illia.estore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.illia.estore.model.management.PaymentAccount;

public interface PaymentAccountRepository extends JpaRepository<PaymentAccount, Long> {
}
