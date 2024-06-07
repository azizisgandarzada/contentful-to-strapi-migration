package az.abb.contentful.to.strapi.migration.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MigrationService {

    private final EntryService entryService;
    private final AssetService assetService;

    @PostConstruct
    public void migrate() {
        assetService.uploadAssets();
        entryService.createEntries();
        entryService.createLocalizedEntries();
        entryService.referEntries();
    }

}
