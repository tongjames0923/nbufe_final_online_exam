package tbs.api_server.objects.jpa;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Table(name = "question", schema = "exam_db", catalog = "")
public class Jpa_Question_Entity {
    private int queId;
    private short queType;
    private int queCreator;
    private Timestamp queAlterTime;
    private String queFile;
    private int publicable;
    private int answerd;
    private double answerdRight;
    private String title;
    private Collection<Jpa_Answer_Entity> answersByQueId;
    private Collection<Jpa_ExamReply_Entity> examRepliesByQueId;
    private Collection<Jpa_ExamLink_Entity> examLinksByQueId;
    private Jpa_UserSec_Entity userSecByQueCreator;
    private Collection<Jpa_ResourceLink_Entity> resourceLinksByQueId;
    private Collection<Jpa_TagLink_Entity> tagLinksByQueId;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "que_id", nullable = false)
    public int getQueId() {
        return queId;
    }

    public void setQueId(int queId) {
        this.queId = queId;
    }

    @Basic
    @Column(name = "que_type", nullable = false)
    public short getQueType() {
        return queType;
    }

    public void setQueType(short queType) {
        this.queType = queType;
    }

    @Basic
    @Column(name = "que_creator", nullable = false)
    public int getQueCreator() {
        return queCreator;
    }

    public void setQueCreator(int queCreator) {
        this.queCreator = queCreator;
    }

    @Basic
    @Column(name = "que_alter_time", nullable = false)
    public Timestamp getQueAlterTime() {
        return queAlterTime;
    }

    public void setQueAlterTime(Timestamp queAlterTime) {
        this.queAlterTime = queAlterTime;
    }

    @Basic
    @Column(name = "que_file", nullable = false, length = -1)
    public String getQueFile() {
        return queFile;
    }

    public void setQueFile(String queFile) {
        this.queFile = queFile;
    }

    @Basic
    @Column(name = "publicable", nullable = false)
    public int getPublicable() {
        return publicable;
    }

    public void setPublicable(int publicable) {
        this.publicable = publicable;
    }

    @Basic
    @Column(name = "answerd", nullable = false)
    public int getAnswerd() {
        return answerd;
    }

    public void setAnswerd(int answerd) {
        this.answerd = answerd;
    }

    @Basic
    @Column(name = "answerd_right", nullable = false, precision = 0)
    public double getAnswerdRight() {
        return answerdRight;
    }

    public void setAnswerdRight(double answerdRight) {
        this.answerdRight = answerdRight;
    }

    @Basic
    @Column(name = "title", nullable = false, length = 32)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Jpa_Question_Entity that = (Jpa_Question_Entity) o;

        if (queId != that.queId) return false;
        if (queType != that.queType) return false;
        if (queCreator != that.queCreator) return false;
        if (answerd != that.answerd) return false;
        if (Double.compare(that.answerdRight, answerdRight) != 0) return false;
        if (queAlterTime != null ? !queAlterTime.equals(that.queAlterTime) : that.queAlterTime != null) return false;
        if (queFile != null ? !queFile.equals(that.queFile) : that.queFile != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = queId;
        result = 31 * result + (int) queType;
        result = 31 * result + queCreator;
        result = 31 * result + (queAlterTime != null ? queAlterTime.hashCode() : 0);
        result = 31 * result + (queFile != null ? queFile.hashCode() : 0);
        result = 31 * result + answerd;
        temp = Double.doubleToLongBits(answerdRight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "questionByQuesId")
    public Collection<Jpa_Answer_Entity> getAnswersByQueId() {
        return answersByQueId;
    }

    public void setAnswersByQueId(Collection<Jpa_Answer_Entity> answersByQueId) {
        this.answersByQueId = answersByQueId;
    }

    @OneToMany(mappedBy = "questionByQuesId")
    public Collection<Jpa_ExamReply_Entity> getExamRepliesByQueId() {
        return examRepliesByQueId;
    }

    public void setExamRepliesByQueId(Collection<Jpa_ExamReply_Entity> examRepliesByQueId) {
        this.examRepliesByQueId = examRepliesByQueId;
    }

    @OneToMany(mappedBy = "questionByQuestionid")
    public Collection<Jpa_ExamLink_Entity> getExamLinksByQueId() {
        return examLinksByQueId;
    }

    public void setExamLinksByQueId(Collection<Jpa_ExamLink_Entity> examLinksByQueId) {
        this.examLinksByQueId = examLinksByQueId;
    }

    @ManyToOne
    @JoinColumn(name = "que_creator", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public Jpa_UserSec_Entity getUserSecByQueCreator() {
        return userSecByQueCreator;
    }

    public void setUserSecByQueCreator(Jpa_UserSec_Entity userSecByQueCreator) {
        this.userSecByQueCreator = userSecByQueCreator;
    }

    @OneToMany(mappedBy = "questionByQuesId")
    public Collection<Jpa_ResourceLink_Entity> getResourceLinksByQueId() {
        return resourceLinksByQueId;
    }

    public void setResourceLinksByQueId(Collection<Jpa_ResourceLink_Entity> resourceLinksByQueId) {
        this.resourceLinksByQueId = resourceLinksByQueId;
    }

    @OneToMany(mappedBy = "questionByQuesId")
    public Collection<Jpa_TagLink_Entity> getTagLinksByQueId() {
        return tagLinksByQueId;
    }

    public void setTagLinksByQueId(Collection<Jpa_TagLink_Entity> tagLinksByQueId) {
        this.tagLinksByQueId = tagLinksByQueId;
    }
}
