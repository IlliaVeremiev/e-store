package ua.illia.estore.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.model.security.Group;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    List<Group> findAll(Specification<Group> specification, Pageable pageable);

    List<Group> findAllByEmployees(Employee employee);

    Optional<Group> findByName(String name);

    Optional<Group> findByUid(String uid);
}
