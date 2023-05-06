package ua.illia.estore.processes.impl.order;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.illia.estore.model.orders.Order;
import ua.illia.estore.model.security.Customer;
import ua.illia.estore.services.order.OrderService;

@Component("prepareOrderProcessDelegateAction")
public class PrepareOrderProcessDelegateAction implements JavaDelegate {

    @Autowired
    private OrderService orderService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long orderId = (Long) execution.getVariable("orderId");
        Order order = orderService.getById(orderId);

        execution.setVariable("orderUid", order.getUid());
        execution.setVariable("recipientPhoneNumber", order.getPhoneNumber());
        execution.setVariable("recipientFullName", order.getLastName() + " " + order.getFirstName() + " " + (order.getPatronymic() != null ? order.getPatronymic() : ""));
        Customer customer = order.getCustomer();
        execution.setVariable("customerPhoneNumber", customer.getPhoneNumber());
        execution.setVariable("customerFullName", customer.getLastName() + " " + customer.getFirstName() + " " + (customer.getPatronymic() != null ? customer.getPatronymic() : ""));
        execution.setVariable("paymentMethod", order.getPaymentMethod());
    }
}
