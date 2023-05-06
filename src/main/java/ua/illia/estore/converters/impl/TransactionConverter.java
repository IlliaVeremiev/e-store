package ua.illia.estore.converters.impl;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.illia.estore.dto.transaction.TransactionResponse;
import ua.illia.estore.model.management.Contractor;
import ua.illia.estore.model.management.PaymentAccount;
import ua.illia.estore.model.management.Transaction;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.services.management.PaymentAccountService;

@Component
public class TransactionConverter {

    @Autowired
    private PaymentAccountService paymentAccountService;

    public TransactionResponse transactionResponse(Transaction transaction) {
        PaymentAccount fromPaymentAccount = transaction.getSenderPaymentAccount();
        PaymentAccount toPaymentAccount = transaction.getReceiverPaymentAccount();

        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setDebitedAmount(transaction.getFromOutcomeAmount());
        response.setDebitedCurrency(fromPaymentAccount.getCurrency().getCode());
        response.setCreditedAmount(transaction.getToIncomeAmount());
        response.setCreditedCurrency(toPaymentAccount.getCurrency().getCode());
        response.setCommission(transaction.getCommission());
        response.setExchangeRate(transaction.getExchangeRate());
        response.setStatus(transaction.getStatus());
        response.setFromPayableUid(fromPaymentAccount.getOwnerPayableUid());
        response.setFromName(getPaymentAccountOwnerName(fromPaymentAccount));
        response.setToPayableUid(toPaymentAccount.getOwnerPayableUid());
        response.setToName(getPaymentAccountOwnerName(toPaymentAccount));
        response.setDescription(transaction.getDescription());
        if (transaction.getImage() != null) response.setImage(transaction.getImage().getPath());
        response.setDate(transaction.getDate());
        response.setCreationDate(transaction.getCreationDate());
        response.setCreatedBy(transaction.getCreatedBy().getId());
        return response;
    }

    private String getPaymentAccountOwnerName(PaymentAccount paymentAccount) {
        Object owner = paymentAccountService.getPaymentAccountOwner(paymentAccount);
        if (Employee.class.equals(owner.getClass())) {
            Employee employee = (Employee) owner;
            return employee.getLastName() + " " + employee.getFirstName();
        } else if (Contractor.class.equals(owner.getClass())) {
            Contractor contractor = (Contractor) owner;
            return contractor.getName();
        }
        throw new NotImplementedException("PaymentAccount owner not found");
    }
}
