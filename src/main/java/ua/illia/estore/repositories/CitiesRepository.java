package ua.illia.estore.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;
import ua.illia.estore.model.location.City;

import java.util.List;
import java.util.Optional;

public interface CitiesRepository extends PagingAndSortingRepository<City, Long> {

    List<City> findAll();

    Page<City> findAll(Specification<City> specification, Pageable pageable);

    Optional<City> findByNpCityID(String cityId);

    List<City> findByNpCityIDNotIn(List<String> cityIds);

    Optional<City> findByNpRef(String npRef);
}
