package az.abb.contentful.to.strapi.migration.enumuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ContentfulFieldType {

    SYMBOL("Symbol"),
    DATE("Date"),
    TEXT("Text"),
    RICH_TEXT("RichText"),
    LOCATION("Location"),
    INTEGER("Integer"),
    NUMBER("Number"),
    BOOLEAN("Boolean"),
    ENTRY("Link"),
    ASSET("Link"),
    ASSET_ARRAY("Array"),
    ENTRY_ARRAY("Array"),
    SYMBOL_ARRAY("Array"),
    OBJECT("Object");

    private final String value;

}
