package az.abb.contentful.to.strapi.migration.util;

import java.util.Arrays;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnumUtils {

    public static boolean equalsAny(Enum<?> value, Enum<?>... values) {
        if (value == null) {
            return false;
        }
        return Arrays.stream(values).anyMatch(enumValue -> enumValue == value);
    }

}
