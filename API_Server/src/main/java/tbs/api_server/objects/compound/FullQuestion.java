package tbs.api_server.objects.compound;

import tbs.api_server.objects.simple.Question;
import tbs.api_server.objects.simple.QuestionResource;

import java.util.ArrayList;
import java.util.List;

public class FullQuestion {
    Question question;
    List<QuestionResource> resources;

    public Question getQuestion() {
        return question;
    }
    public FullQuestion()
    {

    }
    public FullQuestion(Question question, List<QuestionResource> resources) {
        this.question = question;
        this.resources = resources;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<QuestionResource> getResources() {
        return resources;
    }

    public void setResources(List<QuestionResource> resources) {
        this.resources = resources;
    }
}
