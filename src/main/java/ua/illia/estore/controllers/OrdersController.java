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
import ua.illia.estore.configuration.security.CustomerUserDetails;
import ua.illia.estore.configuration.security.TypedUserDetails;
import ua.illia.estore.converters.impl.OrderConverter;
import ua.illia.estore.dto.order.OrderCreateForm;
import ua.illia.estore.dto.order.OrderResponse;
import ua.illia.estore.dto.order.OrderSearchForm;
import ua.illia.estore.services.order.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
public class OrdersController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderConverter orderConverter;

    @Transactional
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public OrderResponse create(@RequestBody OrderCreateForm form,
                                @AuthenticationPrincipal CustomerUserDetails userDetails) {
        return orderConverter.orderResponse(orderService.create(form, userDetails.getUser()), userDetails);
    }

    @Transactional
    @GetMapping("/{orderId}")
    public OrderResponse getById(@PathVariable long orderId,
                                 @AuthenticationPrincipal TypedUserDetails userDetails) {
        return orderConverter.orderResponse(orderService.getById(orderId), userDetails);
    }

    @Transactional
    @GetMapping
    public Page<OrderResponse> search(OrderSearchForm form,
                                      @PageableDefault Pageable pageable,
                                      @AuthenticationPrincipal TypedUserDetails userDetails) {
        if (TypedUserDetails.CUSTOMER_TYPE.equals(userDetails.getUserType())) {
            form.setCustomerId(((CustomerUserDetails) userDetails).getUser().getId());
        }
        return orderService.search(form, pageable)
                .map(o -> orderConverter.orderResponse(o, userDetails));

    }

    @Transactional
    @PostMapping("/{orderId}/processes/{processId}/message/{message}")
    public String sendOrderProcessMessage(@PathVariable long orderId,
                                          @PathVariable String processId,
                                          @PathVariable String message) {
        return orderService.sendProcessMessage(orderId, processId, message);
    }

    @Transactional
    @GetMapping("/{orderId}/processes")
    public List<Map<String, Object>> getOrderProcesses(@PathVariable long orderId,
                                                       HttpServletRequest request) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return orderService.getOrderProcesses(orderId);
    }
}
