package ua.illia.estore.converters.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.illia.estore.dto.kassasession.KassaSessionResponse;
import ua.illia.estore.model.store.KassaSession;

@Component
public class KassaSessionConverter {

    @Autowired
    private EmployeeConverter employeeConverter;

    @Autowired
    private StoreConverter storeConverter;

    public KassaSessionResponse convert(KassaSession session) {
        KassaSessionResponse response = new KassaSessionResponse();
        response.setId(session.getId());
        response.setDate(session.getDate());
        response.setState(session.getState());
        response.setEmployee(employeeConverter.convert(session.getEmployee()));
        response.setStore(storeConverter.storeResponse(session.getStore()));
        response.setUuid(session.getUuid());
        response.setOpenCacheAmount(session.getOpenCacheAmount());
        response.setCloseCacheAmount(session.getCloseCacheAmount());
        response.setOpenDateTime(session.getOpenDateTime());
        response.setCloseDateTime(session.getCloseDateTime());
        return response;
    }
}
