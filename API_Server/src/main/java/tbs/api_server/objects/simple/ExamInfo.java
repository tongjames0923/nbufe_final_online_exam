package tbs.api_server.objects.simple;

import java.io.InputStream;
import java.util.Date;

public class ExamInfo {
    private int exam_id;
    private String exam_name;
    private Date exam_begin;
    private int exam_len;
    private String exam_file;
    private String exam_note;
    private int exam_status;

    public int getExam_id() {
        return exam_id;
    }

    public void setExam_id(int exam_id) {
        this.exam_id = exam_id;
    }

    public String getExam_name() {
        return exam_name;
    }

    public void setExam_name(String exam_name) {
        this.exam_name = exam_name;
    }

    public Date getExam_begin() {
        return exam_begin;
    }

    public void setExam_begin(Date exam_begin) {
        this.exam_begin = exam_begin;
    }

    public int getExam_len() {
        return exam_len;
    }

    public void setExam_len(int exam_len) {
        this.exam_len = exam_len;
    }

    public String getExam_file()
    {
        return exam_file;
    }

    public void setExam_file(String exam_file)
    {
        this.exam_file = exam_file;
    }

    public String getExam_note() {
        return exam_note;
    }

    public void setExam_note(String exam_note) {
        this.exam_note = exam_note;
    }

    public int getExam_status() {
        return exam_status;
    }

    public void setExam_status(int exam_status) {
        this.exam_status = exam_status;
    }
}
