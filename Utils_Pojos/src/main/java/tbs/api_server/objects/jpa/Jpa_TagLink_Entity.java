package tbs.api_server.objects.jpa;

import javax.persistence.*;

@Entity
@Table(name = "tag_link", schema = "exam_db", catalog = "")
public class Jpa_TagLink_Entity {
    private int id;
    private short tagId;
    private int quesId;
    private Jpa_Tag_Entity tagByTagId;
    private Jpa_Question_Entity questionByQuesId;

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
    @Column(name = "tag_id", nullable = false)
    public short getTagId() {
        return tagId;
    }

    public void setTagId(short tagId) {
        this.tagId = tagId;
    }

    @Basic
    @Column(name = "ques_id", nullable = false)
    public int getQuesId() {
        return quesId;
    }

    public void setQuesId(int quesId) {
        this.quesId = quesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Jpa_TagLink_Entity that = (Jpa_TagLink_Entity) o;

        if (tagId != that.tagId) return false;
        if (quesId != that.quesId) return false;
        if(id!=that.id)return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (int) tagId;
        result = 31 * result + quesId;
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "tag_id", referencedColumnName = "tag_id", nullable = false,insertable = false,updatable = false)
    public Jpa_Tag_Entity getTagByTagId() {
        return tagByTagId;
    }

    public void setTagByTagId(Jpa_Tag_Entity tagByTagId) {
        this.tagByTagId = tagByTagId;
    }

    @ManyToOne
    @JoinColumn(name = "ques_id", referencedColumnName = "que_id", nullable = false,insertable = false,updatable = false)
    public Jpa_Question_Entity getQuestionByQuesId() {
        return questionByQuesId;
    }

    public void setQuestionByQuesId(Jpa_Question_Entity questionByQuesId) {
        this.questionByQuesId = questionByQuesId;
    }
}
