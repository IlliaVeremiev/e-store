package ua.illia.estore.services.management;

import java.util.Map;

public interface TemplateService {

    String buildTemplate(String name, Map<String, Object> model);
}
