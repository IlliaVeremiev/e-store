package ua.illia.estore.services.management.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.illia.estore.configuration.exceptions.ConflictException;
import ua.illia.estore.configuration.exceptions.NotFoundException;
import ua.illia.estore.dto.transaction.TransactionCreateForm;
import ua.illia.estore.model.management.PaymentAccount;
import ua.illia.estore.model.management.Transaction;
import ua.illia.estore.model.management.enums.PaymentAccountOperationPurposeType;
import ua.illia.estore.model.management.enums.TransactionStatus;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.repositories.TransactionRepository;
import ua.illia.estore.services.management.PaymentAccountService;
import ua.illia.estore.services.management.TransactionService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

@Service
public class DefaultTransactionService implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PaymentAccountService paymentAccountService;

    @Override
    public Page<Transaction> getPage(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }


    @Override
    @Transactional
    public Transaction create(TransactionCreateForm form, Employee employee) {
        PaymentAccount fromPaymentAccount = paymentAccountService.getById(form.getFrom());
        PaymentAccount toPaymentAccount = paymentAccountService.getById(form.getTo());

        Transaction transaction = new Transaction();
        transaction.setSenderPaymentAccount(fromPaymentAccount);
        transaction.setSenderPayableUid(fromPaymentAccount.getOwnerPayableUid());
        transaction.setReceiverPaymentAccount(toPaymentAccount);
        transaction.setReceiverPayableUid(toPaymentAccount.getOwnerPayableUid());

        transaction.setFromOutcomeAmount(form.getFromOutcomeAmount());
        transaction.setFromCurrency(fromPaymentAccount.getCurrency());

        transaction.setToIncomeAmount(form.getToIncomeAmount());
        transaction.setToCurrency(toPaymentAccount.getCurrency());

        transaction.setCommission(form.getCommission());
        transaction.setExchangeRate(form.getExchangeRate());
        transaction.setDescription(form.getDescription());
        transaction.setStatus(TransactionStatus.CREATED);

        transaction.setDate(form.getDate());
        transaction.setCreationDate(LocalDateTime.now());
        transaction.setCreatedBy(employee);
        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public Transaction changeStatus(long id, TransactionStatus status, Employee employee) {
        Transaction transaction = getById(id);
        if (status == TransactionStatus.PROCESSED) {
            if (!transaction.getStatus().equals(TransactionStatus.CREATED)) {
                throw new ConflictException("Transaction cannot be processed", "transaction.status");
            }

            PaymentAccount fromPaymentAccount = transaction.getSenderPaymentAccount();
            PaymentAccount toPaymentAccount = transaction.getReceiverPaymentAccount();

            paymentAccountService.makeCredit(fromPaymentAccount, transaction.getFromOutcomeAmount(),
                    PaymentAccountOperationPurposeType.TRANSACTION, String.valueOf(id), employee);
            paymentAccountService.makeDebit(toPaymentAccount, transaction.getToIncomeAmount(),
                    PaymentAccountOperationPurposeType.TRANSACTION, String.valueOf(id), employee);

        } else if (status == TransactionStatus.CANCELLED) {
            if (!transaction.getStatus().equals(TransactionStatus.CREATED)) {
                throw new ConflictException("Transaction cannot be cancelled", "transaction.status");
            }
        } else if (status == TransactionStatus.ROLLED_BACK) {
            if (!transaction.getStatus().equals(TransactionStatus.PROCESSED)) {
                throw new ConflictException("Transaction cannot be rolled back", "transaction.status");
            }
            if (LocalDateTime.now().getLong(ChronoField.SECOND_OF_DAY) - transaction.getCreationDate().getLong(ChronoField.SECOND_OF_DAY) > 60 * 60 * 24) {
                throw new ConflictException("Transaction cannot be rolled back after 24 hours", "transaction.rollback");
            }
            PaymentAccount fromPaymentAccount = transaction.getSenderPaymentAccount();
            PaymentAccount toPaymentAccount = transaction.getReceiverPaymentAccount();

            paymentAccountService.makeCredit(toPaymentAccount, transaction.getToIncomeAmount(),
                    PaymentAccountOperationPurposeType.TRANSACTION_ROLLBACK, String.valueOf(id), employee);
            paymentAccountService.makeDebit(fromPaymentAccount, transaction.getFromOutcomeAmount(),
                    PaymentAccountOperationPurposeType.TRANSACTION_ROLLBACK, String.valueOf(id), employee);

        }
        transaction.setStatus(status);
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction getById(long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction with id: " + id + " not found", "transaction.id"));
    }
}
