package tbs.api_server.ai.model;

import java.util.ArrayList;
import java.util.List;

public class Ai_RequestModel {
    public static class Message
    {
        private String role="user";
        private String content;

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public Message(String content) {
            this.content = content;
        }

        public Message()
        {

        }
        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
    private String model="gpt-3.5-turbo";
    private List<Message> messages=new ArrayList<>();

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public static Ai_RequestModel request(String msg)
    {
        Ai_RequestModel model1=new Ai_RequestModel();
        model1.messages.add(new Message(msg));
        return model1;
    }
}
