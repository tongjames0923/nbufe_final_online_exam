package tbs.api_server.objects.compound.exam;

public class AnswerVO {
    String answerWord;
    String replyText="";
    String sign="";

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAnswerWord() {
        return answerWord;
    }

    public void setAnswerWord(String answerWord) {
        this.answerWord = answerWord;
    }

    public String getReplyText() {
        return replyText;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }
}
