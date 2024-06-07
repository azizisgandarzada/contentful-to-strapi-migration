package az.abb.contentful.to.strapi.migration.component.field;

import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.ID;
import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.LNG;
import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.LON;
import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.REQUIRED;
import static az.abb.contentful.to.strapi.migration.util.ContentfulUtils.getLocalizedValueOrDefault;

import az.abb.contentful.to.strapi.migration.enumuration.EntryFieldType;
import az.abb.contentful.to.strapi.migration.normalization.FieldNormalizer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

@Component
public class LocationField extends EntryField {

    @Override
    public EntryFieldType getType() {
        return EntryFieldType.LOCATION;
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
        ObjectNode location = getLocalizedValueOrDefault(getRawField(), getLocale(), getDefaultLocale()).deepCopy();
        location.set(LNG, location.get(LON));
        location.remove(LON);
        return location;
    }

}
