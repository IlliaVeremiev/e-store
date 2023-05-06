package ua.illia.estore.services.product.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.illia.estore.dto.brands.BrandCreateForm;
import ua.illia.estore.dto.brands.BrandSearchForm;
import ua.illia.estore.injectors.impl.BrandInjector;
import ua.illia.estore.model.product.Brand;
import ua.illia.estore.repositories.BrandRepository;
import ua.illia.estore.search.SpecificationList;
import ua.illia.estore.services.product.BrandService;
import ua.illia.estore.utils.ServiceUtils;
import ua.illia.estore.validation.validators.impl.BrandValidator;

import java.util.Map;

@Service
public class DefaultBrandService implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private BrandInjector brandInjector;

    @Autowired
    private BrandValidator brandValidator;

    @Override
    public Brand create(BrandCreateForm form) {
        Brand brand = new Brand();
        brandInjector.inject(brand, form);
        brandValidator.validate(brand);
        return brandRepository.save(brand);
    }

    @Override
    public Brand getById(long id) {
        return brandRepository.findById(id)
                .orElseThrow(ServiceUtils.notFound("Brand", "id", id));
    }

    @Override
    public Page<Brand> search(BrandSearchForm form, Pageable pageable) {
        SpecificationList<Brand> specification = new SpecificationList<>();
        if (StringUtils.isNotEmpty(form.getQuery())) {
            specification.add((r, q, b) -> b.isTrue(b.function("@@", Boolean.class,
                    b.function("to_tsvector", Map.class, r.get("name")),
                    b.function("plainto_tsquery", Map.class, b.literal(form.getQuery()))
            )));
        }
        return brandRepository.findAll(specification, pageable);
    }

    @Override
    public Brand update(long id, Map<String, Object> data) {
        Brand brand = getById(id);
        brandInjector.inject(brand, data);
        brandValidator.validate(brand);
        return brandRepository.save(brand);
    }

    @Override
    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Brand getByUid(String uid) {
        return brandRepository.findByUid(uid)
                .orElseThrow(ServiceUtils.notFound("Brand", "uid", uid));
    }
}
