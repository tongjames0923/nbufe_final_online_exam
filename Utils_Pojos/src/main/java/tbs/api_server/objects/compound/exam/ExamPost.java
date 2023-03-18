package tbs.api_server.objects.compound.exam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExamPost {
    private String exam_name;
    private Date exam_begin;
    private int exam_len;
    private String exam_note;
    private List<ExamUser> students = new ArrayList<ExamUser>();
    private List<ExamQuestion> questions = new ArrayList<ExamQuestion>();

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


    public String getExam_note() {
        return exam_note;
    }

    public void setExam_note(String exam_note) {
        this.exam_note = exam_note;
    }

    public List<ExamUser> getStudents() {
        return students;
    }

    public void setStudents(List<ExamUser> students) {
        this.students = students;
    }

    public List<ExamQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<ExamQuestion> questions) {
        this.questions = questions;
    }
}
