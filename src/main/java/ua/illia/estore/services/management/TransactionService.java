package ua.illia.estore.services.management;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.illia.estore.dto.transaction.TransactionCreateForm;
import ua.illia.estore.model.management.Transaction;
import ua.illia.estore.model.management.enums.TransactionStatus;
import ua.illia.estore.model.security.Employee;

public interface TransactionService {

    Page<Transaction> getPage(Pageable pageable);

    Transaction create(TransactionCreateForm form, Employee employee);

    Transaction changeStatus(long id, TransactionStatus status, Employee employee);

    Transaction getById(long id);
}
