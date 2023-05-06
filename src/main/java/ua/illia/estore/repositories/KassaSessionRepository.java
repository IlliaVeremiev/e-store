package ua.illia.estore.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.model.store.KassaSession;
import ua.illia.estore.model.store.Store;

import java.util.List;
import java.util.Optional;

public interface KassaSessionRepository extends JpaRepository<KassaSession, Long> {

    List<KassaSession> findAllByStoreAndEmployee(Store store, Employee employee);

    List<KassaSession> findAll(Specification<KassaSession> specification);

    Optional<KassaSession> findByUuid(String uuid);
}
