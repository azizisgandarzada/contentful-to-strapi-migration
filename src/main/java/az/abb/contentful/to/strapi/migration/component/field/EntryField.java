package az.abb.contentful.to.strapi.migration.component.field;

import az.abb.contentful.to.strapi.migration.enumuration.EntryFieldType;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Locale;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public abstract class EntryField {

    private JsonNode cdaField;
    private JsonNode rawField;
    private Locale locale;
    private Locale defaultLocale;

    public abstract EntryFieldType getType();

    public abstract String getName();

    public abstract boolean required();

    public abstract Object resolveValue();

    public void configure(JsonNode cdaField, JsonNode rawField, Locale locale, Locale defaultLocale) {
        this.cdaField = cdaField;
        this.rawField = rawField;
        this.locale = locale;
        this.defaultLocale = defaultLocale;
    }

}
