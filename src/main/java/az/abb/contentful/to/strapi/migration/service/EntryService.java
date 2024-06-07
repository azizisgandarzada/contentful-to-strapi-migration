package az.abb.contentful.to.strapi.migration.service;

import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.CONNECT;
import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.CONTENT_TYPES;
import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.DATA;
import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.ENTRIES;
import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.FIELDS;
import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.ID;
import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.LOCALE;
import static az.abb.contentful.to.strapi.migration.enumuration.EntryFieldType.ASSET;
import static az.abb.contentful.to.strapi.migration.enumuration.EntryFieldType.ASSET_ARRAY;
import static az.abb.contentful.to.strapi.migration.enumuration.EntryFieldType.ENTRY;
import static az.abb.contentful.to.strapi.migration.enumuration.EntryFieldType.ENTRY_ARRAY;
import static az.abb.contentful.to.strapi.migration.util.ContentfulUtils.getContentTypeId;
import static az.abb.contentful.to.strapi.migration.util.ContentfulUtils.getContentTypeIdFromEntry;
import static java.util.Collections.emptyMap;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliteratorUnknownSize;

import az.abb.contentful.to.strapi.migration.client.StrapiClient;
import az.abb.contentful.to.strapi.migration.component.field.EntryField;
import az.abb.contentful.to.strapi.migration.component.handler.EntryFieldHandler;
import az.abb.contentful.to.strapi.migration.dao.entity.Asset;
import az.abb.contentful.to.strapi.migration.dao.entity.Entry;
import az.abb.contentful.to.strapi.migration.dao.repository.AssetRepository;
import az.abb.contentful.to.strapi.migration.dao.repository.EntryRepository;
import az.abb.contentful.to.strapi.migration.normalization.ContentTypeNormalizer;
import az.abb.contentful.to.strapi.migration.util.CollectionUtils;
import az.abb.contentful.to.strapi.migration.util.ContentfulUtils;
import az.abb.contentful.to.strapi.migration.util.EnumUtils;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EntryService {

    private final JsonNode contentfulData;
    private final EntryFieldHandler entryFieldHandler;
    private final EntryRepository entryRepository;
    private final AssetRepository assetRepository;
    private final StrapiClient strapiClient;

    @Value("${contentful.default-locale}")
    private Locale defaultLocale;

    @Value("${contentful.other-locales}")
    private List<Locale> otherLocales;

    @Value("${contentful.skipped-content-type-ids}")
    private List<String> skippedContentTypeIds;

    public void createEntries() {
        log.info("Creating entries");
        var contentTypes = getContentTypes();
        for (JsonNode entry : contentfulData.get(ENTRIES)) {
            JsonNode contentType = contentTypes.get(getContentTypeIdFromEntry(entry));
            String contentTypeId = ContentTypeNormalizer.normalizePluralName(getContentTypeId(contentType));
            if (skippedContentTypeIds.contains(contentTypeId)) {
                continue;
            }
            String entryId = ContentfulUtils.getEntryId(entry);
            if (existsByContentfulIdAndLang(entryId, defaultLocale)) {
                continue;
            }
            Map<String, Object> data = buildEntryData(contentType, entry, defaultLocale);
            if (data.isEmpty()) {
                continue;
            }
            JsonNode response = strapiClient.createEntry(contentTypeId, Map.of(DATA, data));
            int strapiId = response.get(DATA).get(ID).asInt();
            saveEntry(entryId, strapiId, contentTypeId, defaultLocale);
        }
        log.info("Created entries");
    }

    public void createLocalizedEntries() {
        log.info("Creating localized entries");
        var contentTypes = getContentTypes();
        otherLocales.forEach(locale -> {
            for (JsonNode entry : contentfulData.get(ENTRIES)) {
                JsonNode contentType = contentTypes.get(getContentTypeIdFromEntry(entry));
                String contentTypeId = ContentTypeNormalizer.normalizePluralName(getContentTypeId(contentType));
                if (skippedContentTypeIds.contains(contentTypeId)) {
                    continue;
                }
                String entryId = ContentfulUtils.getEntryId(entry);
                var defaultEntry = entryRepository.findByContentfulIdAndLang(entryId, defaultLocale.getLanguage());
                if (defaultEntry == null) {
                    continue;
                }
                if (existsByContentfulIdAndLang(entryId, locale)) {
                    continue;
                }
                Map<String, Object> data = buildEntryData(contentType, entry, locale);
                if (data.isEmpty()) {
                    continue;
                }
                data.put(LOCALE, locale.getLanguage());
                JsonNode response = strapiClient.createLocalizedEntry(defaultEntry.getStrapiId(), contentTypeId, data);
                int strapiId = response.get(ID).asInt();
                saveEntry(entryId, strapiId, contentTypeId, locale);
            }
        });
        log.info("Created localized entries");
    }

    public void referEntries() {
        log.info("Referring entries");
        var contentTypes = getContentTypes();
        var locales = new ArrayList<Locale>();
        locales.add(defaultLocale);
        locales.addAll(otherLocales);
        locales.forEach(locale -> {
            for (JsonNode entry : contentfulData.get(ENTRIES)) {
                JsonNode contentType = contentTypes.get(getContentTypeIdFromEntry(entry));
                String contentTypeId = ContentTypeNormalizer.normalizePluralName(getContentTypeId(contentType));
                if (skippedContentTypeIds.contains(contentTypeId)) {
                    return;
                }
                String entryId = ContentfulUtils.getEntryId(entry);
                Entry referencedEntry = entryRepository.findByContentfulIdAndLang(entryId, locale.getLanguage());
                if (referencedEntry == null) {
                    return;
                }
                Map<String, JsonNode> rawFields = getRawFields(entry);
                if (rawFields.isEmpty()) {
                    continue;
                }
                var fields = getFields(contentType);
                rawFields.keySet()
                        .forEach(key -> {
                            JsonNode cdaField = fields.get(key);
                            JsonNode rawField = rawFields.get(key);
                            Map<String, Object> data = buildEntryConnectData(cdaField, rawField, locale);
                            if (data.isEmpty()) {
                                return;
                            }
                            strapiClient.connect(referencedEntry.getStrapiId(), referencedEntry.getContentTypeId(), data);
                        });
            }
        });
        log.info("Referred entries");
    }

    private Map<String, Object> buildEntryData(JsonNode contentType, JsonNode entry, Locale locale) {
        var fields = getFields(contentType);
        Map<String, JsonNode> rawFields = getRawFields(entry);
        if (rawFields.isEmpty()) {
            return emptyMap();
        }
        var data = new HashMap<String, Object>();
        rawFields.keySet()
                .forEach(key -> {
                    JsonNode cdaField = fields.get(key);
                    JsonNode rawField = rawFields.get(key);
                    EntryField entryField = entryFieldHandler.handle(cdaField, rawField, locale, defaultLocale);
                    if (EnumUtils.equalsAny(entryField.getType(), ENTRY, ENTRY_ARRAY)) {
                        return;
                    }
                    Object value = entryField.resolveValue();
                    if (value == null && entryField.required()) {
                        throw new IllegalArgumentException("Value can not be null");
                    } else if (value == null) {
                        return;
                    }
                    if (entryField.getType() == ASSET) {
                        Asset asset = assetRepository.findByContentfulIdAndLang(value.toString(), locale.getLanguage());
                        if (asset == null) {
                            throw new IllegalArgumentException("Asset not found");
                        }
                        data.put(entryField.getName(), asset.getStrapiId());
                    } else if (entryField.getType() == ASSET_ARRAY) {
                        List<Asset> assets =
                                assetRepository.findAllByContentfulIdInAndLang(CollectionUtils.convertToList(value,
                                        String.class), locale.getLanguage());
                        if (assets.isEmpty()) {
                            throw new IllegalArgumentException("Assets not found");
                        }
                        List<Integer> strapiIds = assets.stream().map(Asset::getStrapiId).toList();
                        data.put(entryField.getName(), strapiIds);
                    } else {
                        data.put(entryField.getName(), value);
                    }

                });
        return data;
    }

    private Map<String, Object> buildEntryConnectData(JsonNode cdaField,
                                                      JsonNode rawField,
                                                      Locale locale) {

        EntryField entryField = entryFieldHandler.handle(cdaField, rawField, locale, defaultLocale);
        if (!EnumUtils.equalsAny(entryField.getType(), ENTRY, ENTRY_ARRAY)) {
            return emptyMap();
        }
        Object value = entryField.resolveValue();
        if (value == null && entryField.required()) {
            throw new IllegalArgumentException("Value can not be null");
        } else if (value == null) {
            return emptyMap();
        }
        List<String> entryIds = CollectionUtils.convertToList(value, String.class);
        List<Integer> strapiIds = entryRepository.findByContentfulIdInAndLang(entryIds, locale.getLanguage())
                .stream()
                .map(Entry::getStrapiId)
                .toList();
        if (strapiIds.isEmpty()) {
            return emptyMap();
        }
        var data = new HashMap<String, Object>();
        var connect = new HashMap<String, Object>();
        connect.put(CONNECT, strapiIds);
        data.put(entryField.getName(), connect);
        return Map.of(DATA, data);
    }

    private Map<String, JsonNode> getRawFields(JsonNode entry) {
        return StreamSupport.stream(spliteratorUnknownSize(entry.get(FIELDS).fields(), ORDERED), false)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<String, JsonNode> getFields(JsonNode contentType) {
        var fields = new HashMap<String, JsonNode>();
        for (JsonNode field : contentType.get(FIELDS)) {
            fields.put(field.get(ID).asText(), field);
        }
        return fields;
    }

    public Map<String, JsonNode> getContentTypes() {
        var contentTypes = new HashMap<String, JsonNode>();
        for (JsonNode contentType : contentfulData.get(CONTENT_TYPES)) {
            contentTypes.put(getContentTypeId(contentType), contentType);
        }
        return contentTypes;
    }

    private boolean existsByContentfulIdAndLang(String contentfulId, Locale locale) {
        return entryRepository.existsByContentfulIdAndLang(contentfulId, locale.getLanguage());
    }

    private void saveEntry(String entryId, int strapiId, String contentTypeId, Locale locale) {
        var entry = new Entry();
        entry.setContentfulId(entryId);
        entry.setStrapiId(strapiId);
        entry.setLang(locale.getLanguage());
        entry.setContentTypeId(contentTypeId);
        entryRepository.save(entry);
    }

}
