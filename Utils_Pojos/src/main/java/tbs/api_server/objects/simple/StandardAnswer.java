package tbs.api_server.objects.simple;

import java.util.Arrays;

public class StandardAnswer {
    private int id;
    private int ques_id;
    private String answer_content;

    private transient int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private String answer_analysis;
    private final static long serialVersionUID = 1L;    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQues_id() {
        return ques_id;
    }

    public void setQues_id(int ques_id) {
        this.ques_id = ques_id;
    }

    public String getAnswer_content() {
        return answer_content;
    }

    public void setAnswer_content(String answer_content) {
        this.answer_content = answer_content;
    }

    public String getAnswer_analysis() {
        return answer_analysis;
    }

    public void setAnswer_analysis(String answer_analysis) {
        this.answer_analysis = answer_analysis;
    }

    @Override
    public String toString() {
        return "StandardAnswer{" +
                "id=" + id +
                ", ques_id=" + ques_id +
                '}';
    }
}
