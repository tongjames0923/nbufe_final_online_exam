package tbs.api_server.objects.jpa;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Table(name = "ques_resource", schema = "exam_db", catalog = "")
public class Jpa_QuesResource_Entity {
    private int id;
    private String resource;
    private String note;
    private Timestamp altertime;
    private short resourceType;
    private Collection<Jpa_ResourceLink_Entity> resourceLinksById;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "resource", nullable = false, length = 96)
    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    @Basic
    @Column(name = "note", nullable = true, length = -1)
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Basic
    @Column(name = "altertime", nullable = false)
    public Timestamp getAltertime() {
        return altertime;
    }

    public void setAltertime(Timestamp altertime) {
        this.altertime = altertime;
    }

    @Basic
    @Column(name = "resource_type", nullable = false)
    public short getResourceType() {
        return resourceType;
    }

    public void setResourceType(short resourceType) {
        this.resourceType = resourceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Jpa_QuesResource_Entity that = (Jpa_QuesResource_Entity) o;

        if (id != that.id) return false;
        if (resourceType != that.resourceType) return false;
        if (resource != null ? !resource.equals(that.resource) : that.resource != null) return false;
        if (note != null ? !note.equals(that.note) : that.note != null) return false;
        if (altertime != null ? !altertime.equals(that.altertime) : that.altertime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (resource != null ? resource.hashCode() : 0);
        result = 31 * result + (note != null ? note.hashCode() : 0);
        result = 31 * result + (altertime != null ? altertime.hashCode() : 0);
        result = 31 * result + (int) resourceType;
        return result;
    }

    @OneToMany(mappedBy = "quesResourceByResourceId")
    public Collection<Jpa_ResourceLink_Entity> getResourceLinksById() {
        return resourceLinksById;
    }

    public void setResourceLinksById(Collection<Jpa_ResourceLink_Entity> resourceLinksById) {
        this.resourceLinksById = resourceLinksById;
    }
}
