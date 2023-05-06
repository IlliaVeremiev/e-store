package ua.illia.estore.configuration;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageImpl;
import ua.illia.estore.serializers.BigDecimalSerializer;
import ua.illia.estore.serializers.PageSerializer;
import ua.illia.estore.serializers.ProductsPageSerializer;
import ua.illia.estore.utils.CountingPage;

import java.math.BigDecimal;

@Configuration
public class JacksonConfiguration {

    @Bean
    public Module jacksonPageWithJsonViewModule() {
        SimpleModule module = new SimpleModule("klkl-page-with-jsonview", Version.unknownVersion());
        module.addSerializer(PageImpl.class, new PageSerializer());
        module.addSerializer(CountingPage.class, new ProductsPageSerializer());
        module.addSerializer(BigDecimal.class, new BigDecimalSerializer());
        return module;
    }
}
