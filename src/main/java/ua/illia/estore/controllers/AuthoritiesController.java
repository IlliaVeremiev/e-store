package ua.illia.estore.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.illia.estore.model.security.enums.Authority;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/authorities")
public class AuthoritiesController {

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<Authority> getAll() {
        return Arrays.asList(Authority.values());
    }
}
