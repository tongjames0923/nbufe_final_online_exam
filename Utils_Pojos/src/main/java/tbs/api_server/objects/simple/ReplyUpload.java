package tbs.api_server.objects.simple;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public class ReplyUpload {
    int examid;
    String number; String personid; String personname;
    List<CheckData> datas;

    public int getExamid() {
        return examid;
    }

    public void setExamid(int examid) {
        this.examid = examid;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPersonid() {
        return personid;
    }

    public void setPersonid(String personid) {
        this.personid = personid;
    }

    public String getPersonname() {
        return personname;
    }

    public void setPersonname(String personname) {
        this.personname = personname;
    }

    public List<CheckData> getDatas() {
        return datas;
    }

    public void setDatas(List<CheckData> datas) {
        this.datas = datas;
    }
}
