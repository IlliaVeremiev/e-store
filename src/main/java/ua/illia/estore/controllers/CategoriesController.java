package ua.illia.estore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ua.illia.estore.converters.impl.CategoryConverter;
import ua.illia.estore.dto.category.CategoryClassificationParameterCreateForm;
import ua.illia.estore.dto.category.CategoryClassificationParameterResponse;
import ua.illia.estore.dto.category.CategoryCreateForm;
import ua.illia.estore.dto.category.CategoryResponse;
import ua.illia.estore.dto.category.CategorySearchForm;
import ua.illia.estore.services.product.CategoryService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoriesController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryConverter categoryConverter;

    @Transactional
    @PostMapping
    public CategoryResponse createCategory(@RequestBody CategoryCreateForm form) {
        return categoryConverter.convert(categoryService.create(form));
    }

    @Transactional
    @GetMapping("/{categoryId}")
    public CategoryResponse getById(@PathVariable long categoryId) {
        return categoryConverter.convert(categoryService.getById(categoryId));
    }

    @Transactional
    @GetMapping
    public Page<CategoryResponse> search(CategorySearchForm categorySearchForm,
                                         @PageableDefault(size = 100) Pageable pageable) {
        return categoryService.search(categorySearchForm, pageable).map(categoryConverter::convert);
    }

    @Transactional
    @PutMapping("/{categoryId}")
    public CategoryResponse update(@PathVariable long categoryId,
                                   @RequestBody Map<String, Object> data) {
        return categoryConverter.convert(categoryService.update(categoryId, data));
    }

    @Transactional
    @PutMapping("/{categoryId}/image")
    public CategoryResponse updateImage(@PathVariable long categoryId,
                                        @RequestPart MultipartFile image) {
        return categoryConverter.convert(categoryService.updateImage(categoryId, image));
    }

    @Transactional
    @PutMapping("/{categoryId}/icon")
    public CategoryResponse updateIcon(@PathVariable long categoryId,
                                       @RequestPart MultipartFile image) {
        return categoryConverter.convert(categoryService.updateIcon(categoryId, image));
    }

    @DeleteMapping("/{categoryId}/parameters/{parameterId}")
    public void deleteParameter(@PathVariable long categoryId,
                                @PathVariable long parameterId) {
        categoryService.deleteParameter(categoryId, parameterId);
    }

    @PostMapping("/{categoryId}/parameters")
    public CategoryClassificationParameterResponse createParameter(@PathVariable long categoryId,
                                                                   @RequestBody CategoryClassificationParameterCreateForm form) {
        return categoryConverter.parameter(categoryService.createParameter(categoryId, form));
    }
}
