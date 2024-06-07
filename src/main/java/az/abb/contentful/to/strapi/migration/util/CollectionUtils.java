package az.abb.contentful.to.strapi.migration.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CollectionUtils {

    public static <T> List<T> convertToList(Object obj, Class<T> clazz) {
        List<T> list;
        if (obj.getClass().isArray()) {
            list = Arrays.asList((T[]) obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((List<T>) obj);
        } else {
            list = List.of((T) obj);
        }
        return list;
    }

}
