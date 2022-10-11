package tbs.api_server.objects.simple;

import java.io.InputStream;

public class StandardAnswer {
    private int id;
    private int ques_id;
    private byte[] answer_content;
    private byte[] answer_analysis;

    public int getId() {
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

    public byte[] getAnswer_content() {
        return answer_content;
    }

    public void setAnswer_content(byte[] answer_content) {
        this.answer_content = answer_content;
    }

    public byte[] getAnswer_analysis() {
        return answer_analysis;
    }

    public void setAnswer_analysis(byte[] answer_analysis) {
        this.answer_analysis = answer_analysis;
    }
}
