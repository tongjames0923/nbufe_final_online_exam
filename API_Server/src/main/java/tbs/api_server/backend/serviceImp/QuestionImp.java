package tbs.api_server.backend.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import tbs.api_server.backend.mappers.QuestionMapper;
import tbs.api_server.backend.mappers.TagMapper;
import tbs.api_server.backend.mappers.UserMapper;
import tbs.api_server.config.constant.const_Question;
import tbs.api_server.config.constant.const_User;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.Question;
import tbs.api_server.objects.simple.UserDetailInfo;
import tbs.api_server.services.QuestionService;
import tbs.api_server.utility.Error;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static tbs.api_server.utility.Error.*;

@Service
@Scope("prototype")
public class QuestionImp implements QuestionService
{

    @Autowired
    private QuestionMapper mp;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    TagMapper tagMapper;

    private boolean ownsQuestion(int queid,int userid)
    {
        Question question= mp.OwnQuestion(queid,userid);
        if(question==null)
        {
            UserDetailInfo ud= userMapper.getUserDetailInfoByID(userid);
            return ud!=null&&ud.getLevel()== const_User.LEVEL_EXAM_STAFF;
        }
        else
            return true;
    }

    @Override
    public ServiceResult uploadQuestion(int que_type, String title, int creator_id, byte[] que_file, Integer isopen, String ans) throws BackendError {
        int c= mp.insertQuestion(que_type, creator_id, que_file,title, isopen,ans);
        if(c>0)
        {
            return ServiceResult.makeResult(SUCCESS);
        }
        else
          throw   _ERROR.throwError(EC_DB_INSERT_FAIL,"上传问题失败");
    }

    @Override
    public ServiceResult deleteQuestion(int quesid, int userid) throws Error.BackendError {
        if(ownsQuestion(quesid, userid))
        {
           return ServiceResult.makeResult(mp.deleteQuestion(quesid),null);
        }
        throw   _ERROR.throwError(EC_LOW_PERMISSIONS,"用户权限不足，无法删除此问题");
    }

    @Override
    public ServiceResult findQuestionsByTags(int[] tags, int from, int num) throws BackendError {
            HashSet<Question> res=new HashSet<>();
            for(int tg:tags)
            {
              for(int qs:tagMapper.listQuestionIdByTagId(tg))
              {
                 res.add(mp.getQuestionByID(qs));
              }

            }
            List<Question> result=new ArrayList<>(res);
            if(result.size()>0)
            return ServiceResult.makeResult(result.size(), result);
            else
                throw   _ERROR.throwError(EC_DB_SELECT_NOTHING,"不存在此标签的问题");


    }

    @Override
    public ServiceResult findQuestionsByType(int[] types, int from, int num) throws BackendError {
            HashSet<Question> res=new HashSet<>();
            for(int tg:types)
            {
                List<Question> questions = mp.getQuestionsByType(tg,from,num);
                res.addAll(questions);
            }
            List<Question> result=new ArrayList<>(res);
            if(result.size()>0)
                return ServiceResult.makeResult(result.size(), result);
            else
                throw   _ERROR.throwError(EC_DB_SELECT_NOTHING,"不存在此类型的问题");

    }

    @Override
    public ServiceResult findQuestionsByTitle(String title, int from, int num) throws BackendError {
            List<Question> ls=mp.findQuestionByTitle(title, from, num);
            if(ls.size()>0)
            return ServiceResult.makeResult(ls.size(),ls);
            else
                throw _ERROR.throwError(EC_DB_SELECT_NOTHING,"不存在相关标题的问题");

    }

    @Override
    public ServiceResult listQuestions(int from, int num) throws BackendError {

            List<Question> obj=mp.getQuestions(from, num);
            if(obj.size()>0)
            return ServiceResult.makeResult(obj.size(), obj);
            else
                throw  _ERROR.throwError(EC_DB_SELECT_NOTHING,"问题列表为空");

    }

    @Override
    public ServiceResult updateQuestionValue(int ques_id, String field, Object value) throws Error.BackendError {

            String[] banded={const_Question.col_id,const_Question.col_altertime};
            for(String i:banded)
            {
                if(field.equals(i))
                    throw  _ERROR.throwError(Error.EC_BAND_COL,String.format("%s 无法手动修改",i));
            }
            int m= mp.updateQuestionValue(ques_id,field,value);
            if(m>0)
            return ServiceResult.makeResult(m,null);
            else
                throw  _ERROR.throwError(EC_DB_UPDATE_FAIL,"更新问题失败");

    }


    @Override
    public ServiceResult questionsLength() throws BackendError {
            return ServiceResult.makeResult(mp.countQuestions(), null);
    }
}
