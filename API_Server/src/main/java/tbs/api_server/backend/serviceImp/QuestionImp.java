package tbs.api_server.backend.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import tbs.api_server.backend.filters.QuestionFilter;
import tbs.api_server.backend.mappers.AnswerMapper;
import tbs.api_server.backend.mappers.QuestionMapper;
import tbs.api_server.backend.mappers.TagMapper;
import tbs.api_server.backend.mappers.UserMapper;
import tbs.api_server.config.constant.const_Question;
import tbs.api_server.config.constant.const_User;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.Question;
import tbs.api_server.objects.simple.UserDetailInfo;
import tbs.api_server.objects.simple.UserSecurityInfo;
import tbs.api_server.services.QuestionService;
import tbs.api_server.utility.Error;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static tbs.api_server.utility.Error.*;

@Service
@Scope("prototype")
public class QuestionImp implements QuestionService {

    @Autowired
    TagMapper tagMapper;
    @Autowired
    private QuestionMapper mp;
    @Autowired
    private UserMapper userMapper;

    private boolean ownsQuestion(int queid, int userid) {
        UserDetailInfo ud = userMapper.getUserDetailInfoByID(userid);
        if (ud.getLevel() == const_User.LEVEL_EXAM_STAFF)
            return true;
        Question question = mp.OwnQuestion(queid, userid);
        if (question == null) {
            return false;
        } else
            return true;
    }

    Integer getID() {
        Integer uuid = UUID.randomUUID().toString().replaceAll("-", "").hashCode();
        uuid = uuid < 0 ? -uuid : uuid;//String.hashCode() 值会为空
        return uuid;
    }

    @Resource
    AnswerMapper answerMapper;

    @Override
    public ServiceResult uploadQuestion(int que_type, String title, int creator_id, byte[] que_file, Integer isopen, String ans) throws BackendError {
        int id = getID();
        int c = mp.insertQuestion(id, que_type, creator_id, que_file, title, isopen);
        c+= answerMapper.insertAnswer(id,ans,null);
        if (c > 1) {
            return ServiceResult.makeResult(SUCCESS, id);
        } else
            throw _ERROR.throwError(EC_DB_INSERT_FAIL, "上传问题失败");
    }

    @Override
    public ServiceResult deleteQuestion(int quesid, int userid) throws Error.BackendError {
        if (ownsQuestion(quesid, userid)) {
            return ServiceResult.makeResult(SUCCESS, mp.deleteQuestion(quesid));
        }
        throw _ERROR.throwError(EC_LOW_PERMISSIONS, "用户权限不足，无法删除此问题");
    }

    @Override
    public ServiceResult findQuestionsByTags(int[] tags, int from, int num) throws BackendError {
        HashSet<Question> res = new HashSet<>();
        for (int tg : tags) {
            for (int qs : tagMapper.listQuestionIdByTagId(tg)) {
                res.add(mp.getQuestionByID(qs));
            }

        }
        List<Question> result = new ArrayList<>(res);
        result = result.stream().filter(QuestionFilter.VisibleFilter).collect(Collectors.toList());
        for(Question q:result)
        {
            q.setTags(tagMapper.findTagsByQuestion(q.getQue_id()));
        }
        if (result.size() > 0)
            return ServiceResult.makeResult(SUCCESS, result);
        else
            throw _ERROR.throwError(EC_DB_SELECT_NOTHING, "不存在此标签的问题");


    }

    @Override
    public ServiceResult findQuestionsByType(int[] types, int from, int num) throws BackendError {
        HashSet<Question> res = new HashSet<>();
        for (int tg : types) {
            List<Question> questions = mp.getQuestionsByType(tg, from, num);
            res.addAll(questions);
        }
        List<Question> result = new ArrayList<>(res);
        result = result.stream().filter(QuestionFilter.VisibleFilter).collect(Collectors.toList());
        for(Question q:result)
        {
            q.setTags(tagMapper.findTagsByQuestion(q.getQue_id()));
        }
        if (result.size() > 0)
            return ServiceResult.makeResult(SUCCESS, result);
        else
            throw _ERROR.throwError(EC_DB_SELECT_NOTHING, "不存在此类型的问题");

    }

    @Override
    public ServiceResult findQuestionsByTitle(String title, int from, int num) throws BackendError {
        List<Question> ls = mp.findQuestionByTitle(title, from, num);
        ls = ls.stream().filter(QuestionFilter.VisibleFilter).collect(Collectors.toList());
        for(Question q:ls)
        {
            q.setTags(tagMapper.findTagsByQuestion(q.getQue_id()));
        }
        if (ls.size() > 0)
            return ServiceResult.makeResult(SUCCESS, ls);
        else
            throw _ERROR.throwError(EC_DB_SELECT_NOTHING, "不存在相关标题的问题");

    }

    @Override
    public ServiceResult findQuestionsByID(int id) throws BackendError {
        Question q = mp.getQuestionByID(id);
        if (q == null)
            throw _ERROR.throwError(EC_DB_SELECT_NOTHING, "不存在指定ID的题目");
        List<Question> ls = new ArrayList<>();
        ls.add(q);
        ls = ls.stream().filter(QuestionFilter.VisibleFilter).collect(Collectors.toList());
        for(Question qx:ls)
        {
            qx.setTags(tagMapper.findTagsByQuestion(qx.getQue_id()));
        }
        return ServiceResult.makeResult(SUCCESS, ls);
    }

    @Override
    public ServiceResult findQuestionsByTag(String tagname) throws BackendError {
        return ServiceResult.makeResult(SUCCESS, mp.getQuestionByTag(tagname));
    }

    @Override
    public ServiceResult listQuestions(int from, int num) throws BackendError {

        List<Question> obj = mp.getQuestions(from, num);
        obj = obj.stream().filter(QuestionFilter.VisibleFilter).collect(Collectors.toList());
        for(Question q:obj)
        {
            q.setTags(tagMapper.findTagsByQuestion(q.getQue_id()));
        }
        if (obj.size() > 0)
            return ServiceResult.makeResult(SUCCESS, obj);
        else
            throw _ERROR.throwError(EC_DB_SELECT_NOTHING, "问题列表为空");

    }

    @Override
    public ServiceResult updateQuestionValue(UserSecurityInfo user,int ques_id, String field, Object value) throws Error.BackendError {

        if(!ownsQuestion(ques_id,user.getId()))
            throw _ERROR.throwError(EC_LOW_PERMISSIONS,"未拥有此题，无法修改");
        String[] banded = {const_Question.col_id, const_Question.col_altertime};
        for (String i : banded) {
            if (field.equals(i))
                throw _ERROR.throwError(Error.EC_BAND_COL, String.format("%s 无法手动修改", i));
        }
        int m = mp.updateQuestionValue(ques_id, field, value);
        if (m > 0)
            return ServiceResult.makeResult(SUCCESS, m);
        else
            throw _ERROR.throwError(EC_DB_UPDATE_FAIL, "更新问题失败");

    }


    @Override
    public ServiceResult questionsLength(UserSecurityInfo u) throws BackendError {
        return ServiceResult.makeResult(SUCCESS, mp.countQuestions(u.getId(),u.getLevel()));
    }

    @Override
    public ServiceResult updateTags(int[] tags, int ques) {
        tagMapper.unLinkTagByQuestion(ques);
        for(int tg:tags)
        {
            tagMapper.linkTag(ques,tg);
        }
        return ServiceResult.makeResult(SUCCESS);
    }
}
