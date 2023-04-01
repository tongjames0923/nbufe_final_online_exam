package tbs.api_server.objects.jpa;

import javax.persistence.*;

@Entity
@Table(name = "answer", schema = "exam_db", catalog = "")
public class Jpa_Answer_Entity {
    private int id;
    private int quesId;
    private String answerContent;
    private String answerAnalysis;
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
    @Column(name = "ques_id", nullable = false)
    public int getQuesId() {
        return quesId;
    }

    public void setQuesId(int quesId) {
        this.quesId = quesId;
    }

    @Basic
    @Column(name = "answer_content", nullable = false, length = -1)
    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    @Basic
    @Column(name = "answer_analysis", nullable = true, length = -1)
    public String getAnswerAnalysis() {
        return answerAnalysis;
    }

    public void setAnswerAnalysis(String answerAnalysis) {
        this.answerAnalysis = answerAnalysis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Jpa_Answer_Entity that = (Jpa_Answer_Entity) o;

        if (id != that.id) return false;
        if (quesId != that.quesId) return false;
        if (answerContent != null ? !answerContent.equals(that.answerContent) : that.answerContent != null)
            return false;
        if (answerAnalysis != null ? !answerAnalysis.equals(that.answerAnalysis) : that.answerAnalysis != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + quesId;
        result = 31 * result + (answerContent != null ? answerContent.hashCode() : 0);
        result = 31 * result + (answerAnalysis != null ? answerAnalysis.hashCode() : 0);
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
}
