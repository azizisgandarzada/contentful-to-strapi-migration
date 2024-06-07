package az.abb.contentful.to.strapi.migration.normalization;

import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FieldNormalizer {

    private static final Map<String, String> MAPPING = Map.of(
            "id", "identifier",
            "created_at", "createdAt",
            "updated_at", "updatedAt",
            "published_at", "publishedAt",
            "created_by_id", "createdById",
            "updated_by_id", "updatedById",
            "created_by", "createdBy",
            "updated_by", "updatedBy");

    public static String normalize(String name) {
        return MAPPING.getOrDefault(name, name);
    }

}
