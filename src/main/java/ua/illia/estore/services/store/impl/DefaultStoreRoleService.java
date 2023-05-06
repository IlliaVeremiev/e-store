package ua.illia.estore.services.store.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.illia.estore.model.security.StoreRole;
import ua.illia.estore.model.security.StoreRoleEntry;
import ua.illia.estore.repositories.StoreRoleEntryRepository;
import ua.illia.estore.repositories.StoreRoleRepository;
import ua.illia.estore.services.store.StoreRoleService;

import java.util.List;

@Service
public class DefaultStoreRoleService implements StoreRoleService {

    @Autowired
    private StoreRoleEntryRepository storeRoleEntryRepository;

    @Autowired
    private StoreRoleRepository storeRoleRepository;

    @Override
    public StoreRole create(StoreRole role) {
        return storeRoleRepository.save(role);
    }

    @Override
    public List<StoreRoleEntry> getRolesByIds(List<Long> ids) {
        return storeRoleEntryRepository.findAllById(ids);
    }

}
