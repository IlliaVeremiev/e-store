package ua.illia.estore.converters.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.illia.estore.converters.EntityConverter;
import ua.illia.estore.dto.category.CategoryClassificationParameterResponse;
import ua.illia.estore.dto.category.CategoryResponse;
import ua.illia.estore.model.product.Category;
import ua.illia.estore.model.product.CategoryClassificationParameter;
import ua.illia.estore.services.product.CategoryService;
import ua.illia.estore.utils.LocalizationUtil;

import java.util.stream.Collectors;

@Component
public class CategoryConverter implements EntityConverter<Category, CategoryResponse> {

    @Autowired
    private ImageConverter imageConverter;

    @Autowired
    private CategoryService categoryService;

    @Override
    public <E extends CategoryResponse> E convert(Category category, E dto) {
        dto.setId(category.getId());
        dto.setUid(category.getUid());
        dto.setName(LocalizationUtil.getLocalized(category.getLocalizedName()));
        dto.setLocalizedName(category.getLocalizedName());
        if (category.getImage() != null) {
            dto.setImage(imageConverter.convert(category.getImage()));
        }
        if (category.getIcon() != null) {
            dto.setIcon(imageConverter.convert(category.getIcon()));
        }
        if (category.getParent() != null) {
            dto.setParentId(category.getParent().getId());
        }
        dto.setFolder(category.isFolder());
        dto.setClassificationParameters(categoryService.getClassificationParameters(category)
                .stream()
                .map(this::categoryClassificationParameterResponse)
                .collect(Collectors.toList()));
        return dto;
    }

    private CategoryClassificationParameterResponse categoryClassificationParameterResponse(CategoryClassificationParameter categoryClassificationParameter) {
        CategoryClassificationParameterResponse ccpr = new CategoryClassificationParameterResponse();
        ccpr.setId(categoryClassificationParameter.getId());
        ccpr.setName(LocalizationUtil.getLocalized(categoryClassificationParameter.getLocalizedName(), null));
        ccpr.setKey(categoryClassificationParameter.getKey());
        ccpr.setType(categoryClassificationParameter.getType());
        ccpr.setMeasure(categoryClassificationParameter.getMeasure());
        return ccpr;
    }

    @Override
    public CategoryResponse convert(Category category) {
        return convert(category, new CategoryResponse());
    }

    public CategoryClassificationParameterResponse parameter(CategoryClassificationParameter parameter) {
        CategoryClassificationParameterResponse response = new CategoryClassificationParameterResponse();
        response.setId(parameter.getId());
        response.setType(parameter.getType());
        response.setName(LocalizationUtil.getLocalized(parameter.getLocalizedName()));
        response.setMeasure(parameter.getMeasure());
        response.setKey(parameter.getKey());
        return response;
    }
}
