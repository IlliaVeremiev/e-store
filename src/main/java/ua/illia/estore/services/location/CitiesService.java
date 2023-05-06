package ua.illia.estore.services.location;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.illia.estore.model.location.City;

import java.util.List;
import java.util.Map;

public interface CitiesService {

    void synchronizeCities(int page, int size);

    void synchronizeCities();

    Page<City> search(String search, Pageable pageable);

    List<Map<String, Object>> warehouses(long cityId);

    City getById(long id);
}
