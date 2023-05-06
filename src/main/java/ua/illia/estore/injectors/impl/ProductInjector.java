package ua.illia.estore.injectors.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.illia.estore.injectors.EntityDataInjector;
import ua.illia.estore.model.product.Brand;
import ua.illia.estore.model.product.Category;
import ua.illia.estore.model.product.Product;
import ua.illia.estore.services.product.BrandService;
import ua.illia.estore.services.product.CategoryService;
import ua.illia.estore.services.store.StoreProductService;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Component
public class ProductInjector extends EntityDataInjector<Product> {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private StoreProductService storeProductService;

    public ProductInjector() {
        field("localizedName", Product::setLocalizedName);
        field("brand", this::setBrand);
        field("category", this::setCategory);
        field("imagesOrder", this::setImagesOrder);
        field("classificationParameters", Product::setClassificationParameters);
        field("specification", Product::setSpecification);
        field("localizedDescription", Product::setLocalizedDescription);
        field("price", this::setPrice);
        field("stockKeepingUnit", Product::setStockKeepingUnit);

        field("universalProductCode", Product::setUniversalProductCode);
        field("manufacturerPartNumber", Product::setManufacturerPartNumber);
        field("active", Product::setActive);
        field("weight", this::setWeight);
        field("dimensionLength", Product::setDimensionLength);
        field("dimensionWidth", Product::setDimensionWidth);
        field("dimensionHeight", Product::setDimensionHeight);
        field("warranty", Product::setWarranty);
        field("countryOfOrigin", Product::setCountryOfOrigin);
        field("notes", Product::setNotes);
        field("modelName", Product::setModelName);
        field("manufacturerCode", Product::setManufacturerCode);
        field("internalCode", Product::setInternalCode);

        ignore("images");
    }

    private void setImagesOrder(Product product, List<Number> imagesIds) {
        product.getImages().sort(Comparator.comparingInt(image -> imagesIds.indexOf((int) image.getImageId())));
    }

    private void setCategory(Product product, Number value) {
        if (value == null) {
            product.setCategory(null);
            return;
        }
        Category category = categoryService.getById(value.longValue());
        product.setCategory(category);
    }

    private void setBrand(Product product, Number value) {
        if (value == null) {
            product.setBrand(null);
            return;
        }
        Brand brand = brandService.getById(value.longValue());
        product.setBrand(brand);
    }

    private void setPrice(Product product, Object value) {
        if (value == null) {
            product.setPrice(null);
            return;
        }
        product.setPrice(new BigDecimal(value.toString()));
    }

    private void setWeight(Product product, Object value) {
        if (value == null) {
            product.setWeight(null);
            return;
        }
        product.setWeight(new BigDecimal(value.toString()));
    }
}
