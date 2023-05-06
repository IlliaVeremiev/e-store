package ua.illia.estore.converters.impl;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.illia.estore.dto.paymentaccount.PaymentAccountResponse;
import ua.illia.estore.dto.paymentaccountoperation.PaymentAccountOperationResponse;
import ua.illia.estore.model.interfaces.Accountable;
import ua.illia.estore.model.management.Contractor;
import ua.illia.estore.model.management.PaymentAccount;
import ua.illia.estore.model.management.PaymentAccountOperation;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.services.management.PaymentAccountService;

@Component
public class PaymentAccountConverter {

    @Autowired
    private PaymentAccountService paymentAccountService;

    public PaymentAccountResponse paymentAccountResponse(PaymentAccount paymentAccount) {
        PaymentAccountResponse response = new PaymentAccountResponse();
        response.setId(paymentAccount.getId());
        response.setName(paymentAccount.getName());
        response.setBalance(paymentAccount.getBalance());
        response.setCurrency(paymentAccount.getCurrency().getShortName());

        Accountable owner = paymentAccountService.getPaymentAccountOwner(paymentAccount);
        if (Employee.class.equals(owner.getClass()))
            response.setOwnerName(((Employee) owner).getFirstName() + " " + ((Employee) owner).getFirstName());
        else if (Contractor.class.equals(owner.getClass())) response.setOwnerName(((Contractor) owner).getName());
        else
            throw new NotImplementedException("Can't exctract name for PaymentAccount owner type: " + owner.getClass());

        return response;
    }

    public PaymentAccountOperationResponse paymentAccountOperation(PaymentAccountOperation paymentAccountOperation) {
        PaymentAccountOperationResponse response = new PaymentAccountOperationResponse();
        response.setId(paymentAccountOperation.getId());
        response.setOperationType(paymentAccountOperation.getOperationType());
        response.setPurposeType(paymentAccountOperation.getPurposeType());
        response.setBusinessKey(paymentAccountOperation.getBusinessKey());
        response.setAmount(paymentAccountOperation.getAmount());
        response.setResultBalance(paymentAccountOperation.getResultBalance());
        response.setCreationDate(paymentAccountOperation.getCreationDate());
        response.setCreatedBy(paymentAccountOperation.getCreatedBy().getId());
        return response;
    }
}
