package tbs.api_server.objects.jpa;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Table(name = "exam", schema = "exam_db", catalog = "")
public class Jpa_Exam_Entity {
    private long examId;
    private String examName;
    private Timestamp examBegin;
    private int examLen;
    private String examFile;
    private String examNote;
    private byte examStatus;
    private Collection<Jpa_ExamReply_Entity> examRepliesByExamId;
    private Jpa_ExamLink_Entity examLinkByExamName;
    private Collection<Jpa_PerExam_Entity> perExamsByExamId;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "exam_id", nullable = false)
    public long getExamId() {
        return examId;
    }

    public void setExamId(long examId) {
        this.examId = examId;
    }

    @Basic
    @Column(name = "exam_name", nullable = false, length = 64)
    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    @Basic
    @Column(name = "exam_begin", nullable = false)
    public Timestamp getExamBegin() {
        return examBegin;
    }

    public void setExamBegin(Timestamp examBegin) {
        this.examBegin = examBegin;
    }

    @Basic
    @Column(name = "exam_len", nullable = false)
    public int getExamLen() {
        return examLen;
    }

    public void setExamLen(int examLen) {
        this.examLen = examLen;
    }

    @Basic
    @Column(name = "exam_file", nullable = true, length = -1)
    public String getExamFile() {
        return examFile;
    }

    public void setExamFile(String examFile) {
        this.examFile = examFile;
    }

    @Basic
    @Column(name = "exam_note", nullable = true, length = 512)
    public String getExamNote() {
        return examNote;
    }

    public void setExamNote(String examNote) {
        this.examNote = examNote;
    }

    @Basic
    @Column(name = "exam_status", nullable = false)
    public byte getExamStatus() {
        return examStatus;
    }

    public void setExamStatus(byte examStatus) {
        this.examStatus = examStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Jpa_Exam_Entity that = (Jpa_Exam_Entity) o;

        if (examId != that.examId) return false;
        if (examLen != that.examLen) return false;
        if (examStatus != that.examStatus) return false;
        if (examName != null ? !examName.equals(that.examName) : that.examName != null) return false;
        if (examBegin != null ? !examBegin.equals(that.examBegin) : that.examBegin != null) return false;
        if (examFile != null ? !examFile.equals(that.examFile) : that.examFile != null) return false;
        if (examNote != null ? !examNote.equals(that.examNote) : that.examNote != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (examId ^ (examId >>> 32));
        result = 31 * result + (examName != null ? examName.hashCode() : 0);
        result = 31 * result + (examBegin != null ? examBegin.hashCode() : 0);
        result = 31 * result + examLen;
        result = 31 * result + (examFile != null ? examFile.hashCode() : 0);
        result = 31 * result + (examNote != null ? examNote.hashCode() : 0);
        result = 31 * result + (int) examStatus;
        return result;
    }

    @OneToMany(mappedBy = "examByExamId")
    public Collection<Jpa_ExamReply_Entity> getExamRepliesByExamId() {
        return examRepliesByExamId;
    }

    public void setExamRepliesByExamId(Collection<Jpa_ExamReply_Entity> examRepliesByExamId) {
        this.examRepliesByExamId = examRepliesByExamId;
    }

    @ManyToOne
    @JoinColumn(name = "exam_name", referencedColumnName = "examname", nullable = false,insertable = false,updatable = false)
    public Jpa_ExamLink_Entity getExamLinkByExamName() {
        return examLinkByExamName;
    }

    public void setExamLinkByExamName(Jpa_ExamLink_Entity examLinkByExamName) {
        this.examLinkByExamName = examLinkByExamName;
    }

    @OneToMany(mappedBy = "examByExamId")
    public Collection<Jpa_PerExam_Entity> getPerExamsByExamId() {
        return perExamsByExamId;
    }

    public void setPerExamsByExamId(Collection<Jpa_PerExam_Entity> perExamsByExamId) {
        this.perExamsByExamId = perExamsByExamId;
    }
}
