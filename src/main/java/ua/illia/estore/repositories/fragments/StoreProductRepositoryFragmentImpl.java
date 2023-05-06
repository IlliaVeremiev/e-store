package ua.illia.estore.repositories.fragments;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import ua.illia.estore.dto.storeproduct.StoreProductRepositoryResponse;
import ua.illia.estore.dto.storeproduct.StoreProductSearchForm;
import ua.illia.estore.model.product.Brand;
import ua.illia.estore.model.product.Category;
import ua.illia.estore.model.product.CategoryClassificationParameter;
import ua.illia.estore.model.product.Product;
import ua.illia.estore.model.product.enums.FilterPropertyType;
import ua.illia.estore.model.store.Store;
import ua.illia.estore.repositories.StoreProductRepositoryFragment;
import ua.illia.estore.services.product.CategoryService;
import ua.illia.estore.utils.CountingPage;
import ua.illia.estore.utils.Utils;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StoreProductRepositoryFragmentImpl implements StoreProductRepositoryFragment {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CategoryService categoryService;

    @Override
    public CountingPage<StoreProductRepositoryResponse> search(Store store, StoreProductSearchForm form, Map<String, Object> classification, Pageable pageable) {
        Category category = getCategory(form.getCategory());
        List<Category> categories = getCategories(category);
        List<Long> categoryIds = categories.stream().map(Category::getId).collect(Collectors.toList());
        List<Brand> categoryBrands = getCategoryBrands(categories);
        List<Long> categoryBrandsIds = categoryBrands.stream().map(Brand::getId).collect(Collectors.toList());
        List<Long> searchBrands = getSearchBrands(form.getBrands(), categoryBrandsIds);
        List<StoreProductRepositoryResponse> products = searchStoreProducts(store, form, category, categoryIds, searchBrands, classification, pageable);
        Map<String, Object> rangeLimits = getRangeLimits(store, form, classification, category, categoryIds);
        rangeLimits.put("availableBrands", categoryBrandsIds);

        return new CountingPage<>(products, pageable, (Long) rangeLimits.get("totalCount"), rangeLimits);
    }

    private List<Category> getCategories(Category category) {
        if (category != null) {
            return categoryService.getSubCategories(category);
        }
        return Collections.emptyList();
    }

    private List<StoreProductRepositoryResponse> searchStoreProducts(Store store, StoreProductSearchForm form, Category category, List<Long> categoryIds, List<Long> brands, Map<String, Object> classification, Pageable pageable) {
        Map<String, Object> parameters = new HashMap<>();
        StringBuilder queryString = new StringBuilder("SELECT p " +
                "FROM Product p " +
                "LEFT JOIN WarehouseProduct wp ON wp.product = p " +
                "LEFT JOIN wp.warehouse w " +
                "LEFT JOIN w.stores s " +
                "LEFT JOIN p.category c " +
                "LEFT JOIN p.brand b " +
                "LEFT JOIN p.createdBy cb " +
                "LEFT JOIN p.productVariantItem pvi " +
                "WHERE 1=1 ");
        if (CollectionUtils.isNotEmpty(categoryIds)) {
            queryString.append("AND c.id IN (:categoryIds) ");
            parameters.put("categoryIds", categoryIds);
        }
        if (StringUtils.isNotBlank(form.getQuery())) {
            queryString.append("AND fts(p.localizedName, p.specification, p.localizedDescription, c.localizedName, b.name, :query) = true ");
            parameters.put("query", form.getQuery());
        }
        if (CollectionUtils.isNotEmpty(brands)) {
            queryString.append("AND b.id IN(:brandsIds) ");
            parameters.put("brandsIds", brands);
        }
        if (form.getMinPrice() != null) {
            queryString.append("AND p.price >= :minPrice ");
            parameters.put("minPrice", form.getMinPrice());
        }
        if (form.getMaxPrice() != null) {
            queryString.append("AND p.price <= :maxPrice ");
            parameters.put("maxPrice", form.getMaxPrice());
        }
        if (category != null && !category.isFolder() && MapUtils.isNotEmpty(classification)) {
            for (Map.Entry<String, Object> entry : classification.entrySet()) {
                CategoryClassificationParameter cp = categoryService.getClassificationParameters(category).stream().filter(cParam -> Objects.equals(cParam.getKey(), entry.getKey())).findFirst().orElseThrow(() -> new IllegalArgumentException("Not such classification parameter: " + entry.getKey()));

                if (cp.getType() == FilterPropertyType.VALUES) {
                    queryString.append("AND jsonb_extract_path_text(p.classificationParameters, :classification_").append(cp.getKey()).append(") IN(:classification_").append(cp.getKey()).append("_value) ");
                    parameters.put("classification_" + cp.getKey(), cp.getKey());
                    parameters.put("classification_" + cp.getKey() + "_value", entry.getValue());
                }
                if (cp.getType() == FilterPropertyType.INT_RANGE) {
                    queryString.append("AND cast(jsonb_extract_path_text(p.classificationParameters, :classification_").append(cp.getKey()).append(") as int) >= :classification_").append(cp.getKey()).append("_valueMin ");
                    queryString.append("AND cast(jsonb_extract_path_text(p.classificationParameters, :classification_").append(cp.getKey()).append(") as int) <= :classification_").append(cp.getKey()).append("_valueMax ");
                    parameters.put("classification_" + cp.getKey(), cp.getKey());
                    parameters.put("classification_" + cp.getKey() + "_valueMin", ((List<?>) entry.getValue()).get(0));
                    parameters.put("classification_" + cp.getKey() + "_valueMax", ((List<?>) entry.getValue()).get(1));
                }
            }
        }

        queryString.append("GROUP BY p.id, c.id, b.id, cb.id, pvi.id ");

        //Be careful in case one more HAVING condition will be added, HAVING included only in case inStock not empty
        if (CollectionUtils.isNotEmpty(form.getInStock())) {
            queryString.append("HAVING (CASE WHEN coalesce(sum(wp.count), 0) > 0 THEN TRUE ELSE FALSE END) IN(:inStock) ");
            parameters.put("inStock", form.getInStock());
        }
        queryString.append("ORDER BY case when coalesce(sum(wp.count), 0) = 0 then 0 else 1 end DESC ");
        if (StringUtils.isNotBlank(form.getSortOrder())) {
            queryString.append(", ").append(getSortOrder(form.getSortOrder()));
        }

        Query query = em.createQuery(queryString.toString(), Product.class);
        EntityGraph<?> entityGraph = em.getEntityGraph(Product.Graph.BASIC);
        query.setHint("javax.persistence.fetchgraph", entityGraph);

        parameters.forEach(query::setParameter);
        query.setMaxResults(pageable.getPageSize());
        query.setFirstResult((int) pageable.getOffset());
        List<Product> products = query.getResultList();
        Query countQuery = em.createQuery("SELECT p.id, coalesce(sum(wp.count), 0) FROM Product p LEFT JOIN WarehouseProduct wp ON wp.product = p LEFT JOIN wp.warehouse w LEFT JOIN w.stores s " +
                "WHERE (s = :store) AND p IN(:products) GROUP BY p.id");
        countQuery.setParameter("store", store);
        countQuery.setParameter("products", products);
        List<Object[]> countResults = countQuery.getResultList();
        return products.stream().map(p -> {
            StoreProductRepositoryResponse response = new StoreProductRepositoryResponse();
            response.setProduct(p);
            response.setCount(countResults.stream().filter(o -> Objects.equals(o[0], p.getId())).map(o -> (BigDecimal) o[1]).findFirst().orElse(BigDecimal.ZERO));
            return response;
        }).collect(Collectors.toList());
    }

    private List<Long> getSearchBrands(List<Long> brands, List<Long> categoryBrandsIds) {
        if (CollectionUtils.isNotEmpty(brands)) {
            return brands.stream().filter(categoryBrandsIds::contains).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private List<Brand> getCategoryBrands(List<Category> categories) {
        if (CollectionUtils.isNotEmpty(categories)) {
            Query availableBrandsQuery = em.createQuery("SELECT DISTINCT b FROM Product p JOIN p.brand b JOIN p.category c WHERE c IN(:categories)");
            availableBrandsQuery.setParameter("categories", categories);
            return (List<Brand>) availableBrandsQuery.getResultList();
        }
        return Collections.emptyList();
    }

    private Category getCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return categoryService.getById(categoryId);
    }

    @Override
    public BigDecimal countProductsAvailableInStore(Store store, Product product) {
        Query query = em.createQuery("SELECT coalesce(sum(wp.count), 0) " + "FROM Store s " + "JOIN s.warehouses w " + "JOIN w.products wp " + "JOIN wp.product p " + "WHERE s = :store AND p = :product " + "GROUP BY p.id");
        query.setParameter("store", store);
        query.setParameter("product", product);
        List<BigDecimal> resultList = query.getResultList();
        return !resultList.isEmpty() ? resultList.get(0) : BigDecimal.ZERO;
    }

    @Data
    @AllArgsConstructor
    private static class ClassificationEntry {

        private String key;

        private FilterPropertyType type;

        private long id;

        private Object value;
    }

    private Map<String, Object> getRangeLimits(Store store, StoreProductSearchForm form, Map<String, Object> classification, Category category, List<Long> categoriesIds) {
        List<ClassificationEntry> classificationEntries = category != null && !category.isFolder() ? categoryService.getClassificationParameters(category).stream().map(ccp -> new ClassificationEntry(ccp.getKey(), ccp.getType(), ccp.getId(), classification.get(ccp.getKey()))).collect(Collectors.toList()) : Collections.emptyList();
        Map<String, Object> parameters = new HashMap<>();
        List<String> columns = new ArrayList<>();
        StringJoiner groupBy = new StringJoiner(", ");

        StringBuilder queryString = new StringBuilder("SELECT ");

        queryString.append("p.category_id AS category, ");
        columns.add("category");
        groupBy.add("p.category_id");

        queryString.append("p.brand_id AS brand, ");
        columns.add("brand");
        groupBy.add("p.brand_id");

        queryString.append("min(p.price) AS min_price, ");
        columns.add("minPrice");
        groupBy.add("p.price >= :minPrice");
        parameters.put("minPrice", form.getMinPrice() != null ? form.getMinPrice() : 0);

        queryString.append("max(p.price) AS max_price, ");
        columns.add("maxPrice");
        groupBy.add("p.price <= :maxPrice");
        parameters.put("maxPrice", form.getMaxPrice() != null ? form.getMaxPrice() : BigDecimal.valueOf(Long.MAX_VALUE));

        queryString.append("p.count > 0 AS available, ");
        columns.add("available");
        groupBy.add("p.count > 0");

        for (ClassificationEntry entry : classificationEntries) {
            if (entry.getType() == FilterPropertyType.INT_RANGE) {
                String paramName = "classification_" + entry.getId();
                parameters.put(paramName, entry.getKey());
                List<?> values = (List<?>) entry.getValue();

                queryString.append("min(cast(jsonb_extract_path_text(p.classification_parameters, :").append(paramName).append(") AS int)) as min_").append(paramName).append(", ");
                columns.add("min_" + paramName);

                queryString.append("max(cast(jsonb_extract_path_text(p.classification_parameters, :").append(paramName).append(") AS int)) as max_").append(paramName).append(", ");
                columns.add("max_" + paramName);

                if (values != null) {
                    groupBy.add("cast(jsonb_extract_path_text(p.classification_parameters, :" + paramName + ") AS int) >= :" + paramName + "_valueMin");
                    groupBy.add("cast(jsonb_extract_path_text(p.classification_parameters, :" + paramName + ") AS int) <= :" + paramName + "_valueMax");
                    parameters.put(paramName + "_valueMin", values.get(0));
                    parameters.put(paramName + "_valueMax", values.get(1));
                }

            } else if (entry.getType() == FilterPropertyType.VALUES) {
                String paramName = "classification_" + entry.getId();
                parameters.put(paramName, entry.getKey());

                queryString.append("max(jsonb_extract_path_text(p.classification_parameters, :").append(paramName).append(")) AS ").append(paramName).append(", ");
                columns.add(paramName);
                groupBy.add("jsonb_extract_path_text(p.classification_parameters, :" + paramName + ")");
            } else {
                throw new IllegalArgumentException("Classification parameter type should not be null");
            }
        }

        queryString.append("count(*) as count ");
        columns.add("count");

        queryString.append("FROM( SELECT p.*, coalesce(sum(wp.count), 0) count ");
        queryString.append("FROM products p " + "LEFT JOIN warehouse_products wp ON wp.product_id = p.id " + "LEFT JOIN warehouses w ON w.id = wp.warehouse_id " + "LEFT JOIN store_warehouses sw ON sw.warehouse_id = w.id " + "LEFT JOIN stores s ON s.id = sw.store_id " + "JOIN categories c ON c.id = p.category_id " + "JOIN brands b ON b.id = p.brand_id ");

        queryString.append("WHERE (s.id = :storeId OR s.id IS NULL) ");
        parameters.put("storeId", store.getId());

        queryString.append("GROUP BY p.id ");
        queryString.append(") as p ");
        queryString.append("GROUP BY ").append(groupBy);

        Query query = em.createNativeQuery(queryString.toString());

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        List<Object[]> resultList = query.getResultList();

        Counter counter = new Counter();
        counter.addParameter("categories", o -> CollectionUtils.isEmpty(categoriesIds) || categoriesIds.contains(((BigInteger) o[columns.indexOf("category")]).longValue()), stream -> stream.collect(Collectors.toMap(o -> ((BigInteger) o[columns.indexOf("category")]).longValue(), o -> ((BigInteger) o[columns.indexOf("count")]).longValue(), Long::sum)));
        counter.addParameter("brands", o -> form.getBrands() == null || form.getBrands().contains(((BigInteger) o[columns.indexOf("brand")]).longValue()), stream -> stream.collect(Collectors.toMap(o -> ((BigInteger) o[columns.indexOf("brand")]).longValue(), o -> ((BigInteger) o[columns.indexOf("count")]).longValue(), Long::sum)));
        counter.addParameter("price", o -> (form.getMinPrice() == null || form.getMinPrice().compareTo((BigDecimal) o[columns.indexOf("minPrice")]) <= 0) && (form.getMaxPrice() == null || form.getMaxPrice().compareTo((BigDecimal) o[columns.indexOf("maxPrice")]) >= 0), stream -> {
            List<Object[]> list = stream.collect(Collectors.toList());
            BigDecimal minPrice = list.stream().filter(o -> o[columns.indexOf("minPrice")] != null).min(Comparator.comparing(o -> ((BigDecimal) o[columns.indexOf("minPrice")]))).map(o -> (BigDecimal) o[columns.indexOf("minPrice")]).orElse(BigDecimal.ZERO);
            BigDecimal maxPrice = resultList.stream().filter(o -> o[columns.indexOf("maxPrice")] != null).max(Comparator.comparing(o -> ((BigDecimal) o[columns.indexOf("maxPrice")]))).map(o -> (BigDecimal) o[columns.indexOf("maxPrice")]).orElse(BigDecimal.ZERO);
            return ImmutableMap.of("min", minPrice, "max", maxPrice);
        });
        counter.addParameter("available", o -> form.getInStock() == null || form.getInStock().isEmpty() || form.getInStock().contains(o[columns.indexOf("available")]), stream -> stream.collect(Collectors.toMap(o -> ((Boolean) o[columns.indexOf("available")]), o -> ((BigInteger) o[columns.indexOf("count")]).longValue(), Long::sum)));

        for (ClassificationEntry entry : classificationEntries) {
            if (entry.getType() == FilterPropertyType.VALUES) {
                counter.addParameter(entry.getKey(), o -> entry.getValue() == null || ((List<?>) entry.getValue()).contains(o[columns.indexOf("classification_" + entry.getId())]), stream -> stream.filter(o -> o[columns.indexOf("classification_" + entry.getId())] != null).collect(Collectors.toMap(o -> o[columns.indexOf("classification_" + entry.getId())], o -> ((BigInteger) o[columns.indexOf("count")]).longValue(), Long::sum)));
            } else if (entry.getType() == FilterPropertyType.INT_RANGE) {
                counter.addParameter(entry.getKey(), o -> entry.getValue() == null || new BigInteger(((List<?>) entry.getValue()).get(0).toString()).compareTo(BigInteger.valueOf((Integer) Utils.valueOrDefault(o[columns.indexOf("min_classification_" + entry.getId())], 0))) <= 0 && new BigInteger(((List<?>) entry.getValue()).get(1).toString()).compareTo(BigInteger.valueOf((Integer) Utils.valueOrDefault(o[columns.indexOf("max_classification_" + entry.getId())], 0))) >= 0, stream -> {
                    List<Object[]> list = stream.collect(Collectors.toList());
                    Integer minResult = list.stream().min(Comparator.comparing(o -> ((Integer) Utils.valueOrDefault(o[columns.indexOf("min_classification_" + entry.getId())], 0)))).map(o -> (Integer) Utils.valueOrDefault(o[columns.indexOf("min_classification_" + entry.getId())], 0)).orElse(0);
                    Integer maxResult = list.stream().max(Comparator.comparing(o -> ((Integer) Utils.valueOrDefault(o[columns.indexOf("max_classification_" + entry.getId())], 0)))).map(o -> (Integer) Utils.valueOrDefault(o[columns.indexOf("max_classification_" + entry.getId())], 0)).orElse(0);
                    return ImmutableMap.of("min", minResult, "max", maxResult);
                });
            }
        }

        long totalCount = resultList.stream().filter(o -> counter.params.entrySet().stream().allMatch(p -> p.getValue().predicate.test(o))).mapToLong(o -> ((BigInteger) o[columns.indexOf("count")]).longValue()).reduce(Long::sum).orElse(0);


        Map<String, Object> response = new HashMap<>();
        response.put("totalCount", totalCount);
        response.putAll(counter.calculateAll(resultList));
        return response;
    }

    private String getSortOrder(String sortOrder) {
        if ("rating".equals(sortOrder)) {
            return "p.id";
        }
        if ("new".equals(sortOrder)) {
            return "p.creationDate DESC";
        }
        if ("priceAsc".equals(sortOrder)) {
            return "p.price ASC";
        }
        if ("priceDesc".equals(sortOrder)) {
            return "p.price DESC";
        }
        return "p.id";
    }

    static class Counter {
        public Map<String, Object> calculateAll(List<Object[]> resultList) {
            Map<String, Object> response = new HashMap<>();
            for (Map.Entry<String, Parameter> entry : params.entrySet()) {
                Stream<Object[]> stream = resultList.stream().filter(o -> params.entrySet().stream().filter(p -> !p.getKey().equals(entry.getKey())).allMatch(p -> p.getValue().predicate.test(o)));
                response.put(entry.getKey(), entry.getValue().supplier.apply(stream));
            }
            return response;
        }

        @AllArgsConstructor
        static class Parameter {
            Predicate<Object[]> predicate;
            Function<Stream<Object[]>, Object> supplier;
        }

        private final Map<String, Parameter> params = new HashMap<>();

        public void addParameter(String key, Predicate<Object[]> predicate, Function<Stream<Object[]>, Object> supplier) {
            params.put(key, new Parameter(predicate, supplier));
        }
    }
}
