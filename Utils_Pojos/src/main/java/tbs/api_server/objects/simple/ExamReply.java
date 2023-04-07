package tbs.api_server.objects.simple;

public class ExamReply {
    private final static long serialVersionUID = 1L;
    private int id,exam_id,ques_id,status, sortcode;
    private  double score;
    private String content,examer_uid;

    public int getSortcode() {
        return sortcode;
    }

    public void setSortcode(int sortcode) {
        this.sortcode = sortcode;
    }

    public String getExamer_uid() {
        return examer_uid;
    }

    public void setExamer_uid(String examer_uid) {
        this.examer_uid = examer_uid;
    }

    public ExamReply(int exam_id, int ques_id, int status, int sortcode, double score, String content, String examer_uid) {
        this.exam_id = exam_id;
        this.ques_id = ques_id;
        this.status = status;
        this.sortcode = sortcode;
        this.score = score;
        this.content = content;
        this.examer_uid = examer_uid;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExamReply)) return false;

        ExamReply reply = (ExamReply) o;

        if (exam_id != reply.exam_id) return false;
        if (ques_id != reply.ques_id) return false;
        if (!examer_uid.equals(reply.examer_uid)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = exam_id;
        result = 31 * result + ques_id;
        result = 31 * result + examer_uid.hashCode();
        return result;
    }
}
