package az.abb.contentful.to.strapi.migration.component.field;

import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.ID;
import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.REQUIRED;

import az.abb.contentful.to.strapi.migration.enumuration.EntryFieldType;
import az.abb.contentful.to.strapi.migration.normalization.FieldNormalizer;
import org.springframework.stereotype.Component;

@Component
public class RichTextField extends EntryField {

    @Override
    public EntryFieldType getType() {
        return EntryFieldType.RICH_TEXT;
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
        return null;
    }

}
