package az.abb.contentful.to.strapi.migration.normalization;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ContentTypeNormalizer {

    public static String normalizeUid(String name) {
        return "api::" + normalizeSingularName(name) + "." + normalizeSingularName(name);
    }

    public static String normalizePluralName(String name) {
        return name.toLowerCase() + "-p";
    }

    public static String normalizeSingularName(String name) {
        return name.toLowerCase() + "-s";
    }

}
