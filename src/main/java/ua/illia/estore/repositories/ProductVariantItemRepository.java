package ua.illia.estore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.illia.estore.model.product.Product;
import ua.illia.estore.model.product.ProductVariantItem;

public interface ProductVariantItemRepository extends JpaRepository<ProductVariantItem, Long> {

    ProductVariantItem findByProduct(Product product);
}
