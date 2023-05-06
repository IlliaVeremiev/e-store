package ua.illia.estore.processes.impl.order;

import com.google.common.collect.ImmutableMap;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.illia.estore.converters.impl.OrderConverter;
import ua.illia.estore.model.orders.Order;
import ua.illia.estore.services.management.EmailService;
import ua.illia.estore.services.management.TemplateService;
import ua.illia.estore.services.order.OrderService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Collectors;

@Component("notifyCustomerOrderCreatedProcessAction")
public class NotifyCustomerOrderCreatedProcessAction implements JavaDelegate {

    @Autowired
    private EmailService emailService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderConverter orderConverter;

    @Value("${application:back-office:url}")
    private String backofficeUrl;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long orderId = (Long) execution.getVariable("orderId");
        Order order = orderService.getById(orderId);

        emailService.sendTemplateMail("openspace.in.ua@gmail.com", "OpenSpace", Collections.singletonList(order.getEmail()), "Ваш заказ №" + order.getUid() + " принят" + LocalDateTime.now(),
                templateService.buildTemplate("email/customer-notification-order-created.flth",
                        ImmutableMap.of(
                                "orderUrl", backofficeUrl + "/back-office/stores/site/orders/" + orderId,
                                "order", orderConverter.orderResponse(order, null),
                                "items", order.getItems().stream().map(orderConverter::orderItemResponse).collect(Collectors.toList())
                        )
                )
        );
    }
}
