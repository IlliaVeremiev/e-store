package ua.illia.estore.services.store;

import ua.illia.estore.model.security.StoreRole;
import ua.illia.estore.model.security.StoreRoleEntry;

import java.util.List;

public interface StoreRoleService {

    StoreRole create(StoreRole role);

    List<StoreRoleEntry> getRolesByIds(List<Long> ids);
}
