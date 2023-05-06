package ua.illia.estore.injectors;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.transaction.annotation.Transactional;
import ua.illia.estore.configuration.exceptions.InternalServerError;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public abstract class EntityDataInjector<T> {
    /**
     * Map<FieldName, BiConsumer<T, value>>
     */
    protected Map<String, BiConsumer<T, ? extends Object>> fieldInjectors = new HashMap<>();

    /**
     * Map<FieldName, BiConsumer<BiConsumer<AuthenticationPattern, <T, value>>>
     */
    protected Map<String, BiConsumer<String, BiConsumer<T, ?>>> authorizedFieldInjectors = new HashMap<>();

    /**
     * Applies all values from <b>data</b> to <b>entity</b>
     *
     * @param entity entity to fill
     * @param data   map with data
     * @return filled entity (same as 1st parameter)
     */
    @Transactional
    public T inject(T entity, Map<String, Object> data) {
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            BiConsumer<T, Object> injector = (BiConsumer<T, Object>) fieldInjectors.get(entry.getKey());
            if (injector == null) {
                throw new IllegalStateException("Injector for field: '" + entry.getKey() + "' does not exists");
            }
            try {
                injector.accept(entity, entry.getValue());
            } catch (Exception e) {
                throw new IllegalStateException("Unable to inject field '" + entry.getKey() + "' with " + entry.getValue().getClass().getName() + " value '" + entry.getValue() + "'", e);
            }
        }
        return entity;
    }

    @Transactional
    public T inject(T entity, Object data) {
        try {
            return inject(entity, PropertyUtils.describe(data));
        } catch (Exception e) {
            throw new InternalServerError("Unable to map " + data.getClass().getName() + " to " + entity.getClass().getName(), "injector.map", e);
        }
    }

    protected <V> void field(String name, BiConsumer<T, V> function) {
        fieldInjectors.put(name, function);
    }

    protected void ignore(String name) {
        field(name, (t, o) -> {
        });
    }
}
