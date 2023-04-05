package tbs.api_server.objects.simple;

import java.util.List;

public class CheckData {
    private int queid;
    private List<String> text;

    @Override
    public String toString() {
        return "CheckData{" +
                "queid=" + queid +
                ", text=" + text +
                '}';
    }

    public int getQueid() {
        return queid;
    }

    public void setQueid(int queid) {
        this.queid = queid;
    }

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }
}
