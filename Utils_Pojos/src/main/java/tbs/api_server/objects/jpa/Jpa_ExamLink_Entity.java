package tbs.api_server.objects.jpa;

import javax.persistence.*;

@Entity
@Table(name = "exam_link", schema = "exam_db", catalog = "")
public class Jpa_ExamLink_Entity {
    private int id;
    private String examname;
    private Integer questionid;
    private String insertor;
    private int score;
    private Jpa_Question_Entity questionByQuestionid;

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
    @Column(name = "examname", nullable = true, length = 64)
    public String getExamname() {
        return examname;
    }

    public void setExamname(String examname) {
        this.examname = examname;
    }

    @Basic
    @Column(name = "questionid", nullable = true)
    public Integer getQuestionid() {
        return questionid;
    }

    public void setQuestionid(Integer questionid) {
        this.questionid = questionid;
    }

    @Basic
    @Column(name = "insertor", nullable = true, length = 32)
    public String getInsertor() {
        return insertor;
    }

    public void setInsertor(String insertor) {
        this.insertor = insertor;
    }

    @Basic
    @Column(name = "score", nullable = false)
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Jpa_ExamLink_Entity that = (Jpa_ExamLink_Entity) o;

        if (id != that.id) return false;
        if (score != that.score) return false;
        if (examname != null ? !examname.equals(that.examname) : that.examname != null) return false;
        if (questionid != null ? !questionid.equals(that.questionid) : that.questionid != null) return false;
        if (insertor != null ? !insertor.equals(that.insertor) : that.insertor != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (examname != null ? examname.hashCode() : 0);
        result = 31 * result + (questionid != null ? questionid.hashCode() : 0);
        result = 31 * result + (insertor != null ? insertor.hashCode() : 0);
        result = 31 * result + score;
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "questionid", referencedColumnName = "que_id",insertable = false,updatable = false)
    public Jpa_Question_Entity getQuestionByQuestionid() {
        return questionByQuestionid;
    }

    public void setQuestionByQuestionid(Jpa_Question_Entity questionByQuestionid) {
        this.questionByQuestionid = questionByQuestionid;
    }
}
