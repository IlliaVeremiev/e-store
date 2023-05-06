package ua.illia.estore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.illia.estore.model.security.StoreRoleEntry;

public interface StoreRoleEntryRepository extends JpaRepository<StoreRoleEntry, Long> {
}
