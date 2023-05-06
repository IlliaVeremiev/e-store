package ua.illia.estore.processes.impl.order;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.illia.estore.model.orders.Order;
import ua.illia.estore.model.security.Customer;
import ua.illia.estore.services.management.CustomerService;
import ua.illia.estore.services.order.OrderService;

import java.time.LocalDate;
import java.util.List;

@Component("fraudCheckProcessAction")
public class FraudCheckProcessAction implements JavaDelegate {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long orderId = (Long) execution.getVariable("orderId");
        Order order = orderService.getById(orderId);
        long customerId = order.getCustomer().getId();
        Customer customer = customerService.getById(customerId);
        List<Order> orders = orderService.getAllByCustomerAndDate(customer, LocalDate.now());
        execution.setVariable("fraudFails", orders.size() >= 1000);
    }
}
