package ua.illia.estore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.illia.estore.configuration.security.EmployeeUserDetails;
import ua.illia.estore.converters.impl.KassaSessionConverter;
import ua.illia.estore.converters.impl.StoreConverter;
import ua.illia.estore.dto.kassasession.KassaSessionResponse;
import ua.illia.estore.dto.store.StoreCreateForm;
import ua.illia.estore.dto.store.StoreResponse;
import ua.illia.estore.dto.store.StoreSearchForm;
import ua.illia.estore.services.store.StoreService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreConverter storeConverter;

    @Transactional
    @PostMapping
    public StoreResponse create(@RequestBody StoreCreateForm form) {
        return storeConverter.storeResponse(storeService.create(form));
    }

    @Transactional
    @GetMapping("/{storeId}")
    public StoreResponse getById(@PathVariable long storeId) {
        return storeConverter.storeResponse(storeService.getById(storeId));
    }

    @Transactional
    @GetMapping
    public Page<StoreResponse> search(StoreSearchForm form,
                                      @PageableDefault(size = 100) Pageable pageable) {
        return storeService.search(form, pageable)
                .map(storeConverter::storeResponse);
    }

    @Transactional
    @PutMapping("/{storeId}")
    public StoreResponse update(@PathVariable long storeId,
                                @RequestBody Map<String, Object> data) {
        return storeConverter.storeResponse(storeService.update(storeId, data));
    }

    @Autowired
    private KassaSessionConverter kassaSessionConverter;

    @Transactional
    @GetMapping("/{storeId}/kassa/open-session/{email}")
    public List<KassaSessionResponse> getCurrentSession(@PathVariable long storeId,
                                                        @PathVariable String email) {
        return storeService.getOpenKassaForEmployee(storeId, email)
                .stream()
                .map(kassaSessionConverter::convert)
                .collect(Collectors.toList());
    }

    @Transactional
    @PostMapping("/{storeId}/kassa/sessions")
    public KassaSessionResponse openNewSession(@PathVariable long storeId,
                                               @AuthenticationPrincipal EmployeeUserDetails userDetails) {
        return kassaSessionConverter.convert(storeService.createSession(storeId, userDetails.getUser()));
    }

    @Transactional
    @GetMapping("/kassa/sessions/{sessionId}")
    public KassaSessionResponse getSessionById(@PathVariable long sessionId) {
        return kassaSessionConverter.convert(storeService.getSession(sessionId));
    }
}
