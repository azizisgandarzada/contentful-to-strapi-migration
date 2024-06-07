package az.abb.contentful.to.strapi.migration.component.handler;

import az.abb.contentful.to.strapi.migration.component.field.EntryField;
import az.abb.contentful.to.strapi.migration.enumuration.EntryFieldType;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntryFieldHandler {

    private static final Map<EntryFieldType, EntryField> FIELD_MAP = new EnumMap<>(EntryFieldType.class);

    private final List<EntryField> entryFields;

    @PostConstruct
    private void init() {
        entryFields.forEach(field -> FIELD_MAP.put(field.getType(), field));
    }

    public EntryField handle(JsonNode cdaField, JsonNode rawField, Locale locale, Locale defaultLocale) {
        EntryFieldType entryFieldType = EntryFieldType.of(cdaField);
        EntryField entryField = FIELD_MAP.get(entryFieldType);
        entryField.configure(cdaField, rawField, locale, defaultLocale);
        return entryField;
    }

}
