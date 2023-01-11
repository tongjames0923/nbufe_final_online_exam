package tbs.api_server.objects.compound.exam;

import tbs.api_server.utility.XML.StrParser.Default.DoubleParser;
import tbs.api_server.utility.XML.StrParser.Default.IntParser;
import tbs.api_server.utility.XML.xml_getter;
import tbs.api_server.utility.XML.xml_setter;

public class ExamQuestion {
    double score;
    int ques_id;


    @xml_getter(name = "score")
    public double getScore() {
        return score;
    }

    @xml_setter(name = "score",parser = DoubleParser.class)
    public void setScore(double score) {
        this.score = score;
    }
    @xml_getter(name = "ques_id")
    public int getQues_id() {
        return ques_id;
    }

    @xml_setter(name = "ques_id",parser = IntParser.class)
    public void setQues_id(int ques_id) {
        this.ques_id = ques_id;
    }
}
