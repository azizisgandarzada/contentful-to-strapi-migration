package az.abb.contentful.to.strapi.migration.enumuration;

import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.ITEMS;
import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.LINK_TYPE;
import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.TYPE;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ContentfulLinkType {

    ENTRY("Entry"),
    SYMBOL("Symbol"),
    ASSET("Asset");

    private final String value;

    public static ContentfulLinkType of(JsonNode cdaField) {
        if (cdaField.get(LINK_TYPE) == null && cdaField.get(ITEMS) == null) {
            return null;
        } else if (cdaField.get(LINK_TYPE) == null && cdaField.get(ITEMS) != null && cdaField.get(ITEMS).get(LINK_TYPE) != null) {
            return ContentfulLinkType.of(cdaField.get(ITEMS).get(LINK_TYPE).asText());
        } else if (cdaField.get(LINK_TYPE) == null && cdaField.get(ITEMS) != null && cdaField.get(ITEMS).get(TYPE) != null) {
            return ContentfulLinkType.of(cdaField.get(ITEMS).get(TYPE).asText());
        } else if (cdaField.get(LINK_TYPE) != null) {
            return ContentfulLinkType.of(cdaField.get(LINK_TYPE).asText());
        } else {
            throw new IllegalArgumentException();
        }

    }

    public static ContentfulLinkType of(String type) {
        return Arrays.stream(values()).filter(value -> value.getValue().equals(type)).findFirst().orElseThrow();
    }
}
