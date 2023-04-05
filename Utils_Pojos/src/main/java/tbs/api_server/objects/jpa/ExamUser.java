package tbs.api_server.objects.jpa;

import com.alibaba.fastjson.JSON;
import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "exam_user",schema = "exam_db")
public class ExamUser {
    @Column(name = "exam",nullable = false)
    int examid;
    @Column(name = "id",nullable = false,length = 64)
    String id;
    @Column(name = "number",nullable = false,length = 64)
    String number;

    @Column(name = "name",nullable = false,length = 64)
    String name;
    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 72)
    String uid;
    public int getExamid() {
        return examid;
    }

    public void setExamid(int examid) {
        this.examid = examid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getNumber() {
        return number;
    }


    public void setNumber(String number) {
        this.number = number;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
