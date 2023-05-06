package ua.illia.estore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.illia.estore.converters.impl.BrandConverter;
import ua.illia.estore.dto.brands.BrandCreateForm;
import ua.illia.estore.dto.brands.BrandResponse;
import ua.illia.estore.dto.brands.BrandSearchForm;
import ua.illia.estore.services.product.BrandService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/brands")
public class BrandsController {

    @Autowired
    private BrandService brandService;

    @Autowired
    private BrandConverter brandConverter;

    @PostMapping
    public BrandResponse create(@RequestBody BrandCreateForm brandCreateForm) {
        return brandConverter.convert(brandService.create(brandCreateForm));
    }

    @GetMapping("/{brandId}")
    public BrandResponse getBrandById(@PathVariable long brandId) {
        return brandConverter.convert(brandService.getById(brandId));
    }

    @GetMapping
    public Page<BrandResponse> search(BrandSearchForm brandSearchForm,
                                      @PageableDefault(size = 100) Pageable pageable) {
        return brandService.search(brandSearchForm, pageable)
                .map(brandConverter::convert);
    }

    @PutMapping("/{brandId}")
    public BrandResponse update(@PathVariable long brandId,
                                Map<String, Object> data) {
        return brandConverter.convert(brandService.update(brandId, data));
    }
}
