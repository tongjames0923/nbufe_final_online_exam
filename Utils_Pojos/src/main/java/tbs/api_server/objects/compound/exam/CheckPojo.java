package tbs.api_server.objects.compound.exam;

import tbs.api_server.objects.jpa.ExamUser;
import tbs.api_server.objects.simple.ExamReply;
import tbs.api_server.objects.simple.Question;

import java.util.List;
import java.util.Map;

public class CheckPojo {
    int examid;
    ExamUser examUser;
    Map<Question,List<ExamReply>> replyList;

    public int getExamid() {
        return examid;
    }

    public void setExamid(int examid) {
        this.examid = examid;
    }

    public ExamUser getExamUser() {
        return examUser;
    }

    public void setExamUser(ExamUser examUser) {
        this.examUser = examUser;
    }

    public Map<Question, List<ExamReply>> getReplyList() {
        return replyList;
    }

    public void setReplyList(Map<Question, List<ExamReply>> replyList) {
        this.replyList = replyList;
    }
}
