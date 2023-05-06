package ua.illia.estore.services.management;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import ua.illia.estore.configuration.camunda.CamundaFormBasedGroupQuery;
import ua.illia.estore.dto.role.GroupCreateForm;
import ua.illia.estore.dto.role.RoleSearchForm;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.model.security.Group;
import ua.illia.estore.model.security.enums.Authority;

import java.util.List;
import java.util.Map;

public interface GroupService {

    Group create(GroupCreateForm form);

    Group getById(long id);

    List<Group> search(RoleSearchForm form, Pageable pageable);

    List<Group> getEmployeeRoles(Employee employee);

    Group addAuthority(long roleId, Authority authority);

    Group removeAuthority(long roleId, Authority authority);

    Group remove(long id);

    List<Group> getRolesByIds(List<Long> ids);

    List<Group> getAll();

    List<GrantedAuthority> getEmployeeAuthorities(Employee employee);

    Group getByName(String groupId);

    List<Group> searchAll(CamundaFormBasedGroupQuery camundaFormBasedGroupQuery);

    Group update(long id, Map<String, Object> form);

    Group save(Group group);

    Group getByUid(String uid);
}
