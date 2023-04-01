package tbs.api_server.objects.jpa;

import javax.persistence.*;

@Entity
@Table(name = "resource_link", schema = "exam_db", catalog = "")
public class Jpa_ResourceLink_Entity {
    private int id;
    private int quesId;
    private int resourceId;
    private Jpa_Question_Entity questionByQuesId;
    private Jpa_QuesResource_Entity quesResourceByResourceId;

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
    @Column(name = "ques_id", nullable = false)
    public int getQuesId() {
        return quesId;
    }

    public void setQuesId(int quesId) {
        this.quesId = quesId;
    }

    @Basic
    @Column(name = "resource_id", nullable = false)
    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Jpa_ResourceLink_Entity that = (Jpa_ResourceLink_Entity) o;

        if (quesId != that.quesId) return false;
        if (resourceId != that.resourceId) return false;
        if(id!= that.id)return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + quesId;
        result = 31 * result + resourceId;
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "ques_id", referencedColumnName = "que_id", nullable = false,insertable = false,updatable = false)
    public Jpa_Question_Entity getQuestionByQuesId() {
        return questionByQuesId;
    }

    public void setQuestionByQuesId(Jpa_Question_Entity questionByQuesId) {
        this.questionByQuesId = questionByQuesId;
    }

    @ManyToOne
    @JoinColumn(name = "resource_id", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public Jpa_QuesResource_Entity getQuesResourceByResourceId() {
        return quesResourceByResourceId;
    }

    public void setQuesResourceByResourceId(Jpa_QuesResource_Entity quesResourceByResourceId) {
        this.quesResourceByResourceId = quesResourceByResourceId;
    }
}
