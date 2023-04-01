package tbs.api_server.objects.jpa;

import javax.persistence.*;

@Entity
@Table(name = "exam_ reply", schema = "exam_db", catalog = "")
public class Jpa_ExamReply_Entity {
    private int id;
    private long examId;
    private String examNumber;
    private String personId;
    private int quesId;
    private int status;
    private Double score;
    private Jpa_Exam_Entity examByExamId;
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
    @Column(name = "exam_id", nullable = false)
    public long getExamId() {
        return examId;
    }

    public void setExamId(long examId) {
        this.examId = examId;
    }

    @Basic
    @Column(name = "exam_number", nullable = false, length = 128)
    public String getExamNumber() {
        return examNumber;
    }

    public void setExamNumber(String examNumber) {
        this.examNumber = examNumber;
    }

    @Basic
    @Column(name = "person_id", nullable = false, length = 128)
    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
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
    @Column(name = "status", nullable = false)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Basic
    @Column(name = "score", nullable = true, precision = 0)
    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Jpa_ExamReply_Entity that = (Jpa_ExamReply_Entity) o;

        if (id != that.id) return false;
        if (examId != that.examId) return false;
        if (quesId != that.quesId) return false;
        if (status != that.status) return false;
        if (examNumber != null ? !examNumber.equals(that.examNumber) : that.examNumber != null) return false;
        if (personId != null ? !personId.equals(that.personId) : that.personId != null) return false;
        if (score != null ? !score.equals(that.score) : that.score != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (int) (examId ^ (examId >>> 32));
        result = 31 * result + (examNumber != null ? examNumber.hashCode() : 0);
        result = 31 * result + (personId != null ? personId.hashCode() : 0);
        result = 31 * result + quesId;
        result = 31 * result + status;
        result = 31 * result + (score != null ? score.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "exam_id", referencedColumnName = "exam_id", nullable = false,insertable = false,updatable = false)
    public Jpa_Exam_Entity getExamByExamId() {
        return examByExamId;
    }

    public void setExamByExamId(Jpa_Exam_Entity examByExamId) {
        this.examByExamId = examByExamId;
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
