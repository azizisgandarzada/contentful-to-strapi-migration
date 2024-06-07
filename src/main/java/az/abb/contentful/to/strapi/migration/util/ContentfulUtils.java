package az.abb.contentful.to.strapi.migration.util;

import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.CONTENT_TYPE;
import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.ID;
import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.SYS;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Locale;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ContentfulUtils {

    public static String getContentTypeId(JsonNode contentType) {
        return contentType.get(SYS).get(ID).asText();
    }

    public static String getAssetId(JsonNode asset) {
        return asset.get(SYS).get(ID).asText();
    }

    public static String getEntryId(JsonNode entry) {
        return entry.get(SYS).get(ID).asText();
    }

    public static String getContentTypeIdFromEntry(JsonNode entry) {
        return entry.get(SYS).get(CONTENT_TYPE).get(SYS).get(ID).asText();
    }

    public static JsonNode getLocalizedValue(JsonNode rawField, Locale locale) {
        return rawField.get(locale.toLanguageTag());
    }

    public static JsonNode getLocalizedValueOrDefault(JsonNode rawField, Locale locale, Locale defaultLocale) {
        JsonNode localizedValue = rawField.get(locale.toLanguageTag());
        if (localizedValue == null) {
            return rawField.get(defaultLocale.toLanguageTag());
        }
        return localizedValue;
    }

}
