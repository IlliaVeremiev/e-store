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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.illia.estore.configuration.exceptions.UnauthorizedException;
import ua.illia.estore.configuration.security.EmployeeUserDetails;
import ua.illia.estore.converters.impl.EmployeeConverter;
import ua.illia.estore.converters.impl.PaymentAccountConverter;
import ua.illia.estore.dto.employee.EmployeeCreateForm;
import ua.illia.estore.dto.employee.EmployeeResponse;
import ua.illia.estore.dto.employee.EmployeeSearchForm;
import ua.illia.estore.dto.paymentaccount.PaymentAccountCreateForm;
import ua.illia.estore.dto.paymentaccount.PaymentAccountResponse;
import ua.illia.estore.services.management.EmployeeService;
import ua.illia.estore.services.management.PaymentAccountService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeConverter employeeConverter;

    @Autowired
    private PaymentAccountService paymentAccountService;

    @Autowired
    private PaymentAccountConverter paymentAccountConverter;

    @Transactional
    @PostMapping
    public EmployeeResponse create(@RequestBody EmployeeCreateForm form) {
        return employeeConverter.convert(employeeService.create(form));
    }

    @Transactional
    @GetMapping
    public Page<EmployeeResponse> search(EmployeeSearchForm form,
                                         @PageableDefault(size = 100) Pageable pageable) {
        return employeeService.search(form, pageable)
                .map(employeeConverter::convert);
    }

    @Transactional
    @GetMapping("/{employeeId}")
    public EmployeeResponse getById(@PathVariable long employeeId) {
        return employeeConverter.convert(employeeService.getById(employeeId));
    }

    @Transactional
    @GetMapping("/me")
    public EmployeeResponse getCurrentEmployee(@AuthenticationPrincipal EmployeeUserDetails userDetails) {
        if (userDetails != null) {
            return employeeConverter.convert(employeeService.getById(userDetails.getUser().getId()));
        }
        throw new UnauthorizedException("User not authorized");
    }

    @Transactional
    @PutMapping("/{employeeId}")
    public EmployeeResponse update(@PathVariable long employeeId,
                                   @RequestBody Map<String, Object> data) {
        return employeeConverter.convert(employeeService.update(employeeId, data));
    }

    @Transactional
    @PostMapping("/{employeeId}/payment-accounts")
    @PreAuthorize("isAuthenticated()")
    public PaymentAccountResponse createPaymentAccount(@PathVariable long employeeId,
                                                       @RequestBody PaymentAccountCreateForm form,
                                                       @AuthenticationPrincipal EmployeeUserDetails userDetails) {
        return paymentAccountConverter.paymentAccountResponse(paymentAccountService.create(form, employeeService.getById(employeeId), userDetails.getUser()));
    }
}
