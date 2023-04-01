package tbs.api_server.objects.jpa;

import javax.persistence.*;

@Entity
@Table(name = "per_exam", schema = "exam_db", catalog = "")
public class Jpa_PerExam_Entity {
    private int id;
    private byte readable;
    private long examId;
    private byte checkable;
    private byte writeable;
    private int user;
    private Jpa_Exam_Entity examByExamId;
    private Jpa_UserSec_Entity userSecByUser;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "readable", nullable = false)
    public byte getReadable() {
        return readable;
    }

    public void setReadable(byte readable) {
        this.readable = readable;
    }

    @Basic
    @Column(name = "exam_id", nullable = false)
    public long getExamId() {
        return examId;
    }

    public void setExamId(long examId) {
        this.examId = examId;
    }

    @Basic
    @Column(name = "checkable", nullable = false)
    public byte getCheckable() {
        return checkable;
    }

    public void setCheckable(byte checkable) {
        this.checkable = checkable;
    }

    @Basic
    @Column(name = "writeable", nullable = false)
    public byte getWriteable() {
        return writeable;
    }

    public void setWriteable(byte writeable) {
        this.writeable = writeable;
    }

    @Basic
    @Column(name = "user", nullable = false)
    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Jpa_PerExam_Entity that = (Jpa_PerExam_Entity) o;

        if (readable != that.readable) return false;
        if (examId != that.examId) return false;
        if (checkable != that.checkable) return false;
        if (writeable != that.writeable) return false;
        if (user != that.user) return false;
        if(id!= that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result =id;
        result = 31 * result + (int) readable;
        result = 31 * result + (int) (examId ^ (examId >>> 32));
        result = 31 * result + (int) checkable;
        result = 31 * result + (int) writeable;
        result = 31 * result + user;
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "exam_id", referencedColumnName = "exam_id", nullable = false,insertable = false,updatable = false)
    public Jpa_Exam_Entity getExamByExamId() {
        return examByExamId;
    }

    public void setExamByExamId(Jpa_Exam_Entity examByExamId) {
        this.examByExamId = examByExamId;
    }

    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public Jpa_UserSec_Entity getUserSecByUser() {
        return userSecByUser;
    }

    public void setUserSecByUser(Jpa_UserSec_Entity userSecByUser) {
        this.userSecByUser = userSecByUser;
    }
}
