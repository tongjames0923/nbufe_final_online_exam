package tbs.api_server.objects.simple;

public class ExamPermission {
    private int id;
    private int readable;
    private int exam_id;
    private int checkable;
    private int writealbe;
    private int user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReadable() {
        return readable;
    }

    public void setReadable(int readable) {
        this.readable = readable;
    }

    public int getExam_id() {
        return exam_id;
    }

    public void setExam_id(int exam_id) {
        this.exam_id = exam_id;
    }

    public int getCheckable() {
        return checkable;
    }

    public void setCheckable(int checkable) {
        this.checkable = checkable;
    }

    public int getWritealbe() {
        return writealbe;
    }

    public void setWritealbe(int writealbe) {
        this.writealbe = writealbe;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }
}
