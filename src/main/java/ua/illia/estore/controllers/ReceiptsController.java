package ua.illia.estore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.illia.estore.configuration.security.EmployeeUserDetails;
import ua.illia.estore.converters.impl.ReceiptConverter;
import ua.illia.estore.dto.receipts.ReceiptCreateForm;
import ua.illia.estore.dto.receipts.ReceiptResponse;
import ua.illia.estore.dto.receipts.ReceiptSearchForm;
import ua.illia.estore.services.store.ReceiptsService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/receipts")
public class ReceiptsController {

    @Autowired
    private ReceiptsService receiptService;

    @Autowired
    private ReceiptConverter receiptConverter;

    @Transactional
    @GetMapping
    public List<ReceiptResponse> search(ReceiptSearchForm form) {
        return receiptService.search(form).stream().map(receiptConverter::convert).collect(Collectors.toList());
    }

    @Transactional
    @PostMapping
    public ReceiptResponse create(ReceiptCreateForm form,
                                  @AuthenticationPrincipal EmployeeUserDetails userDetails) {
        return receiptConverter.convert(receiptService.create(form, userDetails.getUser()));
    }
}
