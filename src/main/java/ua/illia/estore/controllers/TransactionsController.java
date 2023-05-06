package ua.illia.estore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.illia.estore.configuration.security.EmployeeUserDetails;
import ua.illia.estore.converters.impl.TransactionConverter;
import ua.illia.estore.dto.transaction.TransactionCreateForm;
import ua.illia.estore.dto.transaction.TransactionResponse;
import ua.illia.estore.model.management.enums.TransactionStatus;
import ua.illia.estore.services.management.TransactionService;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionsController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionConverter transactionConverter;

    @GetMapping
    public Page<TransactionResponse> getPage(@PageableDefault(size = 100, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return transactionService.getPage(pageable).map(transactionConverter::transactionResponse);
    }

    @GetMapping("/{transactionId}")
    public TransactionResponse getById(@PathVariable long transactionId) {
        return transactionConverter.transactionResponse(transactionService.getById(transactionId));
    }

    @PostMapping
    public TransactionResponse create(@RequestBody TransactionCreateForm form,
                                      @AuthenticationPrincipal EmployeeUserDetails userDetails) {
        return transactionConverter.transactionResponse(transactionService.create(form, userDetails.getUser()));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{transactionId}/status/{status}")
    public TransactionResponse updateStatus(@PathVariable long transactionId,
                                            @PathVariable TransactionStatus status,
                                            @AuthenticationPrincipal EmployeeUserDetails userDetails) {
        return transactionConverter.transactionResponse(transactionService.changeStatus(transactionId, status, userDetails.getUser()));
    }
}
