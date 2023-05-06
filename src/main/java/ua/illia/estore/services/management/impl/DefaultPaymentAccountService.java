package ua.illia.estore.services.management.impl;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.illia.estore.configuration.exceptions.NotFoundException;
import ua.illia.estore.dto.paymentaccount.PaymentAccountCreateForm;
import ua.illia.estore.model.interfaces.Accountable;
import ua.illia.estore.model.management.Contractor;
import ua.illia.estore.model.management.PaymentAccount;
import ua.illia.estore.model.management.PaymentAccountOperation;
import ua.illia.estore.model.management.enums.PaymentAccountOperationPurposeType;
import ua.illia.estore.model.management.enums.PaymentAccountOperationType;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.repositories.ContractorRepository;
import ua.illia.estore.repositories.EmployeeRepository;
import ua.illia.estore.repositories.PaymentAccountOperationRepository;
import ua.illia.estore.repositories.PaymentAccountRepository;
import ua.illia.estore.services.management.PaymentAccountService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class DefaultPaymentAccountService implements PaymentAccountService {

    @Autowired
    private PaymentAccountRepository paymentAccountRepository;

    @Autowired
    private PaymentAccountOperationRepository paymentAccountOperationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ContractorRepository contractorRepository;

    @Override
    @Transactional
    public PaymentAccountOperation makeCredit(PaymentAccount account, BigDecimal amount, PaymentAccountOperationPurposeType purpose, String businessKey, Employee employee) {
        BigDecimal resultBalance = account.getBalance().subtract(amount);

        account.setBalance(resultBalance);
        paymentAccountRepository.save(account);

        PaymentAccountOperation operation = new PaymentAccountOperation();
        operation.setPaymentAccount(account);
        operation.setOperationType(PaymentAccountOperationType.CREDIT);
        operation.setCreationDate(LocalDateTime.now());
        operation.setCreatedBy(employee);
        operation.setAmount(amount);
        operation.setResultBalance(resultBalance);
        operation.setPurposeType(purpose);
        operation.setBusinessKey(businessKey);
        return paymentAccountOperationRepository.save(operation);
    }

    @Override
    public PaymentAccountOperation makeDebit(PaymentAccount account, BigDecimal amount, PaymentAccountOperationPurposeType purpose, String businessKey, Employee employee) {
        BigDecimal resultBalance = account.getBalance().add(amount);

        account.setBalance(resultBalance);
        paymentAccountRepository.save(account);

        PaymentAccountOperation operation = new PaymentAccountOperation();
        operation.setPaymentAccount(account);
        operation.setOperationType(PaymentAccountOperationType.DEBIT);
        operation.setCreationDate(LocalDateTime.now());
        operation.setCreatedBy(employee);
        operation.setAmount(amount);
        operation.setResultBalance(resultBalance);
        operation.setPurposeType(purpose);
        operation.setBusinessKey(businessKey);
        return paymentAccountOperationRepository.save(operation);
    }

    @Override
    public PaymentAccount save(PaymentAccount paymentAccount) {
        return paymentAccountRepository.save(paymentAccount);
    }


    @Override
    public PaymentAccount getById(long id) {
        return paymentAccountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("PaymentAccount with id: " + id + " not found", "paymentAccount.id"));
    }

    @Override
    public Page<PaymentAccount> getPage(Pageable pageable) {
        return paymentAccountRepository.findAll(pageable);
    }

    @Override
    public Accountable getPaymentAccountOwner(PaymentAccount paymentAccount) {
        Employee employee = employeeRepository.findByPayableUid(paymentAccount.getOwnerPayableUid())
                .orElse(null);
        if (employee != null) {
            return employee;
        }
        Contractor contractor = contractorRepository.findByPayableUid(paymentAccount.getOwnerPayableUid())
                .orElse(null);
        if (contractor != null) {
            return contractor;
        }
        throw new NotImplementedException("Unable to find owner for paymentAccount with id: " + paymentAccount.getId());
    }

    @Override
    public PaymentAccount create(PaymentAccountCreateForm form, Accountable owner, Employee createdBy) {
        PaymentAccount paymentAccount = new PaymentAccount();
        paymentAccount.setName(form.getName());
        paymentAccount.setOwnerPayableUid(owner.getPayableUid());
        paymentAccount.setCurrency(form.getCurrency());
        paymentAccount.setCreationDate(LocalDateTime.now());
        paymentAccount.setBalance(BigDecimal.ZERO);
        paymentAccount.setCreatedBy(createdBy);
        return paymentAccountRepository.save(paymentAccount);
    }

    @Override
    public Page<PaymentAccountOperation> getPaymentAccountOperations(long id, Pageable pageable) {
        return paymentAccountOperationRepository.findAllByPaymentAccountId(id, pageable);
    }
}
