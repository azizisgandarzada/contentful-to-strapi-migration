package az.abb.contentful.to.strapi.migration.dao.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "entries")
@Data
public class Entry {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    private String contentfulId;
    private Integer strapiId;
    private String contentTypeId;
    private String lang;

}
