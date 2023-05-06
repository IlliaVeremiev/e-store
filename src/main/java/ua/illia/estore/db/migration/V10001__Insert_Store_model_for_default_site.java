package ua.illia.estore.db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.illia.estore.dto.store.StoreCreateForm;
import ua.illia.estore.services.store.StoreService;

@Component
public class V10001__Insert_Store_model_for_default_site extends BaseJavaMigration {

    @Lazy
    @Autowired
    private StoreService storeService;

    @Override
    @Transactional
    public void migrate(Context context) throws Exception {
        StoreCreateForm form = new StoreCreateForm();
        form.setName("Site");
        form.setUid("site");
        storeService.create(form);
    }
}
