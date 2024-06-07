package az.abb.contentful.to.strapi.migration.dao.repository;


import az.abb.contentful.to.strapi.migration.dao.entity.Asset;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Integer> {

    Asset findByContentfulIdAndLang(String contentfulId, String lang);

    List<Asset> findAllByContentfulIdInAndLang(List<String> contentfulIds, String lang);

    boolean existsByContentfulIdAndLang(String contentfulId, String lang);

}
