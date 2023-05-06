package ua.illia.estore.converters.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.illia.estore.configuration.security.TypedUserDetails;
import ua.illia.estore.dto.order.OrderItemResponse;
import ua.illia.estore.dto.order.OrderResponse;
import ua.illia.estore.model.orders.Order;
import ua.illia.estore.model.orders.data.OrderItem;
import ua.illia.estore.services.order.OrderService;
import ua.illia.estore.services.product.ProductService;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Component
public class OrderConverter {

    @Autowired
    private ProductConverter productConverter;
    @Autowired
    private ProductService productService;

    @Autowired
    private CityConverter cityConverter;

    @Autowired
    private CustomerConverter customerConverter;

    @Autowired
    private EmployeeConverter employeeConverter;

    @Autowired
    private OrderService orderService;

    public OrderResponse orderResponse(Order order, TypedUserDetails userDetails) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setUid(order.getUid());
        response.setOrderItems(order.getItems().stream().map(this::orderItemResponse).collect(Collectors.toList()));
        response.setCustomer(customerConverter.convert(order.getCustomer()));
        if (order.getStatusUpdatedBy() != null) {
            response.setStatusUpdatedBy(employeeConverter.convert(order.getStatusUpdatedBy()));
        }
        response.setFirstName(order.getFirstName());
        response.setLastName(order.getLastName());
        response.setPatronymic(order.getPatronymic());
        response.setPhoneNumber(order.getPhoneNumber());
        response.setEmail(order.getEmail());
        response.setCreationDate(order.getCreationDate());
        response.setStatus(order.getStatus());
        response.setStatusUpdatedDate(order.getStatusUpdatedDate());
        response.setDeliveryMethod(order.getDeliveryMethod());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setDeliveryCity(cityConverter.convert(order.getDeliveryCity()));
        response.setDeliveryInformation(order.getDeliveryInformation());
        response.setDeliveryFirstName(order.getDeliveryFirstName());
        response.setDeliveryLastName(order.getDeliveryLastName());
        response.setDeliveryPatronymic(order.getDeliveryPatronymic());
        response.setDeliveryPhoneNumber(order.getDeliveryPhoneNumber());

        response.setTotalPrice(orderService.getTotalPrice(order));
        return response;
    }

    public OrderItemResponse orderItemResponse(OrderItem orderItem) {
        OrderItemResponse response = new OrderItemResponse();
        response.setProduct(productConverter.convert(productService.getById(orderItem.getProductId())));
        response.setPrice(orderItem.getPrice());
        response.setCount(orderItem.getCount());
        response.setTotalPrice(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getCount())));
        return response;
    }
}
