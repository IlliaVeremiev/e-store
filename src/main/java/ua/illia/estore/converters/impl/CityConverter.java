package ua.illia.estore.converters.impl;

import org.springframework.stereotype.Component;
import ua.illia.estore.converters.EntityConverter;
import ua.illia.estore.dto.cities.CityResponse;
import ua.illia.estore.model.location.City;
import ua.illia.estore.utils.LocalizationUtil;

@Component
public class CityConverter implements EntityConverter<City, CityResponse> {

    @Override
    public CityResponse convert(City city, CityResponse response) {
        response.setName(LocalizationUtil.getLocalized(city.getLocalizedName(), null));
        response.setId(city.getId());
        response.setAreaName(LocalizationUtil.getLocalized(city.getNpAreaDescriptionLocalized(), null) + " обл.");
        response.setCityName(
                LocalizationUtil.getLocalized(city.getNpSettlementTypeDescriptionLocalized(), null) + " " +
                        LocalizationUtil.getLocalized(city.getLocalizedName(), null)
        );
        return response;
    }

    @Override
    public CityResponse convert(City city) {
        CityResponse response = new CityResponse();
        return convert(city, response);
    }
}
