package az.abb.contentful.to.strapi.migration.component.field;

import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.ID;
import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.REQUIRED;

import az.abb.contentful.to.strapi.migration.enumuration.EntryFieldType;
import az.abb.contentful.to.strapi.migration.normalization.FieldNormalizer;
import az.abb.contentful.to.strapi.migration.util.ContentfulUtils;
import org.springframework.stereotype.Component;

@Component
public class NumberField extends EntryField {

    @Override
    public EntryFieldType getType() {
        return EntryFieldType.NUMBER;
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
        return ContentfulUtils.getLocalizedValueOrDefault(getRawField(), getLocale(), getDefaultLocale()).asDouble();
    }

}
