package tbs.api_server.objects.compound.exam;

import tbs.api_server.objects.simple.UserSecurityInfo;

public class UserVo {
    private int id;

    private String name;
    private String password;
    private String sec_ques;
    private String sec_ans;
    private String uid;

    public UserVo(String uid, UserSecurityInfo info) {
        this.uid = uid;
        setPassword(info.getPassword());
        setId(info.getId());
        setName(info.getName());
        setSec_ans(info.getSec_ans());
        setSec_ques(info.getSec_ques());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSec_ques() {
        return sec_ques;
    }

    public void setSec_ques(String sec_ques) {
        this.sec_ques = sec_ques;
    }

    public String getSec_ans() {
        return sec_ans;
    }

    public void setSec_ans(String sec_ans) {
        this.sec_ans = sec_ans;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
