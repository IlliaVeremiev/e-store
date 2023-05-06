package ua.illia.estore.services.product.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ua.illia.estore.configuration.exceptions.NotFoundException;
import ua.illia.estore.dto.category.CategoryClassificationParameterCreateForm;
import ua.illia.estore.dto.category.CategoryCreateForm;
import ua.illia.estore.dto.category.CategorySearchForm;
import ua.illia.estore.injectors.impl.CategoryInjector;
import ua.illia.estore.model.product.Category;
import ua.illia.estore.model.product.CategoryClassificationParameter;
import ua.illia.estore.model.product.Image;
import ua.illia.estore.model.product.enums.FilterPropertyType;
import ua.illia.estore.repositories.CategoryClassificationParameterRepository;
import ua.illia.estore.repositories.CategoryRepository;
import ua.illia.estore.search.SpecificationList;
import ua.illia.estore.services.media.ImageService;
import ua.illia.estore.services.product.CategoryService;
import ua.illia.estore.utils.ServiceUtils;
import ua.illia.estore.validation.validators.impl.CategoryValidator;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DefaultCategoryService implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryClassificationParameterRepository categoryClassificationParameterRepository;

    @Autowired
    private CategoryInjector categoryInjector;

    @Autowired
    private CategoryValidator categoryValidator;

    @Autowired
    private ImageService imageService;

    @Override
    public Category create(CategoryCreateForm form) {
        Category category = new Category();
        categoryInjector.inject(category, form);
        categoryValidator.validate(category);
        return categoryRepository.save(category);
    }

    @Override
    public Category getById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(ServiceUtils.notFound("Category", "id", id));
    }

    @Override
    public Page<Category> search(CategorySearchForm form, Pageable pageable) {
        SpecificationList<Category> specification = new SpecificationList<>();
        if (StringUtils.isNotEmpty(form.getQuery())) {
            specification.add((r, q, b) -> b.isTrue(b.function("@@", Boolean.class,
                    b.function("to_tsvector", Map.class, r.get("name")),
                    b.function("plainto_tsquery", Map.class, b.literal(form.getQuery()))
            )));
        }

        if (Objects.nonNull(form.getParentId())) {
            specification.add((r, q, b) -> b.equal(r.get("parent").get("id"), form.getParentId()));
        }

        if (Objects.nonNull(form.getFolder())) {
            specification.add((r, q, b) -> b.equal(r.get("folder"), form.getFolder()));
        }
        return categoryRepository.findAll(specification, pageable);
    }

    @Override
    public Category update(long id, Map<String, Object> data) {
        Category category = getById(id);
        categoryInjector.inject(category, data);
        categoryValidator.validate(category);
        return categoryRepository.save(category);
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category getByUid(String uid) {
        return categoryRepository.findByUid(uid)
                .orElseThrow(ServiceUtils.notFound("Category", "uid", uid));
    }

    @Override
    @Transactional
    public void deleteParameter(long categoryId, long parameterId) {
        Category category = getById(categoryId);
        CategoryClassificationParameter parameter = categoryClassificationParameterRepository.findById(parameterId)
                .orElseThrow(() -> new NotFoundException("Parameter not found", "parameter.id"));
        deleteParameter(category, parameter);
    }

    @Override
    public void deleteParameter(Category category, CategoryClassificationParameter categoryClassificationParameter) {
        List<CategoryClassificationParameter> classificationParameters = getClassificationParameters(category);
        if (classificationParameters.stream().filter(categoryClassificationParameter::equals).findAny().isEmpty()) {
            throw new NotFoundException("No parameter found for this category", "category.classificationParameter");
        }
        categoryClassificationParameterRepository.delete(categoryClassificationParameter);
        classificationParameters
                .removeIf(param -> param.getId() == categoryClassificationParameter.getId());
    }

    @Override
    public Category updateImage(long id, MultipartFile file) {
        Category category = getById(id);
        Image image = imageService.create(file);
        if (category.getImage() != null) {
            imageService.deleteImage(category.getImage().getId());
        }
        category.setImage(image);
        return categoryRepository.save(category);
    }

    @Override
    public Category updateIcon(long id, MultipartFile file) {
        Category category = getById(id);
        Image image = imageService.create(file);
        if (category.getIcon() != null) {
            imageService.deleteImage(category.getIcon().getId());
        }
        category.setIcon(image);
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public CategoryClassificationParameter createParameter(long categoryId, CategoryClassificationParameterCreateForm form) {
        Category category = getById(categoryId);
        CategoryClassificationParameter parameter = new CategoryClassificationParameter();
        parameter.setCategory(category);
        parameter.setLocalizedName(form.getLocalizedName());
        parameter.setKey(form.getKey());
        parameter.setType(form.getType());
        parameter.setMeasure(form.getMeasure());
        return categoryClassificationParameterRepository.save(parameter);
    }

    @Override
    public CategoryClassificationParameter createParameter(Category category, Map<String, String> localizedName, String key, FilterPropertyType type, String measure) {
        CategoryClassificationParameter parameter = new CategoryClassificationParameter();
        parameter.setCategory(category);
        parameter.setLocalizedName(localizedName);
        parameter.setKey(key);
        parameter.setType(type);
        parameter.setMeasure(measure);
        return categoryClassificationParameterRepository.save(parameter);
    }

    @Override
    public List<Category> getSubCategories(Category category) {
        if (!category.isFolder()) {
            return Collections.singletonList(category);
        }
        List<Category> allCategories = categoryRepository.findAll(new SpecificationList<>(), Pageable.ofSize(Integer.MAX_VALUE)).getContent();
        return getChildren(category, allCategories);
    }

    @Override
    public List<CategoryClassificationParameter> getClassificationParameters(Category category) {
        return categoryClassificationParameterRepository.findAllByCategory(category);
    }

    private List<Category> getChildren(Category category, List<Category> allCategories) {
        return allCategories.stream()
                .filter(cat -> cat.getParent() != null && cat.getParent().getId() == category.getId())
                .map(cat -> cat.isFolder() ? getChildren(cat, allCategories) : Collections.singletonList(cat))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
