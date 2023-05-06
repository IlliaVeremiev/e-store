package ua.illia.estore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.model.security.StoreRoleEntry;

import java.util.List;

public interface StoreRoleEntriesRepository extends JpaRepository<StoreRoleEntry, Long> {

    List<StoreRoleEntry> findAllByEmployee(Employee employee);
}
