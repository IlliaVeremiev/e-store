package ua.illia.estore.services.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.web.multipart.MultipartFile;
import ua.illia.estore.dto.category.CategoryClassificationParameterCreateForm;
import ua.illia.estore.dto.category.CategoryCreateForm;
import ua.illia.estore.dto.category.CategorySearchForm;
import ua.illia.estore.model.product.Category;
import ua.illia.estore.model.product.CategoryClassificationParameter;
import ua.illia.estore.model.product.enums.FilterPropertyType;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    Category create(CategoryCreateForm form);

    Category getById(long id);

    Page<Category> search(CategorySearchForm form, Pageable pageable);

    Category update(long id, Map<String, Object> data);

    Category save(Category category);

    Category getByUid(String uid);

    void deleteParameter(long categoryId, long parameterId);

    void deleteParameter(Category category, CategoryClassificationParameter categoryClassificationParameter);

    Category updateImage(long id, MultipartFile image);

    Category updateIcon(long id, MultipartFile image);

    CategoryClassificationParameter createParameter(long categoryId, CategoryClassificationParameterCreateForm form);

    CategoryClassificationParameter createParameter(Category category, Map<String, String> localizedName, String key, FilterPropertyType type, String measure);

    @EntityGraph(Category.Graph.BASIC)
    List<Category> getSubCategories(Category category);

    List<CategoryClassificationParameter> getClassificationParameters(Category category);
}
