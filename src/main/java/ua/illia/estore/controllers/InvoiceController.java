package ua.illia.estore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.illia.estore.configuration.security.EmployeeUserDetails;
import ua.illia.estore.converters.impl.InvoiceConverter;
import ua.illia.estore.dto.invoice.InvoiceCreateForm;
import ua.illia.estore.dto.invoice.InvoiceResponse;
import ua.illia.estore.dto.invoice.InvoiceSearchForm;
import ua.illia.estore.model.invoices.emuns.InvoiceStatus;
import ua.illia.estore.services.invoice.InvoiceService;

@RestController
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceConverter invoiceConverter;

    @Transactional
    @PostMapping
    public InvoiceResponse create(@RequestBody InvoiceCreateForm form,
                                  @AuthenticationPrincipal EmployeeUserDetails userDetails) {
        return invoiceConverter.convert(invoiceService.create(form, userDetails.getUser()));
    }

    @Transactional
    @PostMapping("/{id}")
    public InvoiceResponse update(@PathVariable long id,
                                  @RequestBody InvoiceCreateForm form,
                                  @AuthenticationPrincipal EmployeeUserDetails userDetails) {
        return invoiceConverter.convert(invoiceService.update(id, form, userDetails.getUser()));
    }

    @Transactional
    @GetMapping
    public Page<InvoiceResponse> search(InvoiceSearchForm form,
                                        @PageableDefault(size = 100) Pageable pageable) {
        return invoiceService.search(form, pageable)
                .map(invoiceConverter::convert);
    }

    @Transactional
    @GetMapping("/{invoiceId}")
    public InvoiceResponse getInvoiceById(@PathVariable long invoiceId) {
        return invoiceConverter.convert(invoiceService.getById(invoiceId));
    }

    @Transactional
    @PostMapping("/{id}/status/{status}")
    public InvoiceResponse setStatus(@PathVariable long id,
                                     @PathVariable InvoiceStatus status,
                                     @AuthenticationPrincipal EmployeeUserDetails userDetails) {
        return invoiceConverter.convert(invoiceService.setStatus(id, status, userDetails.getUser()));
    }
}
