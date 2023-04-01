package tbs.api_server.objects.jpa;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "tag", schema = "exam_db", catalog = "")
public class Jpa_Tag_Entity {
    private short tagId;
    private String tagName;
    private Collection<Jpa_TagLink_Entity> tagLinksByTagId;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "tag_id", nullable = false)
    public short getTagId() {
        return tagId;
    }

    public void setTagId(short tagId) {
        this.tagId = tagId;
    }

    @Basic
    @Column(name = "tag_name", nullable = false, length = 128)
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Jpa_Tag_Entity that = (Jpa_Tag_Entity) o;

        if (tagId != that.tagId) return false;
        if (tagName != null ? !tagName.equals(that.tagName) : that.tagName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) tagId;
        result = 31 * result + (tagName != null ? tagName.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "tagByTagId")
    public Collection<Jpa_TagLink_Entity> getTagLinksByTagId() {
        return tagLinksByTagId;
    }

    public void setTagLinksByTagId(Collection<Jpa_TagLink_Entity> tagLinksByTagId) {
        this.tagLinksByTagId = tagLinksByTagId;
    }
}
