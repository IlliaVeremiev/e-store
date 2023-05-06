package ua.illia.estore.services.management.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.illia.estore.configuration.camunda.CamundaFormBasedGroupQuery;
import ua.illia.estore.configuration.exceptions.NotFoundException;
import ua.illia.estore.dto.role.GroupCreateForm;
import ua.illia.estore.dto.role.RoleSearchForm;
import ua.illia.estore.injectors.impl.RoleInjector;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.model.security.Group;
import ua.illia.estore.model.security.enums.Authority;
import ua.illia.estore.repositories.GroupRepository;
import ua.illia.estore.search.SpecificationList;
import ua.illia.estore.services.management.EmployeeService;
import ua.illia.estore.services.management.GroupService;
import ua.illia.estore.utils.ServiceUtils;
import ua.illia.estore.validation.validators.impl.RoleValidator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DefaultGroupService implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private RoleInjector roleInjector;

    @Autowired
    private RoleValidator roleValidator;

    @Autowired
    private EmployeeService employeeService;


    @Override
    @Transactional
    public Group create(GroupCreateForm form) {
        Group group = new Group();
        roleInjector.inject(group, form);
        roleValidator.validate(group);
        return groupRepository.save(group);
    }

    @Override
    public Group getById(long id) {
        return groupRepository.findById(id)
                .orElseThrow(ServiceUtils.notFound("Role", "id", id));
    }

    @Override
    public List<Group> search(RoleSearchForm form, Pageable pageable) {
        SpecificationList<Group> specification = new SpecificationList<>();
        if (StringUtils.isNotEmpty(form.getQuery())) {
            specification.add((r, q, b) -> b.isTrue(b.function("@@", Boolean.class, b.function("concat", String.class,
                    b.function("to_tsvector", Map.class, r.get("name")),
                    b.function("to_tsvector", Map.class, r.get("description")),
                    b.function("to_tsvector", Map.class, r.get("authorities"))
            ), b.function("plainto_tsquery", Map.class, b.literal(form.getQuery())))));
        }
        return groupRepository.findAll(specification, pageable);
    }

    @Override
    public List<Group> getEmployeeRoles(Employee employee) {
        return groupRepository.findAllByEmployees(employee);
    }

    @Override
    public Group addAuthority(long roleId, Authority authority) {
        Group group = getById(roleId);
        group.getAuthorities().add(authority);
        return groupRepository.save(group);
    }

    @Override
    public Group removeAuthority(long roleId, Authority authority) {
        Group group = getById(roleId);
        group.getAuthorities().removeIf(authority::equals);
        return groupRepository.save(group);
    }

    @Override
    public Group remove(long roleId) {
        Group group = getById(roleId);
        groupRepository.delete(group);
        return group;
    }

    @Override
    public List<Group> getRolesByIds(List<Long> ids) {
        return groupRepository.findAllById(ids);
    }

    @Override
    public List<Group> getAll() {
        return groupRepository.findAll();
    }

    @Override
    @Transactional
    public List<GrantedAuthority> getEmployeeAuthorities(Employee employee) {
        return getEmployeeRoles(employee).stream()
                .flatMap(r -> r.getAuthorities().stream())
                .collect(Collectors.toList());
    }

    @Override
    public Group getByName(String name) {
        return groupRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Role with name: " + name + " not found", "role.name"));
    }

    @Override
    public List<Group> searchAll(CamundaFormBasedGroupQuery form) {
        SpecificationList<Group> specification = new SpecificationList<>();
        if (form.getName() != null) {
            specification.add((r, q, b) -> b.like(r.get("name"), form.getName()));
        }
        if (form.getUserId() != null) {
            specification.add((r, q, b) -> b.equal(r.join("employees").get("email"), form.getUserId()));
        }
        if (form.getId() != null) {
            specification.add((r, q, b) -> b.equal(r.get("uid"), form.getId()));
        }
        return groupRepository.findAll(specification, PageRequest.of(0, Integer.MAX_VALUE));
    }

    @Override
    public Group update(long id, Map<String, Object> form) {
        Group group = getById(id);
        group = roleInjector.inject(group, form);
        roleValidator.validate(group);
        groupRepository.save(group);
        return getById(id);
    }

    @Override
    public Group save(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public Group getByUid(String uid) {
        return groupRepository.findByUid(uid)
                .orElseThrow(ServiceUtils.notFound("Group", "uid", uid));
    }
}
