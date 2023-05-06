package ua.illia.estore.services.management;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.illia.estore.dto.paymentaccount.PaymentAccountCreateForm;
import ua.illia.estore.model.interfaces.Accountable;
import ua.illia.estore.model.management.PaymentAccount;
import ua.illia.estore.model.management.PaymentAccountOperation;
import ua.illia.estore.model.management.enums.PaymentAccountOperationPurposeType;
import ua.illia.estore.model.security.Employee;

import java.math.BigDecimal;

public interface PaymentAccountService {

    PaymentAccountOperation makeCredit(PaymentAccount account, BigDecimal amount, PaymentAccountOperationPurposeType purpose, String businessKey, Employee employee);

    PaymentAccountOperation makeDebit(PaymentAccount account, BigDecimal amount, PaymentAccountOperationPurposeType purpose, String businessKey, Employee employee);

    PaymentAccount save(PaymentAccount paymentAccount);

    PaymentAccount getById(long id);

    Page<PaymentAccount> getPage(Pageable pageable);

    Accountable getPaymentAccountOwner(PaymentAccount paymentAccount);

    PaymentAccount create(PaymentAccountCreateForm form, Accountable owner, Employee createdBy);

    Page<PaymentAccountOperation> getPaymentAccountOperations(long id, Pageable pageable);
}
