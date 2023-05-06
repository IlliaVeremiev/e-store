package ua.illia.estore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.illia.estore.model.product.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
