package az.abb.contentful.to.strapi.migration.client;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "strapiClient", url = "http://localhost:1337/")
public interface StrapiClient {

    @PostMapping(value = "api/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    JsonNode uploadFile(@RequestPart MultipartFile files);

    @PostMapping(value = "api/{contentTypeId}")
    JsonNode createEntry(@PathVariable String contentTypeId,
                         @RequestBody Map<String, Object> entry);

    @PostMapping(value = "api/{contentTypeId}/{id}/localizations")
    JsonNode createLocalizedEntry(@PathVariable int id,
                                  @PathVariable String contentTypeId,
                                  @RequestBody Map<String, Object> entry);

    @PutMapping(value = "api/{contentTypeId}/{id}")
    JsonNode connect(@PathVariable int id,
                     @PathVariable String contentTypeId,
                     @RequestBody Map<String, Object> entry);

}
