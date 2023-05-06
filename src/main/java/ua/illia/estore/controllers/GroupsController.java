package ua.illia.estore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.illia.estore.converters.impl.GroupConverter;
import ua.illia.estore.dto.role.GroupCreateForm;
import ua.illia.estore.dto.role.GroupResponse;
import ua.illia.estore.model.security.enums.Authority;
import ua.illia.estore.services.management.GroupService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupsController {

    @Autowired
    private GroupService groupService;
    @Autowired
    private GroupConverter groupConverter;

    @PostMapping
    public GroupResponse create(@RequestBody GroupCreateForm form) {
        return groupConverter.roleResponse(groupService.create(form));
    }

    @GetMapping
    public List<GroupResponse> getAll() {
        return groupService.getAll()
                .stream()
                .map(groupConverter::roleResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public GroupResponse getById(@PathVariable long id) {
        return groupConverter.roleResponse(groupService.getById(id));
    }

    @PutMapping("/{id}")
    public GroupResponse update(@PathVariable long id,
                                @RequestBody Map<String, Object> form) {
        return groupConverter.roleResponse(groupService.update(id, form));
    }

    @PutMapping("/{roleId}/authorities/{authorityName}")
    public GroupResponse addAuthority(@PathVariable long roleId,
                                      @PathVariable Authority authorityName) {
        return groupConverter.roleResponse(groupService.addAuthority(roleId, authorityName));
    }

    @DeleteMapping("/{roleId}/authorities/{authorityName}")
    public GroupResponse removeAuthority(@PathVariable long roleId,
                                         @PathVariable Authority authorityName) {
        return groupConverter.roleResponse(groupService.removeAuthority(roleId, authorityName));
    }

    @DeleteMapping("/{roleId}")
    public void removeRole(@PathVariable long roleId) {
        groupService.remove(roleId);
    }
}
