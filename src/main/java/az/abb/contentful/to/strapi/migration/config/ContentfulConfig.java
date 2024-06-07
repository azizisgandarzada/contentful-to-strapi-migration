package az.abb.contentful.to.strapi.migration.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@RequiredArgsConstructor
public class ContentfulConfig {

    private final ObjectMapper objectMapper;

    @Value("classpath:export/export.json")
    private Resource resource;

    @Bean
    @SneakyThrows
    public JsonNode contentfulData() {
        return objectMapper.readValue(resource.getInputStream(), JsonNode.class);
    }

}
