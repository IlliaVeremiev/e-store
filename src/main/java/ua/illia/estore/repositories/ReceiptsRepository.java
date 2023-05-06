package ua.illia.estore.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.illia.estore.model.store.Receipt;

import java.util.List;

public interface ReceiptsRepository extends JpaRepository<Receipt, Long> {

    List<Receipt> findAll(Specification<Receipt> specification, Pageable pageable);
}
