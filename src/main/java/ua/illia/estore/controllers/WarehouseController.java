package ua.illia.estore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.illia.estore.configuration.exceptions.BindingValidationException;
import ua.illia.estore.converters.impl.WarehouseConverter;
import ua.illia.estore.dto.warehouse.WarehouseCreateForm;
import ua.illia.estore.dto.warehouse.WarehouseResponse;
import ua.illia.estore.services.warehouse.WarehouseService;

@RestController
@RequestMapping("/api/v1/warehouses")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private WarehouseConverter warehouseConverter;

    @Transactional
    @GetMapping
    public Page<WarehouseResponse> search(@PageableDefault(size = 100) Pageable pageable) {
        return warehouseService.search(pageable).map(warehouseConverter::warehouseResponse);
    }

    @Transactional
    @PostMapping
    public WarehouseResponse create(@RequestBody @Validated WarehouseCreateForm warehouseCreateForm,
                                    BindingResult result) {
        if (result.hasErrors()) {
            throw new BindingValidationException(result);
        }
        return warehouseConverter.warehouseResponse(warehouseService.create(warehouseCreateForm));
    }

    @Transactional
    @GetMapping("/{warehouseId}")
    public WarehouseResponse getById(@PathVariable long warehouseId) {
        return warehouseConverter.warehouseResponse(warehouseService.getById(warehouseId));
    }
}
