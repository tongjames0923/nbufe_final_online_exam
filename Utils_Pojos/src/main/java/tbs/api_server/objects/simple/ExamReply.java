package tbs.api_server.objects.simple;

import java.util.Arrays;

public class ExamReply {
    private final static long serialVersionUID = 1L;
    private int id,exam_id,ques_id,status;
    private  double score;
    private String content,exam_number,person_id,person_name;

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public ExamReply(int exam_id, int ques_id, String content, String exam_number, String person_id, String person_name) {
        this.exam_id = exam_id;
        this.ques_id = ques_id;
        this.content = content;
        this.exam_number = exam_number;
        this.person_id = person_id;
        this.person_name = person_name;
    }

    public ExamReply() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExam_id() {
        return exam_id;
    }

    public void setExam_id(int exam_id) {
        this.exam_id = exam_id;
    }

    public int getQues_id() {
        return ques_id;
    }

    public void setQues_id(int ques_id) {
        this.ques_id = ques_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExam_number() {
        return exam_number;
    }

    public void setExam_number(String exam_number) {
        this.exam_number = exam_number;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }
}
