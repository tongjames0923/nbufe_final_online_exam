package tbs.api_server.objects.simple;

public class ExamQuestionLink {
    private final static long serialVersionUID = 1L;    private String insertor,examname;
    private int id, score,questionid;

    public int getQuestionid() {
        return questionid;
    }


    @Override
    public String toString() {
        return "ExamQuestionLink{" +
                "insertor='" + insertor + '\'' +
                ", examname='" + examname + '\'' +
                ", id=" + id +
                ", socre=" + score +
                ", questionid=" + questionid +
                '}';
    }

    public void setQuestionid(int questionid) {
        this.questionid = questionid;
    }

    public String getInsertor() {
        return insertor;
    }

    public void setInsertor(String insertor) {
        this.insertor = insertor;
    }

    public String getExamname() {
        return examname;
    }

    public void setExamname(String examname) {
        this.examname = examname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
