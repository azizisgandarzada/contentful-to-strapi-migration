package az.abb.contentful.to.strapi.migration.component.field;

import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.ID;
import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.REQUIRED;

import az.abb.contentful.to.strapi.migration.enumuration.EntryFieldType;
import az.abb.contentful.to.strapi.migration.normalization.FieldNormalizer;
import az.abb.contentful.to.strapi.migration.util.ContentfulUtils;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntryArrayField extends EntryField {

    @Override
    public EntryFieldType getType() {
        return EntryFieldType.ENTRY_ARRAY;
    }

    @Override
    public String getName() {
        return FieldNormalizer.normalize(getCdaField().get(ID).asText());
    }

    @Override
    public boolean required() {
        return getCdaField().get(REQUIRED).asBoolean();
    }

    @Override
    public Object resolveValue() {
        List<String> ids = new ArrayList<>();
        for (JsonNode entry : ContentfulUtils.getLocalizedValueOrDefault(getRawField(), getLocale(), getDefaultLocale())) {
            ids.add(ContentfulUtils.getEntryId(entry));
        }
        if (ids.isEmpty()) {
            return null;
        }
        return ids;
    }

}
