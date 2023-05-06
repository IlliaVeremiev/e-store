package ua.illia.estore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.illia.estore.model.security.StoreRole;

public interface StoreRoleRepository extends JpaRepository<StoreRole, Long> {
}
