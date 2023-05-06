package ua.illia.estore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.illia.estore.model.product.ProductVariant;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
}
