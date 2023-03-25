package tbs.api_server.backend.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tbs.api_server.config.AccessManager;
import tbs.api_server.config.constant.const_User;
import tbs.api_server.objects.simple.Question;
import tbs.api_server.objects.simple.UserSecurityInfo;

import javax.annotation.Resource;
import java.util.function.Predicate;

@Component
public class QuestionFilter implements Predicate<Question> {

    public static QuestionFilter VisibleFilter;

    @Autowired
    public QuestionFilter()
    {
        VisibleFilter=this;
    }



    @Override
    public boolean test(Question question) {
        UserSecurityInfo now= AccessManager.ACCESS_MANAGER.getLoginedFromHttp();
        if(now.getLevel()== const_User.LEVEL_EXAM_STAFF)
            return true;
        if(question.getPublicable()==1)
            return true;
        return now.getId()== question.getQue_creator();
    }
}
