package ua.illia.estore.converters.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.illia.estore.dto.receipts.ReceiptItemResponse;
import ua.illia.estore.dto.receipts.ReceiptResponse;
import ua.illia.estore.model.store.Receipt;
import ua.illia.estore.model.store.data.ReceiptItem;

import java.util.stream.Collectors;

@Component
public class ReceiptConverter {

    @Autowired
    private StoreConverter storeConverter;

    @Autowired
    private EmployeeConverter employeeConverter;

    public ReceiptResponse convert(Receipt receipt) {
        ReceiptResponse response = new ReceiptResponse();
        response.setId(receipt.getId());
        response.setKassaSessionId(receipt.getKassaSession().getId());
        response.setUuid(receipt.getUuid());
        response.setNumber(receipt.getNumber());
        response.setItems(receipt.getItems().stream().map(this::convertItem).collect(Collectors.toList()));
        response.setStore(storeConverter.storeResponse(receipt.getStore()));
        response.setEmployee(employeeConverter.convert(receipt.getEmployee()));
        response.setCreationDate(receipt.getCreationDate());
        response.setTotal(receipt.getTotal());
        response.setPaymentType(receipt.getPaymentType());
        return response;
    }

    private ReceiptItemResponse convertItem(ReceiptItem receiptItem) {
        ReceiptItemResponse response = new ReceiptItemResponse();
        response.setProductId(receiptItem.getProductId());
        response.setCount(receiptItem.getCount());
        response.setBasePrice(receiptItem.getBasePrice());
        response.setDiscount(receiptItem.getDiscount());
        response.setTotalPrice(receiptItem.getTotalPrice());
        return response;
    }
}
