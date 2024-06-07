package az.abb.contentful.to.strapi.migration.enumuration;

import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.TYPE;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EntryFieldType {

    SYMBOL(ContentfulFieldType.SYMBOL, StrapiFieldType.STRING),
    DATE(ContentfulFieldType.DATE, StrapiFieldType.STRING),
    TEXT(ContentfulFieldType.TEXT, StrapiFieldType.TEXT),
    RICH_TEXT(ContentfulFieldType.RICH_TEXT, StrapiFieldType.BLOCKS),
    LOCATION(ContentfulFieldType.LOCATION, StrapiFieldType.LOCATION),
    INTEGER(ContentfulFieldType.INTEGER, StrapiFieldType.INTEGER),
    NUMBER(ContentfulFieldType.NUMBER, StrapiFieldType.DECIMAL),
    BOOLEAN(ContentfulFieldType.BOOLEAN, StrapiFieldType.BOOLEAN),
    ENTRY(ContentfulFieldType.ENTRY, StrapiFieldType.RELATION),
    ASSET(ContentfulFieldType.ASSET, StrapiFieldType.MEDIA),
    ASSET_ARRAY(ContentfulFieldType.ASSET_ARRAY, StrapiFieldType.MEDIA),
    ENTRY_ARRAY(ContentfulFieldType.ENTRY_ARRAY, StrapiFieldType.RELATION),
    SYMBOL_ARRAY(ContentfulFieldType.SYMBOL_ARRAY, StrapiFieldType.TEXT),
    OBJECT(ContentfulFieldType.OBJECT, StrapiFieldType.JSON);

    private final ContentfulFieldType contentfulType;
    private final StrapiFieldType strapiType;

    public static EntryFieldType of(JsonNode cdaField) {
        String type = cdaField.get(TYPE).asText();
        ContentfulLinkType linkType = ContentfulLinkType.of(cdaField);
        if (linkType == null) {
            return Arrays.stream(values())
                    .filter(value -> value.getContentfulType().getValue().equals(type))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
        if (type.equals(ContentfulFieldType.ENTRY.getValue()) && ContentfulLinkType.ENTRY == linkType) {
            return EntryFieldType.ENTRY;
        } else if (type.equals(ContentfulFieldType.ENTRY_ARRAY.getValue()) && ContentfulLinkType.ENTRY == linkType) {
            return EntryFieldType.ENTRY_ARRAY;
        } else if (type.equals(ContentfulFieldType.ASSET.getValue()) && ContentfulLinkType.ASSET == linkType) {
            return EntryFieldType.ASSET;
        } else if (type.equals(ContentfulFieldType.ASSET_ARRAY.getValue()) && ContentfulLinkType.ASSET == linkType) {
            return EntryFieldType.ASSET_ARRAY;
        } else if (type.equals(ContentfulFieldType.SYMBOL_ARRAY.getValue()) && ContentfulLinkType.SYMBOL == linkType) {
            return EntryFieldType.SYMBOL_ARRAY;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
