package tbs.api_server.objects.compound.exam;

import tbs.api_server.objects.simple.Question;

public class ExamQuestion {
    double score;
    int ques_id;
    Question detail;

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
    public int getQues_id() {
        return ques_id;
    }

    public void setQues_id(int ques_id) {
        this.ques_id = ques_id;
    }
    public Question getDetail() {
        return detail;
    }

    public void setDetail(Question detail) {
        this.detail = detail;
    }
}
