package ua.illia.estore.converters.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.illia.estore.converters.EntityConverter;
import ua.illia.estore.dto.product.ProductResponse;
import ua.illia.estore.dto.product.ProductVariantItemResponse;
import ua.illia.estore.dto.product.ProductVariantResponse;
import ua.illia.estore.model.product.Product;
import ua.illia.estore.services.media.ImageService;
import ua.illia.estore.services.product.ProductService;
import ua.illia.estore.services.store.StoreProductService;
import ua.illia.estore.utils.LocalizationUtil;

import java.util.AbstractMap;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ProductConverter implements EntityConverter<Product, ProductResponse> {
    @Autowired
    private BrandConverter brandConverter;
    @Autowired
    private CategoryConverter categoryConverter;
    @Autowired
    private ImageConverter imageConverter;
    @Autowired
    private ImageService imageService;
    @Autowired
    private StoreProductService storeProductService;
    @Autowired
    private ProductService productService;

    @Override
    public <E extends ProductResponse> E convert(Product product, E response) {
        response.setId(product.getId());
        response.setName(LocalizationUtil.getLocalized(product.getLocalizedName()));
        response.setLocalizedName(product.getLocalizedName());

        if (product.getBrand() != null)
            response.setBrand(brandConverter.convert(product.getBrand()));

        if (product.getCategory() != null)
            response.setCategory(categoryConverter.convert(product.getCategory()));


        if (product.getImages() != null) {
            response.setImages(product.getImages().stream().map(imageConverter::convert).collect(Collectors.toList()));
        }
        response.setClassificationParameters(product.getClassificationParameters());
        response.setSpecification(product.getSpecification());
        response.setDescription(LocalizationUtil.getLocalized(product.getLocalizedDescription()));
        response.setLocalizedDescription(product.getLocalizedDescription());
        response.setPrice(product.getPrice());

        if (product.getProductVariantItem() != null) {
            ProductVariantResponse productVariant = new ProductVariantResponse();
            productVariant.setId(product.getProductVariantItem().getProductVariant().getId());
            productVariant.setParametersName(
                    product.getProductVariantItem()
                            .getProductVariant()
                            .getLocalizedParametersName()
                            .entrySet()
                            .stream()
                            .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), LocalizationUtil.getLocalized(e.getValue())))
                            .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)));
            productVariant.setProductVariantItems(
                    product.getProductVariantItem()
                            .getProductVariant()
                            .getProductVariantItems()
                            .stream()
                            .filter(pv -> !Objects.equals(pv.getProduct().getId(), product.getId()))
                            .map(pv -> new ProductVariantItemResponse(pv.getId(), pv.getProduct().getId(), pv.getParametersValues()))
                            .collect(Collectors.toList())
            );
            response.setProductVariant(productVariant);
        }

        response.setStockKeepingUnit(product.getStockKeepingUnit());
        response.setUniversalProductCode(product.getUniversalProductCode());
        response.setManufacturerPartNumber(product.getManufacturerPartNumber());
        response.setWeight(product.getWeight());
        response.setDimensionLength(product.getDimensionLength());
        response.setDimensionWidth(product.getDimensionWidth());
        response.setDimensionHeight(product.getDimensionHeight());
        response.setWarranty(product.getWarranty());
        response.setCountryOfOrigin(product.getCountryOfOrigin());
        response.setNotes(product.getNotes());
        response.setModelName(product.getModelName());
        response.setManufacturerCode(product.getManufacturerCode());
        response.setInternalCode(product.getInternalCode());
        return response;
    }

    @Override
    public <E extends ProductResponse> E convert(Product product) {
        ProductResponse response = new ProductResponse();
        return (E) convert(product, response);
    }
}
