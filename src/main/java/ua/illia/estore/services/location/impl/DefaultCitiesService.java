package ua.illia.estore.services.location.impl;

import com.google.common.collect.ImmutableMap;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ua.illia.estore.configuration.exceptions.NotFoundException;
import ua.illia.estore.model.location.City;
import ua.illia.estore.repositories.CitiesRepository;
import ua.illia.estore.search.SpecificationList;
import ua.illia.estore.services.location.CitiesService;
import ua.illia.estore.utils.ServiceUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DefaultCitiesService implements CitiesService {

    @Autowired
    private CitiesRepository citiesRepository;

    @PersistenceContext
    private EntityManager em;

    @Scheduled(initialDelay = 60 * 60 * 60 * 1000, fixedDelay = 24 * 60 * 60 * 1000)
    public void synchronizeCities() {
        int npCitiesCount = getNpCitiesCount();
        int citiesSize = 100;
        for (int page = 1, processed = 0; processed < npCitiesCount; page++, processed += citiesSize) {
            long startTime = System.currentTimeMillis();
            synchronizeCities(page, citiesSize);
            log.info("Updated " + Math.min((processed + citiesSize), npCitiesCount) + " cities of " + npCitiesCount + " in " + String.format("%.2f", (System.currentTimeMillis() - startTime) / 1000.f) + " seconds");
        }
    }

    public void synchronizeCities(int page, int size) {
        List<Map> citiesFromNp = getNpCities(page, size);
        for (Map<String, String> cityData : citiesFromNp) {
            City city = citiesRepository.findByNpCityID(cityData.get("CityID")).orElseGet(() -> {
                City c = new City();
                c.setNpCityID(cityData.get("CityID"));
                return c;
            });

            city.setNpRef(cityData.get("Ref"));
            city.setNpArea(cityData.get("Area"));
            city.setLocalizedName(ImmutableMap.of("uk", cityData.get("Description"), "ru", cityData.get("DescriptionRu")));
            city.setNpSettlementTypeDescriptionLocalized(ImmutableMap.of("uk", cityData.get("SettlementTypeDescription"), "ru", cityData.get("SettlementTypeDescriptionRu")));
            city.setNpAreaDescriptionLocalized(ImmutableMap.of("uk", cityData.get("AreaDescription"), "ru", cityData.get("AreaDescriptionRu")));
            city.setNovaPoshtaWarehouses(getWarehouses(city.getNpRef()));

            citiesRepository.save(city);
            em.flush();
            em.clear();
        }
    }

    private List<Map<String, Object>> getWarehouses(String npRef) {
        Map<String, Object> body = new HashMap<>();
        body.put("apiKey", "0f31a25e5f0f90d12de0a59eb53f4e3e");
        body.put("modelName", "Address");
        body.put("calledMethod", "getWarehouses");
        body.put("methodProperties", ImmutableMap.of(
                "CityRef", npRef
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        return ServiceUtils.makeSeveralAttempts(() -> (List<Map<String, Object>>) performNpRequest(request).get("data"), 10, 60000)
                .stream()
                .map(warehouseData -> {
                    HashMap<String, Object> wh = new HashMap<>();
                    wh.put("name", ImmutableMap.of("uk", warehouseData.get("Description"), "ru", warehouseData.get("DescriptionRu")));
                    wh.put("npRef", warehouseData.get("Ref"));
                    wh.put("shortAddress", ImmutableMap.of("uk", warehouseData.get("ShortAddress"), "ru", warehouseData.get("ShortAddressRu")));
                    wh.put("typeOfWarehouse", warehouseData.get("TypeOfWarehouse"));
                    wh.put("number", warehouseData.get("Number"));
                    wh.put("cityDescription", ImmutableMap.of("uk", warehouseData.get("CityDescription"), "ru", warehouseData.get("CityDescriptionRu")));
                    wh.put("longitude", warehouseData.get("Longitude"));
                    wh.put("latitude", warehouseData.get("latitude"));
                    wh.put("totalMaxWeightAllowed", warehouseData.get("TotalMaxWeightAllowed"));
                    wh.put("receivingLimitationsOnDimensions", warehouseData.get("ReceivingLimitationsOnDimensions"));
                    wh.put("maxDeclaredCost", warehouseData.get("MaxDeclaredCost"));
                    return wh;
                })
                .collect(Collectors.toList());
    }

    private Map performNpRequest(HttpEntity<Map<String, Object>> request) {
        HttpComponentsClientHttpRequestFactory clientRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientRequestFactory.setReadTimeout(120000);
        RestTemplate restTemplate = new RestTemplate(clientRequestFactory);
        return restTemplate.exchange("https://api.novaposhta.ua/v2.0/json/", HttpMethod.POST, request, Map.class)
                .getBody();
    }

    @SneakyThrows
    private List<Map<String, Object>> getNpWarehouses(int page, int size) {
        Map<String, Object> body = new HashMap<>();
        body.put("apiKey", "0f31a25e5f0f90d12de0a59eb53f4e3e");
        body.put("modelName", "Address");
        body.put("calledMethod", "getWarehouses");
        body.put("methodProperties", ImmutableMap.of(
                "Page", page,
                "Limit", size
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        return ServiceUtils.makeSeveralAttempts(() -> (List<Map<String, Object>>) performNpRequest(request).get("data"), 10, 60000);
    }

    @SneakyThrows
    private List<Map> getNpCities(int page, int size) {
        Map<String, Object> body = new HashMap<>();
        body.put("apiKey", "0f31a25e5f0f90d12de0a59eb53f4e3e");
        body.put("modelName", "Address");
        body.put("calledMethod", "getCities");
        body.put("methodProperties", ImmutableMap.of(
                "Page", page,
                "Limit", size
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);


        return ServiceUtils.makeSeveralAttempts(() -> (List<Map>) performNpRequest(request).get("data"), 10, 60000);
    }

    @SneakyThrows
    private int getNpCitiesCount() {
        Map<String, Object> body = new HashMap<>();
        body.put("apiKey", "0f31a25e5f0f90d12de0a59eb53f4e3e");
        body.put("modelName", "Address");
        body.put("calledMethod", "getCities");
        body.put("methodProperties", ImmutableMap.of("Limit", 1));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        Map<String, Object> info = (Map<String, Object>) performNpRequest(request).get("info");
        return ((Number) info.get("totalCount")).intValue();
    }

    private int getNpWarehousesCount() {
        Map<String, Object> body = new HashMap<>();
        body.put("apiKey", "0f31a25e5f0f90d12de0a59eb53f4e3e");
        body.put("modelName", "Address");
        body.put("calledMethod", "getWarehouses");
        body.put("methodProperties", ImmutableMap.of("Limit", 1));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        Map<String, Object> info = (Map<String, Object>) performNpRequest(request).get("info");
        return ((Number) info.get("totalCount")).intValue();
    }

    @Override
    public Page<City> search(String search, Pageable pageable) {
        SpecificationList<City> specification = new SpecificationList<>();
        if (search != null && !search.equals("")) {
            specification.add((r, q, b) -> b.isTrue(
                    b.function("@@", Boolean.class,
                            b.function("concat", String.class,
                                    b.function("to_tsvector", Map.class, r.get("localizedName")),
                                    b.function("to_tsvector", Map.class, r.get("npAreaDescriptionLocalized")),
                                    b.function("to_tsvector", Map.class, r.get("npSettlementTypeDescriptionLocalized"))
                            ),
                            b.function("plainto_tsquery", Map.class, b.literal(search))
                    )
            ));
        } else {
            specification.add((r, q, b) ->
                    b.in(b.function("->>", String.class, r.get("localizedName"), b.literal("ru")))
                            .value("Киев")
                            .value("Харьков")
                            .value("Одесса")
                            .value("Днепр")
                            .value("Запорожье")
                            .value("Львов")
            );
        }
        return citiesRepository.findAll(specification, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.by("npCityID")).and(pageable.getSort())));
    }

    @Override
    public List<Map<String, Object>> warehouses(long id) {
        City city = getById(id);
        return city.getNovaPoshtaWarehouses();
    }

    @Override
    public City getById(long id) {
        return citiesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("City with id: " + id + " not found", "city.id"));
    }
}
