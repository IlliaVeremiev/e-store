package ua.illia.estore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.illia.estore.configuration.security.EmployeeUserDetails;
import ua.illia.estore.converters.impl.KassaSessionConverter;
import ua.illia.estore.dto.kassasession.KassaSessionCreateForm;
import ua.illia.estore.dto.kassasession.KassaSessionResponse;
import ua.illia.estore.dto.kassasession.KassaSessionSearchForm;
import ua.illia.estore.services.store.StoreService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/kassa")
public class KassaController {

    @Autowired
    private StoreService storeService;
    @Autowired
    private KassaSessionConverter kassaSessionConverter;

    @Transactional
    @GetMapping("/sessions")
    public List<KassaSessionResponse> search(KassaSessionSearchForm form) {
        return storeService.searchSession(form).stream().map(kassaSessionConverter::convert).collect(Collectors.toList());
    }

    @Transactional
    @PostMapping("/sessions")
    public KassaSessionResponse create(@RequestBody KassaSessionCreateForm form,
                                       @AuthenticationPrincipal EmployeeUserDetails userDetails) {
        return kassaSessionConverter.convert(storeService.createSession(form, userDetails.getUser()));
    }
}
