package ua.illia.estore.services.order.impl;

import com.google.common.collect.ImmutableMap;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ua.illia.estore.configuration.SqlFunctionsContributor;
import ua.illia.estore.dto.order.OrderCreateForm;
import ua.illia.estore.dto.order.OrderSearchForm;
import ua.illia.estore.model.location.City;
import ua.illia.estore.model.orders.Order;
import ua.illia.estore.model.orders.data.OrderItem;
import ua.illia.estore.model.orders.enums.OrderStatus;
import ua.illia.estore.model.product.Product;
import ua.illia.estore.model.security.Customer;
import ua.illia.estore.repositories.OrderRepository;
import ua.illia.estore.search.SpecificationList;
import ua.illia.estore.services.location.CitiesService;
import ua.illia.estore.services.order.OrderService;
import ua.illia.estore.services.process.BusinessProcessService;
import ua.illia.estore.services.product.ProductService;
import ua.illia.estore.utils.ServiceUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DefaultOrderService implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BusinessProcessService businessProcessService;

    @Autowired
    private CitiesService citiesService;

    @Autowired
    private ProductService productService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    @Transactional
    public Order create(OrderCreateForm form, Customer customer) {
        Order order = new Order();
        order.setCreationDate(LocalDateTime.now());
        order.setCustomer(customer);
        order.setFirstName(form.getFirstName());
        order.setLastName(form.getLastName());
        order.setPatronymic(form.getPatronymic());
        order.setPhoneNumber(form.getPhoneNumber());
        order.setEmail(form.getEmail());
        City deliveryCity = citiesService.getById(form.getDeliveryCity());
        order.setDeliveryCity(deliveryCity);
        order.setDeliveryMethod(form.getDeliveryMethod());
        order.setPaymentMethod(form.getPaymentMethod());
        order.setDeliveryInformation(form.getDeliveryInformation());
        order.setStatus(OrderStatus.NEW);
        order.setDeliveryFirstName(form.getDeliveryFirstName());
        order.setDeliveryLastName(form.getDeliveryLastName());
        order.setDeliveryPatronymic(form.getDeliveryPatronymic());
        order.setDeliveryPhoneNumber(form.getDeliveryPhoneNumber());
        LocalDate now = LocalDate.now();
        long count = orderRepository.countByCreationDateBetween(now.atStartOfDay(), now.atTime(LocalTime.MAX));
        order.setUid(LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyy")) + "-" + String.format("%06d", orderOrdinalNumberToOrderIdNumber((int) count)));

        List<Product> products = productService.getByIds(form.getOrderItems()
                .entrySet()
                .stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList()));
        order.setItems(form.getOrderItems()
                .entrySet()
                .stream()
                .map(e -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductId(e.getKey());
                    orderItem.setCount(e.getValue());
                    orderItem.setPrice(products.stream().filter(p -> p.getId() == e.getKey()).findFirst().map(Product::getPrice).orElse(BigDecimal.ZERO));
                    return orderItem;
                })
                .collect(Collectors.toList()));

        orderRepository.save(order);
        businessProcessService.start("orderCreated", order.getUid(), ImmutableMap.of("orderId", order.getId()));
        return order;
    }

    private int orderOrdinalNumberToOrderIdNumber(int input) {
        Asserts.check(input < 1000000, "Value larger than 1000000 will be repetitive");
        return 31 * (input + 33) % 1000000;
    }

    @Override
    public Order getById(long id) {
        return orderRepository.findById(id)
                .orElseThrow(ServiceUtils.notFound("Order", "id", id));
    }

    @Override
    public Page<Order> search(OrderSearchForm searchForm, Pageable pageable) {
        SpecificationList<Order> specification = new SpecificationList<>();

        if (searchForm.getCustomerId() != null) {
            specification.add((r, q, cb) -> cb.equal(r.get("customer").get("id"), searchForm.getCustomerId()));
        }
        if (searchForm.getOrderUid() != null) {
            specification.add((r, q, cb) -> cb.equal(r.get("uid"), searchForm.getOrderUid()));
        }
        if (searchForm.getDate() != null) {
            specification.add((r, q, cb) -> cb.equal(cb.function(SqlFunctionsContributor.DATE_TRUNC, LocalDate.class, cb.literal("day"), r.get("creationDate")), searchForm.getDate()));
        }
        if (searchForm.getPhoneNumber() != null) {
            specification.add((r, q, cb) -> cb.equal(r.get("phoneNumber"), searchForm.getPhoneNumber()));
        }
        Pageable pageableRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.asc("creationDate")).and(pageable.getSort()));

        return orderRepository.findAll(specification, pageableRequest);
    }

    @Override
    public List<Order> getWithProduct(Product product) {
        Specification<Order> specification = (r, q, b) -> b.equal(b.function("json_extract_path_text", String.class,
                r.get("items"), b.literal("productId")), product.getId());
        return orderRepository.findAll(specification);
    }

    @Override
    public BigDecimal getTotalPrice(Order order) {
        return order.getItems()
                .stream()
                .reduce(BigDecimal.ZERO, (subtotal, item) -> subtotal.add(item.getPrice().multiply(BigDecimal.valueOf(item.getCount()))), BigDecimal::add);
    }

    @Override
    public List<Order> getAllByCustomerAndDate(Customer customer, LocalDate date) {
        return orderRepository.findAllByCustomerAndCreationDateBetween(customer, date.atStartOfDay(), date.atTime(LocalTime.MAX));
    }

    @Override
    public Order update(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public String sendProcessMessage(long orderId, String processId, String message) {
        Order order = getById(orderId);
        return businessProcessService.processMessage(processId, order.getUid(), message);
    }

    @Override
    public List<Map<String, Object>> getOrderProcesses(long orderId) {
        Order order = getById(orderId);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String serverAddress = request.getRequestURL().toString().replace(request.getRequestURI(), "");

        List<Map<String, Object>> instances = restTemplate.getForObject(serverAddress + "/engine-rest/process-instance?businessKey=" + order.getUid(), List.class);

        return instances.parallelStream()
                .peek(i -> i.put("definition", restTemplate.getForObject(serverAddress + "/engine-rest/process-definition/" + i.get("definitionId"), Map.class)))
                .peek(i -> i.put("comments", restTemplate.getForObject(serverAddress + "/engine-rest/process-instance/" + i.get("id") + "/comment", List.class)))
                .peek(i -> i.put("tasks", restTemplate.getForObject(serverAddress + "/engine-rest/task?processInstanceId=" + i.get("id"), List.class)))
                .collect(Collectors.toList());
    }
}
