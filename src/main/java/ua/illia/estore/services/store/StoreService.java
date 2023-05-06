package ua.illia.estore.services.store;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.illia.estore.dto.kassasession.KassaSessionCreateForm;
import ua.illia.estore.dto.kassasession.KassaSessionSearchForm;
import ua.illia.estore.dto.store.StoreCreateForm;
import ua.illia.estore.dto.store.StoreSearchForm;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.model.store.KassaSession;
import ua.illia.estore.model.store.Store;

import java.util.List;
import java.util.Map;

public interface StoreService {

    Store create(StoreCreateForm form);

    Store getById(long id);

    Page<Store> search(StoreSearchForm form, Pageable pageable);

    Store update(long storeId, Map<String, Object> data);

    Store getByUid(String uid);

    Store save(Store store);

    List<KassaSession> getOpenKassaForEmployee(long storeId, String email);

    KassaSession createSession(long storeId, Employee user);

    KassaSession getSession(long id);

    List<KassaSession> searchSession(KassaSessionSearchForm form);

    KassaSession createSession(KassaSessionCreateForm form, Employee employee);
}
