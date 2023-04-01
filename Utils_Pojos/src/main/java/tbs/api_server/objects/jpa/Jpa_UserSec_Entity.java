package tbs.api_server.objects.jpa;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "user_sec", schema = "exam_db", catalog = "")
public class Jpa_UserSec_Entity {
    private int id;
    private String name;
    private String password;
    private String secQues;
    private String secAns;
    private Jpa_ExamLink_Entity examLinkByName;
    private Collection<Jpa_PerExam_Entity> perExamsById;
    private Collection<Jpa_Question_Entity> questionsById;
    private Jpa_UserInfo_Entity userInfoById;

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
    @Column(name = "name", nullable = false, length = 32)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "password", nullable = false, length = 32)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "sec_ques", nullable = true, length = 64)
    public String getSecQues() {
        return secQues;
    }

    public void setSecQues(String secQues) {
        this.secQues = secQues;
    }

    @Basic
    @Column(name = "sec_ans", nullable = true, length = 64)
    public String getSecAns() {
        return secAns;
    }

    public void setSecAns(String secAns) {
        this.secAns = secAns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Jpa_UserSec_Entity that = (Jpa_UserSec_Entity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (secQues != null ? !secQues.equals(that.secQues) : that.secQues != null) return false;
        if (secAns != null ? !secAns.equals(that.secAns) : that.secAns != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (secQues != null ? secQues.hashCode() : 0);
        result = 31 * result + (secAns != null ? secAns.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "name", referencedColumnName = "insertor", nullable = false,insertable = false,updatable = false)
    public Jpa_ExamLink_Entity getExamLinkByName() {
        return examLinkByName;
    }

    public void setExamLinkByName(Jpa_ExamLink_Entity examLinkByName) {
        this.examLinkByName = examLinkByName;
    }

    @OneToMany(mappedBy = "userSecByUser")
    public Collection<Jpa_PerExam_Entity> getPerExamsById() {
        return perExamsById;
    }

    public void setPerExamsById(Collection<Jpa_PerExam_Entity> perExamsById) {
        this.perExamsById = perExamsById;
    }

    @OneToMany(mappedBy = "userSecByQueCreator")
    public Collection<Jpa_Question_Entity> getQuestionsById() {
        return questionsById;
    }

    public void setQuestionsById(Collection<Jpa_Question_Entity> questionsById) {
        this.questionsById = questionsById;
    }

    @OneToOne(mappedBy = "userSecById")
    public Jpa_UserInfo_Entity getUserInfoById() {
        return userInfoById;
    }

    public void setUserInfoById(Jpa_UserInfo_Entity userInfoById) {
        this.userInfoById = userInfoById;
    }
}
