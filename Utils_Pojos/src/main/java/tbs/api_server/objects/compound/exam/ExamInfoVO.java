package tbs.api_server.objects.compound.exam;

import tbs.api_server.objects.jpa.ExamUser;
import tbs.api_server.objects.simple.ExamInfo;

public class ExamInfoVO extends ExamInfo {

    String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
