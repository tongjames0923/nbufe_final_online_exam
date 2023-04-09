package tbs.api_server.objects.jpa;

import javax.persistence.*;

@Entity
@Table(name = "exam_check", schema = "exam_db", catalog = "")
public class ExamCheck_Entity {
    private Integer checkid;
    private Integer quesId;
    private Integer examId;
    private String examer;
    private Integer score;

    @Id
    @Column(name = "checkid")
    public Integer getCheckid() {
        return checkid;
    }

    public void setCheckid(Integer checkid) {
        this.checkid = checkid;
    }

    @Basic
    @Column(name = "ques_id")
    public Integer getQuesId() {
        return quesId;
    }

    public void setQuesId(Integer quesId) {
        this.quesId = quesId;
    }

    @Basic
    @Column(name = "exam_id")
    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    @Basic
    @Column(name = "examer")
    public String getExamer() {
        return examer;
    }

    public void setExamer(String examer) {
        this.examer = examer;
    }

    @Basic
    @Column(name = "score")
    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExamCheck_Entity that = (ExamCheck_Entity) o;

        if (checkid != null ? !checkid.equals(that.checkid) : that.checkid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = checkid != null ? checkid.hashCode() : 0;
        return result;
    }
}
