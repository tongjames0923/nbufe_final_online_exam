package tbs.api_server.backend.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tbs.api_server.backend.mappers.QuestionMapper;
import tbs.api_server.config.constant.const_Question;
import tbs.api_server.config.constant.const_Text;
import tbs.api_server.config.constant.const_User;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.Question;
import tbs.api_server.objects.simple.UserDetailInfo;
import tbs.api_server.services.QuestionService;
import tbs.api_server.utility.Error;
import tbs.api_server.utility.MapperStore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static tbs.api_server.utility.Error.EC_LOW_PERMISSIONS;
import static tbs.api_server.utility.Error._ERROR;

@Service
public class QuestionImp implements QuestionService
{

    @Autowired
    private QuestionMapper mp;


    private boolean ownsQuestion(int queid,int userid)
    {
        Question question= mp.OwnQuestion(queid,userid);
        if(question==null)
        {
            UserDetailInfo ud= MapperStore.userMapper.getUserDetailInfoByID(userid);
            return ud!=null&&ud.getLevel()== const_User.LEVEL_EXAM_STAFF;
        }
        else
            return true;
    }

    @Override
    public ServiceResult uploadQuestion(int que_type, int creator_id, byte[] que_file, Integer isopen, Integer tagid)
    {
        return ServiceResult.makeResult(mp.insertQuestion(que_type, creator_id, que_file, isopen, tagid));
    }

    @Override
    public ServiceResult deleteQuestion(int quesid, int userid)
    {
        if(ownsQuestion(quesid, userid))
        {
           return ServiceResult.makeResult(mp.deleteQuestion(quesid),null);
        }
        _ERROR.throwError(EC_LOW_PERMISSIONS,null);
        return null;
    }

    @Override
    public ServiceResult findQuestionsByTags(int[] tags, int from, int num)
    {

        HashSet<Question> res=new HashSet<>();
        for(int tg:tags)
        {
            List<Question> questions = mp.getQuestionsByTag(tg,from,num);
            res.addAll(questions);
        }
        List<Question> result=new ArrayList<>(res);
        return ServiceResult.makeResult(result.size(), result);
    }

    @Override
    public ServiceResult findQuestionsByType(int[] types, int from, int num)
    {
        HashSet<Question> res=new HashSet<>();
        for(int tg:types)
        {
            List<Question> questions = mp.getQuestionsByType(tg,from,num);
            res.addAll(questions);
        }
        List<Question> result=new ArrayList<>(res);
        return ServiceResult.makeResult(result.size(), result);
    }

    @Override
    public ServiceResult listQuestions(int from, int num)
    {
        List<Question> obj=mp.getQuestions(from, num);
        return ServiceResult.makeResult(obj.size(), obj);
    }

    @Override
    public ServiceResult updateQuestionValue(int ques_id, String field, Object value)
    {
        String[] banded={const_Question.col_id,const_Question.col_altertime};
        for(String i:banded)
        {
            if(field.equals(i))
                _ERROR.throwError(Error.EC_BAND_COL, const_Text.ERROR_BAND_COLUMN_NAME(i));
        }
        int m= mp.updateQuestionValue(ques_id,field,value);
        return ServiceResult.makeResult(m,null);
    }

    @Override
    public ServiceResult deleteQuestion(int quesid)
    {
        int m=mp.deleteQuestion(quesid);

        return ServiceResult.makeResult(m,null);
    }

    @Override
    public ServiceResult questionsLength()
    {
        return ServiceResult.makeResult(mp.countQuestions(), null);
    }
}
