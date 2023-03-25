package tbs.api_server.objects.simple;

import java.util.Arrays;

public class ExamReply {
    private int id;
    private int exam_id,status;

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    private String exam_number,check_file;

    public String getCheck_file()
    {
        return check_file;
    }

    public void setCheck_file(String check_file)
    {
        this.check_file = check_file;
    }

    private String person_id;
    private byte[] reply_file;

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

    public byte[] getReply_file() {
        return reply_file;
    }

    public void setReply_file(byte[] reply_file) {
        this.reply_file = reply_file;
    }

    @Override
    public String toString() {
        return "ExamReply{" +
                "id=" + id +
                ", exam_id=" + exam_id +
                ", status=" + status +
                ", exam_number='" + exam_number + '\'' +
                ", check_file='" + check_file + '\'' +
                ", person_id='" + person_id + '\'' +
                '}';
    }
}
