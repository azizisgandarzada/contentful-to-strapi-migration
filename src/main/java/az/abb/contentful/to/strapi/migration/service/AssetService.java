package az.abb.contentful.to.strapi.migration.service;

import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.ASSETS;
import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.FIELDS;
import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.FILE;
import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.ID;
import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.SLASH;
import static az.abb.contentful.to.strapi.migration.constant.ContentfulConstants.URL;
import static az.abb.contentful.to.strapi.migration.util.ContentfulUtils.getAssetId;
import static az.abb.contentful.to.strapi.migration.util.ContentfulUtils.getLocalizedValueOrDefault;

import az.abb.contentful.to.strapi.migration.client.StrapiClient;
import az.abb.contentful.to.strapi.migration.dao.entity.Asset;
import az.abb.contentful.to.strapi.migration.dao.repository.AssetRepository;
import az.abb.contentful.to.strapi.migration.util.CustomMultipartFile;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@Slf4j
public class AssetService {

    private final JsonNode contentfulData;
    private final StrapiClient strapiClient;
    private final AssetRepository assetRepository;

    @Value("${contentful.default-locale}")
    private Locale defaultLocale;

    @Value("${contentful.other-locales}")
    private List<Locale> otherLocales;

    public void uploadAssets() {
        log.info("Uploading assets");
        var locales = new ArrayList<Locale>();
        locales.add(defaultLocale);
        locales.addAll(otherLocales);
        locales.forEach(locale -> {
            for (JsonNode asset : contentfulData.get(ASSETS)) {
                String contentfulId = getAssetId(asset);
                if (existsByContentfulIdAndLang(contentfulId, locale)) {
                    continue;
                }
                JsonNode file = asset.get(FIELDS).get(FILE);
                JsonNode localizedValue = getLocalizedValueOrDefault(file, locale, defaultLocale);
                String[] urlParts = localizedValue.get(URL).asText().split(SLASH);
                String host = urlParts[2];
                String spaceId = urlParts[3];
                String assetId = urlParts[4];
                String hash = urlParts[5];
                String assetName = urlParts[6];
                String path = String.join(SLASH, host, spaceId, assetId, hash, assetName);
                MultipartFile multipartFile = buildMultipartfile(path);
                JsonNode response = strapiClient.uploadFile(multipartFile);
                int strapiId = response.get(0).get(ID).asInt();
                saveAsset(contentfulId, strapiId, locale);
            }
        });
        log.info("Uploaded assets");
    }

    @SneakyThrows
    private MultipartFile buildMultipartfile(String path) {
        return new CustomMultipartFile(ResourceUtils.getFile("classpath:export/" + path));
    }

    private boolean existsByContentfulIdAndLang(String contentfulId, Locale locale) {
        return assetRepository.existsByContentfulIdAndLang(contentfulId, locale.getLanguage());
    }

    private void saveAsset(String contentfulId, int strapiId, Locale locale) {
        var asset = new Asset();
        asset.setContentfulId(contentfulId);
        asset.setStrapiId(strapiId);
        asset.setLang(locale.getLanguage());
        assetRepository.save(asset);
    }

}
