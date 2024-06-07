package az.abb.contentful.to.strapi.migration.component.field;

import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.ID;
import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.REQUIRED;
import static az.abb.contentful.to.strapi.migration.util.ContentfulUtils.getLocalizedValueOrDefault;

import az.abb.contentful.to.strapi.migration.enumuration.EntryFieldType;
import az.abb.contentful.to.strapi.migration.normalization.FieldNormalizer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SymbolArrayField extends EntryField {

    @Override
    public EntryFieldType getType() {
        return EntryFieldType.SYMBOL_ARRAY;
    }

    @Override
    public String getName() {
        return FieldNormalizer.normalize(getCdaField().get(ID).asText());
    }

    @Override
    public boolean required() {
        return getCdaField().get(REQUIRED).asBoolean();
    }

    @Override
    public Object resolveValue() {
        StringBuilder stringBuilder = new StringBuilder();
        for (JsonNode symbol : getLocalizedValueOrDefault(getRawField(), getLocale(), getDefaultLocale())) {
            stringBuilder.append(symbol.asText()).append(",");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

}
