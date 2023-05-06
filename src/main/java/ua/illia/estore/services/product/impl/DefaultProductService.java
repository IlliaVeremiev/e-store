package ua.illia.estore.services.product.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import ua.illia.estore.configuration.exceptions.ConflictException;
import ua.illia.estore.dto.product.ProductCreateForm;
import ua.illia.estore.dto.product.ProductSearchForm;
import ua.illia.estore.injectors.impl.ProductInjector;
import ua.illia.estore.model.product.Category;
import ua.illia.estore.model.product.Image;
import ua.illia.estore.model.product.Product;
import ua.illia.estore.model.product.data.ProductImage;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.repositories.ProductRepository;
import ua.illia.estore.search.SpecificationList;
import ua.illia.estore.services.invoice.InvoiceService;
import ua.illia.estore.services.media.ImageService;
import ua.illia.estore.services.order.OrderService;
import ua.illia.estore.services.product.CategoryService;
import ua.illia.estore.services.product.ProductService;
import ua.illia.estore.services.warehouse.WarehouseService;
import ua.illia.estore.utils.ServiceUtils;
import ua.illia.estore.validation.validators.impl.ProductValidator;

import javax.persistence.criteria.JoinType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DefaultProductService implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductInjector productInjector;

    @Autowired
    private ProductValidator productValidator;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private OrderService orderService;


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ImageService imageService;

    @Override
    @Transactional
    public Product create(ProductCreateForm form, Employee employee) {
        Product product = new Product();
        productInjector.inject(product, form);
        productValidator.validate(product);
        product.setCreationDate(LocalDateTime.now());
        product.setCreatedBy(employee);
        product.setActive(true);

        productRepository.save(product);

        if (form.getImages() != null) {
            List<ProductImage> images = form.getImages()
                    .parallelStream()
                    .map(image -> imageService.create(image))
                    .map(image -> {
                        ProductImage productImage = new ProductImage();
                        productImage.setImageId(image.getId());
                        productImage.setPath(image.getPath());
                        product.getImages().add(productImage);
                        return productImage;
                    })
                    .collect(Collectors.toList());
            product.setImages(images);
        }
        productRepository.save(product);

        return product;
    }

    @Override
    public Product getById(long id) {
        return productRepository.findById(id)
                .orElseThrow(ServiceUtils.notFound("Product", "id", id));
    }

    @Override
    public Page<Product> search(ProductSearchForm form, Pageable pageable) {
        SpecificationList<Product> specification = new SpecificationList<>();
        if (StringUtils.isNotEmpty(form.getCategory())) {
            Category category = categoryService.getByUid(form.getCategory());
            specification.add((r, q, b) -> b.equal(r.get("category"), category));
        }
        if (!CollectionUtils.isEmpty(form.getBrands())) {
            specification.add((r, q, b) -> b.in(r.get("brand").get("id")).value(form.getBrands()));
        }
        if (StringUtils.isNotEmpty(form.getQuery())) {
            specification.add((r, q, b) -> b.isTrue(b.function("@@", Boolean.class,
                    b.function("concat", String.class,
                            b.function("to_tsvector", Map.class, r.get("id").as(String.class)),
                            b.function("to_tsvector", Map.class, r.join("category", JoinType.LEFT).get("localizedName")),
                            b.function("to_tsvector", Map.class, r.join("brand", JoinType.LEFT).get("name")),
                            b.function("to_tsvector", Map.class, r.get("localizedName"))
                    ),
                    b.function("plainto_tsquery", Map.class, b.literal(form.getQuery()))
            )));
        }
        if (form.getClassification() != null) {
            for (Map.Entry<String, String> entry : form.getClassification().entrySet()) {
                specification.add((r, q, b) -> b.equal(b.function("jsonb_extract_path_text", String.class, r.get("classificationParameters"), b.literal(entry.getKey())), entry.getValue()));
            }
        }
        return productRepository.findAll(specification, pageable);
    }

    @Override
    public Product update(long id, Map<String, Object> data, Employee employee) {
        Product product = getById(id);
        productInjector.inject(product, data);
        product.setUpdatedBy(employee);
        product.setLastUpdateDate(LocalDateTime.now());
        productValidator.validate(product);
        productRepository.save(product);
        return productRepository.getById(product.getId());
    }

    @Override
    public void delete(long id) {
        Product product = getById(id);
        if (!warehouseService.getByProduct(product).isEmpty()) {
            throw new ConflictException("Can not delete product that used as warehouse product", "product.warehouse");
        }
        if (!invoiceService.getWithProduct(product).isEmpty()) {
            throw new ConflictException("Can not delete product that presents in invoice", "product.invoice");
        }
        if (!orderService.getWithProduct(product).isEmpty()) {
            throw new ConflictException("Can not delete product that presents in orders", "product.order");
        }
        productRepository.deleteById(id);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Image> addImages(long productId, List<MultipartFile> files) {
        Product product = getById(productId);
        List<Image> images = files.parallelStream()
                .map(image -> imageService.create(image))
                .collect(Collectors.toList());
        List<ProductImage> imageList = new ArrayList<>(product.getImages());
        for (Image image : images) {
            ProductImage productImage = new ProductImage();
            productImage.setImageId(image.getId());
            productImage.setPath(image.getPath());
            imageList.add(productImage);
        }
        product.setImages(imageList);
        productRepository.save(product);
        return images;
    }

    @Override
    @Transactional
    public List<Image> deleteImage(long productId, long imageId) {
        Product product = getById(productId);
        product.setImages(product.getImages().stream().filter(image -> imageId != image.getImageId()).collect(Collectors.toList()));
        productRepository.save(product);
        imageService.deleteImage(imageId);
        return product.getImages().stream().map(ProductImage::getImageId).map(imageService::getById).collect(Collectors.toList());
    }

    @Override
    public List<Product> getByIds(List<Long> ids) {
        return productRepository.findAllByIdIn(ids);
    }
}
