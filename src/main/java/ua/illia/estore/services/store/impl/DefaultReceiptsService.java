package ua.illia.estore.services.store.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ua.illia.estore.dto.receipts.ReceiptCreateForm;
import ua.illia.estore.dto.receipts.ReceiptSearchForm;
import ua.illia.estore.model.product.Product;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.model.store.Receipt;
import ua.illia.estore.model.store.data.ReceiptItem;
import ua.illia.estore.repositories.ReceiptsRepository;
import ua.illia.estore.search.SpecificationList;
import ua.illia.estore.services.product.ProductService;
import ua.illia.estore.services.store.ReceiptsService;
import ua.illia.estore.services.store.StoreService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultReceiptsService implements ReceiptsService {

    @Autowired
    private ReceiptsRepository receiptsRepository;

    @Override
    public List<Receipt> search(ReceiptSearchForm form) {
        SpecificationList<Receipt> specification = new SpecificationList<>();
        if (form.getStoreId() != null) {
            specification.add((r, q, b) -> b.equal(r.get("store").get("id"), form.getStoreId()));
        }
        return receiptsRepository.findAll(specification, PageRequest.of(0, 1000));
    }

    @Autowired
    private StoreService storeService;

    @Autowired
    private ProductService productService;

    @Override
    public Receipt create(ReceiptCreateForm form, Employee employee) {
        Receipt receipt = new Receipt();
        receipt.setUuid(form.getUuid());
        receipt.setNumber(form.getNumber());
        receipt.setKassaSession(storeService.getSession(form.getKassaSessionId()));
        receipt.setItems(form.getItems().stream().map(item -> {
            Product product = productService.getById(item.getProductId());
            ReceiptItem receiptItem = new ReceiptItem();
            receiptItem.setProductId(item.getProductId());
            receiptItem.setCount(item.getCount());
            receiptItem.setBasePrice(product.getPrice());
            receiptItem.setTotalPrice(product.getPrice());
            return receiptItem;
        }).collect(Collectors.toList()));
        receipt.setStore(storeService.getById(form.getStoreId()));
        receipt.setEmployee(employee);
        receipt.setCreationDate(form.getCreationDate());
        receipt.setTotal(receipt.getItems().stream().map(ReceiptItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
        receipt.setPaymentType(form.getPaymentType());
        return receiptsRepository.save(receipt);
    }
}
