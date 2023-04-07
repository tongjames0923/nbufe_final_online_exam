package tbs.api_server.objects.compound.exam;

import tbs.api_server.objects.jpa.ExamUser;
import tbs.api_server.objects.simple.ExamReply;
import tbs.api_server.objects.simple.Question;
import tbs.api_server.objects.simple.StandardAnswer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CheckPojo {
    int examid;
    ExamUser examUser;
    public static class InnerReply
    {
        int question;
        StandardAnswer answer;

        public StandardAnswer getAnswer() {
            return answer;
        }

        public void setAnswer(StandardAnswer answer) {
            this.answer = answer;
        }

        List<ExamReply> replyList;

        public int getQuestion() {
            return question;
        }

        public void setQuestion(int question) {
            this.question = question;
        }

        public List<ExamReply> getReplyList() {
            return replyList;
        }

        public void setReplyList(List<ExamReply> replyList) {
            this.replyList = replyList;
        }
    }
    List<InnerReply> replyList;

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

    public List<InnerReply> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<InnerReply> replyList) {
        this.replyList = replyList;
    }
}
