package tbs.api_server.objects.simple;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public class ReplyUpload {
    int examid;
    String uid;
    List<CheckData> datas;

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

    public List<CheckData> getDatas() {
        return datas;
    }

    public void setDatas(List<CheckData> datas) {
        this.datas = datas;
    }
}
