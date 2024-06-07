package az.abb.contentful.to.strapi.migration.dao.repository;


import az.abb.contentful.to.strapi.migration.dao.entity.Entry;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryRepository extends JpaRepository<Entry, Integer> {

    Entry findByContentfulId(String contentfulId);

    List<Entry> findByContentfulIdInAndLang(List<String> contentfulIds, String lang);

    Entry findByContentfulIdAndLang(String contentfulId, String lang);

    boolean existsByContentfulIdAndLang(String contentfulId, String lang);

}
