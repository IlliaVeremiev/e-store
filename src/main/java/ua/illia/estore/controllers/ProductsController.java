package ua.illia.estore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
import ua.illia.estore.configuration.exceptions.BindingValidationException;
import ua.illia.estore.configuration.security.EmployeeUserDetails;
import ua.illia.estore.converters.impl.ImageConverter;
import ua.illia.estore.converters.impl.ProductConverter;
import ua.illia.estore.dto.image.ImageResponse;
import ua.illia.estore.dto.product.ProductCreateForm;
import ua.illia.estore.dto.product.ProductResponse;
import ua.illia.estore.dto.product.ProductSearchForm;
import ua.illia.estore.dto.storeproduct.StoreProductResponse;
import ua.illia.estore.model.product.Product;
import ua.illia.estore.services.product.ProductService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
public class ProductsController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductConverter productConverter;

    @Autowired
    private ImageConverter imageConverter;

    @Transactional
    @PostMapping
    public ProductResponse create(@Validated ProductCreateForm productDto,
                                  BindingResult result,
                                  @AuthenticationPrincipal EmployeeUserDetails userDetails) {
        if (result.hasErrors()) {
            throw new BindingValidationException(result);
        }
        return productConverter.convert(productService.create(productDto, userDetails.getUser()), new StoreProductResponse());
    }

    @Transactional
    @GetMapping("/{productId}")
    public ProductResponse getById(@PathVariable long productId) {
        return productConverter.convert(productService.getById(productId), new StoreProductResponse());
    }

    @Transactional
    @GetMapping
    public Page<ProductResponse> search(ProductSearchForm productSearchForm,
                                        @PageableDefault(size = 48, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Product> search = productService.search(productSearchForm, pageable);
        return search
                .map(product -> productConverter.convert(product, new StoreProductResponse()));

    }

    @Transactional
    @PutMapping("/{productId}")
    public ProductResponse update(@PathVariable long productId,
                                  @RequestBody Map<String, Object> data,
                                  @AuthenticationPrincipal EmployeeUserDetails userDetails) {
        return productConverter.convert(productService.update(productId, data, userDetails.getUser()));
    }

    @DeleteMapping("/{productId}")
    public void delete(@PathVariable long productId) {
        productService.delete(productId);
    }

    @Transactional
    @DeleteMapping("/{productId}/images/{imageId}")
    public List<ImageResponse> deleteImage(@PathVariable Long productId,
                                           @PathVariable Long imageId) {
        return productService.deleteImage(productId, imageId)
                .stream()
                .map(imageConverter::convert)
                .collect(Collectors.toList());
    }

    @Transactional
    @PostMapping("/{productId}/images")
    public List<ImageResponse> uploadImage(@PathVariable Long productId,
                                           @RequestPart List<MultipartFile> images) {
        return productService.addImages(productId, images)
                .stream()
                .map(imageConverter::convert)
                .collect(Collectors.toList());
    }
}
