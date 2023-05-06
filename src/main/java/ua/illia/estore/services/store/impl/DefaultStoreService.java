package ua.illia.estore.services.store.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.illia.estore.configuration.exceptions.ValidationException;
import ua.illia.estore.dto.kassasession.KassaSessionCreateForm;
import ua.illia.estore.dto.kassasession.KassaSessionSearchForm;
import ua.illia.estore.dto.store.StoreCreateForm;
import ua.illia.estore.dto.store.StoreSearchForm;
import ua.illia.estore.injectors.impl.StoreInjector;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.model.store.KassaSession;
import ua.illia.estore.model.store.Store;
import ua.illia.estore.model.store.enums.KassaSessionState;
import ua.illia.estore.repositories.KassaSessionRepository;
import ua.illia.estore.repositories.StoreRepository;
import ua.illia.estore.search.SpecificationList;
import ua.illia.estore.services.management.EmployeeService;
import ua.illia.estore.services.store.StoreService;
import ua.illia.estore.utils.ServiceUtils;
import ua.illia.estore.validation.validators.impl.StoreValidator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class DefaultStoreService implements StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreInjector storeInjector;

    @Autowired
    private StoreValidator storeValidator;

    @Override
    public Store create(StoreCreateForm form) {
        Store store = new Store();
        storeInjector.inject(store, form);
        storeValidator.validate(store);
        store.setCreationDate(LocalDateTime.now());
        return storeRepository.save(store);
    }

    @Override
    public Store getById(long id) {
        return storeRepository.findById(id)
                .orElseThrow(ServiceUtils.notFound("Store", "id", id));
    }

    @Override
    public Page<Store> search(StoreSearchForm form, Pageable pageable) {
        SpecificationList<Store> specification = new SpecificationList<>();
        if (StringUtils.isNotEmpty(form.getQuery())) {
            specification.add((r, q, b) -> b.isTrue(b.function("@@", Boolean.class, b.function("to_tsvector", Map.class, r.get("name")), b.function("plainto_tsquery", Map.class, b.literal(form.getQuery())))));
        }
        return storeRepository.findAll(specification, pageable);
    }

    @Override
    public Store update(long id, Map<String, Object> data) {
        Store store = getById(id);
        storeInjector.inject(store, data);
        storeValidator.validate(store);
        storeRepository.save(store);
        return storeRepository.getById(store.getId());
    }

    @Override
    public Store getByUid(String uid) {
        return storeRepository.findByUid(uid)
                .orElseThrow(ServiceUtils.notFound("Store", "uid", uid));
    }

    @Override
    public Store save(Store store) {
        return storeRepository.save(store);
    }

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private KassaSessionRepository kassaSessionRepository;

    @Override
    public List<KassaSession> getOpenKassaForEmployee(long storeId, String email) {
        Store store = storeService.getById(storeId);
        Employee employee = employeeService.getByEmail(email);
        return kassaSessionRepository.findAllByStoreAndEmployee(store, employee);
    }

    @Override
    public KassaSession createSession(long storeId, Employee employee) {
        Store store = storeService.getById(storeId);
        KassaSession session = new KassaSession();
        session.setDate(LocalDate.now());
        session.setCreatedBy(employee);
        session.setEmployee(employee);
        session.setCreationDate(LocalDateTime.now());
        session.setStore(store);
        session.setState(KassaSessionState.OPEN);
        return kassaSessionRepository.save(session);
    }

    @Override
    public KassaSession getSession(long id) {
        return kassaSessionRepository.getById(id);
    }

    @Override
    public List<KassaSession> searchSession(KassaSessionSearchForm form) {
        SpecificationList<KassaSession> specification = new SpecificationList<>();
        if (form.getState() != null) {
            specification.add((r, q, b) -> b.equal(r.get("state"), form.getState()));
        }
        if (form.getEmail() != null) {
            specification.add((r, q, b) -> b.equal(r.get("employee").get("email"), form.getEmail()));
        }
        return kassaSessionRepository.findAll(specification);
    }

    @Override
    public KassaSession createSession(KassaSessionCreateForm form, Employee employee) {
        if (!Objects.equals(employee.getEmail(), form.getEmail())) {
            throw new ValidationException("You have not access to create session for employee: " + form.getEmail(), "session.employee");
        }
        Store store = storeService.getById(form.getStoreId());
        KassaSession session = new KassaSession();
        session.setDate(LocalDate.now());
        session.setCreatedBy(employee);
        session.setEmployee(employee);
        session.setCreationDate(LocalDateTime.now());
        session.setStore(store);
        session.setState(KassaSessionState.OPEN);
        return kassaSessionRepository.save(session);
    }
}
