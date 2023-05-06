package ua.illia.estore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.illia.estore.configuration.security.EmployeeUserDetails;
import ua.illia.estore.converters.impl.ContractorConverter;
import ua.illia.estore.converters.impl.PaymentAccountConverter;
import ua.illia.estore.dto.contractor.ContractorCreateForm;
import ua.illia.estore.dto.contractor.ContractorResponse;
import ua.illia.estore.dto.contractor.ContractorSearchForm;
import ua.illia.estore.dto.paymentaccount.PaymentAccountCreateForm;
import ua.illia.estore.dto.paymentaccount.PaymentAccountResponse;
import ua.illia.estore.services.invoice.ContractorService;
import ua.illia.estore.services.management.PaymentAccountService;

@RestController
@RequestMapping("/api/v1/contractors")
public class ContractorsController {

    @Autowired
    private ContractorService contractorService;

    @Autowired
    private ContractorConverter contractorConverter;

    @Autowired
    private PaymentAccountService paymentAccountService;

    @Autowired
    private PaymentAccountConverter paymentAccountConverter;

    @Transactional
    @PostMapping
    public ContractorResponse create(@RequestBody ContractorCreateForm contractorCreateForm,
                                     @AuthenticationPrincipal EmployeeUserDetails userDetails) {
        return contractorConverter.convert(contractorService.create(contractorCreateForm, userDetails.getUser()));
    }

    @Transactional
    @GetMapping("/{contractorId}")
    public ContractorResponse getById(@PathVariable long contractorId) {
        return contractorConverter.convert(contractorService.getById(contractorId));
    }

    @Transactional
    @GetMapping
    public Page<ContractorResponse> search(ContractorSearchForm contractorSearchForm,
                                           @PageableDefault(size = 24) Pageable pageable) {
        return contractorService.search(contractorSearchForm, pageable)
                .map(contractorConverter::convert);
    }

    @Transactional
    @PostMapping("/{contractorId}/payment-accounts")
    @PreAuthorize("isAuthenticated()")
    public PaymentAccountResponse createPaymentAccount(@PathVariable long contractorId,
                                                       @RequestBody PaymentAccountCreateForm form,
                                                       @AuthenticationPrincipal EmployeeUserDetails userDetails) {
        return paymentAccountConverter.paymentAccountResponse(paymentAccountService.create(form, contractorService.getById(contractorId), userDetails.getUser()));
    }
}
