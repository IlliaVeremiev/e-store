package ua.illia.estore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.illia.estore.configuration.exceptions.UnauthorizedException;
import ua.illia.estore.configuration.security.CustomerUserDetails;
import ua.illia.estore.configuration.security.TypedUserDetails;
import ua.illia.estore.converters.impl.CustomerConverter;
import ua.illia.estore.dto.customer.CustomerCreateForm;
import ua.illia.estore.dto.customer.CustomerResponse;
import ua.illia.estore.dto.customer.CustomerSearchForm;
import ua.illia.estore.services.management.CustomerService;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomersController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerConverter customerConverter;

    @PostMapping
    public CustomerResponse create(@RequestBody CustomerCreateForm customerCreateForm) {
        return customerConverter.convert(customerService.create(customerCreateForm));
    }

    @GetMapping("/{customerId}")
    public CustomerResponse getById(@PathVariable long customerId,
                                    @AuthenticationPrincipal TypedUserDetails userDetails) {
        return customerConverter.convert(customerService.getById(customerId));
    }

    @GetMapping("/me")
    public CustomerResponse getCurrentCustomer(@AuthenticationPrincipal CustomerUserDetails userDetails) {
        if (userDetails != null) {
            return customerConverter.convert(userDetails.getUser());
        }
        throw new UnauthorizedException("User not authorized");
    }

    @GetMapping
    public Page<CustomerResponse> search(CustomerSearchForm customerSearchForm,
                                         @PageableDefault(size = 24) Pageable pageable) {
        return customerService.search(customerSearchForm, pageable)
                .map(customerConverter::convert);
    }
}
