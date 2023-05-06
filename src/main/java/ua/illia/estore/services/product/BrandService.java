package ua.illia.estore.services.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.illia.estore.dto.brands.BrandCreateForm;
import ua.illia.estore.dto.brands.BrandSearchForm;
import ua.illia.estore.model.product.Brand;

import java.util.Map;

public interface BrandService {

    Brand create(BrandCreateForm form);

    Brand getById(long id);

    Page<Brand> search(BrandSearchForm form, Pageable pageable);

    Brand update(long id, Map<String, Object> data);

    Brand save(Brand brand);


    Brand getByUid(String uid);
}
