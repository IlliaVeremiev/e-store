package ua.illia.estore.converters.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.illia.estore.converters.EntityConverter;
import ua.illia.estore.dto.invoice.InvoiceItemResponse;
import ua.illia.estore.dto.storeproduct.StoreProductResponse;
import ua.illia.estore.model.invoices.data.InvoiceItem;
import ua.illia.estore.services.product.ProductService;

@Component
public class InvoiceItemConverter implements EntityConverter<InvoiceItem, InvoiceItemResponse> {

    @Autowired
    private ProductConverter productConverter;

    @Autowired
    private ProductService productService;

    @Override
    public <E extends InvoiceItemResponse> E convert(InvoiceItem invoiceItem, E dto) {
        dto.setProduct(productConverter.convert(productService.getById(invoiceItem.getProductId()), new StoreProductResponse()));
        dto.setCount(invoiceItem.getCount());
        dto.setPrice(invoiceItem.getPrice());
        return dto;
    }

    @Override
    public InvoiceItemResponse convert(InvoiceItem invoiceItem) {
        return convert(invoiceItem, new InvoiceItemResponse());
    }
}
