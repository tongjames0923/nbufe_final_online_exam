package tbs.api_server.ai.model;

import java.util.List;

public class Select_AI_Model {
    private String text;
    public static class Ai_Option
    {
        private String answer;
        private boolean right;

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public boolean isRight() {
            return right;
        }

        public void setRight(boolean right) {
            this.right = right;
        }
    }
    private List<Ai_Option> options;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Ai_Option> getOptions() {
        return options;
    }

    public void setOptions(List<Ai_Option> options) {
        this.options = options;
    }
}
