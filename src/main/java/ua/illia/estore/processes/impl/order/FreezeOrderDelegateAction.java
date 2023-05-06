package ua.illia.estore.processes.impl.order;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.illia.estore.model.orders.Order;
import ua.illia.estore.model.orders.enums.OrderStatus;
import ua.illia.estore.services.order.OrderService;

@Component("freezeOrderDelegateAction")
public class FreezeOrderDelegateAction implements JavaDelegate {

    @Autowired
    private OrderService orderService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long orderId = (Long) execution.getVariable("orderId");
        Order order = orderService.getById(orderId);
        order.setStatus(OrderStatus.FREEZED);
        orderService.update(order);
    }
}
