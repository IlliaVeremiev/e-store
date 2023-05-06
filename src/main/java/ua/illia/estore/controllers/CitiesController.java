package ua.illia.estore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.illia.estore.converters.impl.CityConverter;
import ua.illia.estore.dto.cities.CityResponse;
import ua.illia.estore.services.location.CitiesService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cities")
public class CitiesController {

    @Autowired
    private CitiesService citiesService;

    @Autowired
    private CityConverter cityConverter;

    @GetMapping
    public Page<CityResponse> getAddress(@RequestParam(required = false) String search,
                                         @PageableDefault(size = 50) Pageable pageable) {
        return citiesService.search(search, pageable).map(cityConverter::convert);
    }

    @GetMapping("/{cityId}")
    public CityResponse getCity(@PathVariable long cityId) {
        return cityConverter.convert(citiesService.getById(cityId));
    }

    @GetMapping("/{cityId}/warehouses")
    public List<Map<String, Object>> getWarehouses(@PathVariable long cityId) {
        return citiesService.warehouses(cityId);
    }
}
