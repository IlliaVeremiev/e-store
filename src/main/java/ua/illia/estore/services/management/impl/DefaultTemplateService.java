package ua.illia.estore.services.management.impl;

import freemarker.template.Configuration;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import ua.illia.estore.services.management.TemplateService;

import java.util.Map;

@Service
public class DefaultTemplateService implements TemplateService {

    @Autowired
    private Configuration fmConfiguration;

    @Override
    @SneakyThrows
    public String buildTemplate(String name, Map<String, Object> model) {
        return FreeMarkerTemplateUtils.processTemplateIntoString(fmConfiguration.getTemplate(name), model);
    }
}
