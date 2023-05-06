package ua.illia.estore.services.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ua.illia.estore.dto.product.ProductCreateForm;
import ua.illia.estore.dto.product.ProductSearchForm;
import ua.illia.estore.model.product.Image;
import ua.illia.estore.model.product.Product;
import ua.illia.estore.model.security.Employee;

import java.util.List;
import java.util.Map;

public interface ProductService {

    Product create(ProductCreateForm form, Employee employee);

    Product getById(long id);

    Page<Product> search(ProductSearchForm form, Pageable pageable);

    Product update(long id, Map<String, Object> data, Employee employee);

    void delete(long id);

    Product save(Product product);

    List<Image> addImages(long productId, List<MultipartFile> images);

    List<Image> deleteImage(long productId, long imageId);

    List<Product> getByIds(List<Long> ids);
}
