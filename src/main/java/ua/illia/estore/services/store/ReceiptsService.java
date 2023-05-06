package ua.illia.estore.services.store;

import ua.illia.estore.dto.receipts.ReceiptCreateForm;
import ua.illia.estore.dto.receipts.ReceiptSearchForm;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.model.store.Receipt;

import java.util.List;

public interface ReceiptsService {

    List<Receipt> search(ReceiptSearchForm form);

    Receipt create(ReceiptCreateForm form, Employee employee);
}
