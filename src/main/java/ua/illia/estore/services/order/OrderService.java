package ua.illia.estore.services.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.illia.estore.dto.order.OrderCreateForm;
import ua.illia.estore.dto.order.OrderSearchForm;
import ua.illia.estore.model.orders.Order;
import ua.illia.estore.model.product.Product;
import ua.illia.estore.model.security.Customer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OrderService {

    Order create(OrderCreateForm form, Customer customer);

    Order getById(long orderId);

    Page<Order> search(OrderSearchForm searchForm, Pageable pageable);

    List<Order> getWithProduct(Product product);

    BigDecimal getTotalPrice(Order order);

    List<Order> getAllByCustomerAndDate(Customer customer, LocalDate date);

    Order update(Order order);

    String sendProcessMessage(long orderId, String processId, String message);

    List<Map<String, Object>> getOrderProcesses(long orderId);
}
