package tbs.api_server.utility.XML;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import tbs.api_server.objects.compound.exam.ExamPost;
import tbs.api_server.objects.compound.exam.ExamQuestion;
import tbs.api_server.objects.compound.exam.ExamUser;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

public class XMLMaker {
    public static byte[] MakeXML(ExamPost post) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        Document document= DocumentHelper.createDocument();
        Element root= document.addElement("exam");
        XmlParser<ExamPost> dataParser=new XmlParser<>(ExamPost.class,"info");
        XmlParser<ExamUser> userXmlParser=new XmlParser<>(ExamUser.class,"user");
        XmlParser<ExamQuestion> questionXmlParser=new XmlParser<>(ExamQuestion.class,"question");
        dataParser.put(root,post);
        Element examers=root.addElement("examers");
        for(ExamUser user:post.getStudents())
        {
            userXmlParser.put(examers,user);
        }
        Element questions=root.addElement("questions");
        for(ExamQuestion q: post.getQuestions())
        {
            questionXmlParser.put(questions,q);
        }
        String doc=document.asXML();
        return doc.getBytes();
    }
}
