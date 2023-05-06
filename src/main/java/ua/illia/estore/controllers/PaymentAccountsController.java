package ua.illia.estore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.illia.estore.converters.impl.PaymentAccountConverter;
import ua.illia.estore.dto.paymentaccount.PaymentAccountResponse;
import ua.illia.estore.dto.paymentaccountoperation.PaymentAccountOperationResponse;
import ua.illia.estore.services.management.PaymentAccountService;

@RestController
@RequestMapping("/api/v1/payment-accounts")
public class PaymentAccountsController {

    @Autowired
    private PaymentAccountService paymentAccountService;

    @Autowired
    private PaymentAccountConverter paymentAccountConverter;

    @Transactional
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public Page<PaymentAccountResponse> getPage(@PageableDefault(size = 50) Pageable pageable) {
        return paymentAccountService.getPage(pageable).map(paymentAccountConverter::paymentAccountResponse);
    }

    @Transactional
    @GetMapping("/{id}/operations")
    public Page<PaymentAccountOperationResponse> getById(@PathVariable long id,
                                                         @PageableDefault(size = 500, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return paymentAccountService.getPaymentAccountOperations(id, pageable)
                .map(paymentAccountConverter::paymentAccountOperation);
    }
}
