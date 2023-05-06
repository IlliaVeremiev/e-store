package ua.illia.estore.converters.impl;

import org.springframework.stereotype.Component;
import ua.illia.estore.converters.EntityConverter;
import ua.illia.estore.dto.brands.BrandResponse;
import ua.illia.estore.model.product.Brand;

@Component
public class BrandConverter implements EntityConverter<Brand, BrandResponse> {

    @Override
    public <E extends BrandResponse> E convert(Brand brand, E dto) {
        dto.setId(brand.getId());
        dto.setUid(brand.getUid());
        dto.setName(brand.getName());
        return dto;
    }

    @Override
    public BrandResponse convert(Brand brand) {
        return convert(brand, new BrandResponse());
    }
}
