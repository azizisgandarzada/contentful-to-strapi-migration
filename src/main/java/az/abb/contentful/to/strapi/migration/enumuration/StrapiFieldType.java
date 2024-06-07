package az.abb.contentful.to.strapi.migration.enumuration;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum StrapiFieldType {

    STRING("string"),
    TEXT("text"),
    BOOLEAN("boolean"),
    BLOCKS("blocks"),
    JSON("json"),
    DECIMAL("decimal"),
    INTEGER("integer"),
    BIGINTEGER("biginteger"),
    FLOAT("float"),
    EMAIL("email"),
    ENUMERATION("enumeration"),
    RELATION("relation"),
    UID("uid"),
    LOCATION("customField"),
    MEDIA("media");

    private final String value;

    public static StrapiFieldType of(String type) {
        return Arrays.stream(values()).filter(value -> value.getValue().equals(type)).findFirst().orElseThrow();
    }
}
