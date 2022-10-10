package tbs.api_server.objects.simple;

import java.io.InputStream;

public class ExamReply {
    private int id;
    private int exam_id;
    private String exam_number;
    private String person_id;
    private InputStream reply_file;

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

    public InputStream getReply_file() {
        return reply_file;
    }

    public void setReply_file(InputStream reply_file) {
        this.reply_file = reply_file;
    }
}
