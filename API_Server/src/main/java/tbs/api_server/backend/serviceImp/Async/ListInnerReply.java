package tbs.api_server.backend.serviceImp.Async;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;
import tbs.api_server.backend.mappers.AnswerMapper;
import tbs.api_server.backend.mappers.ExamReplyMapper;
import tbs.api_server.backend.repos.ExamCheckMapper;
import tbs.api_server.config.constant.const_Question;
import tbs.api_server.objects.compound.exam.AnswerVO;
import tbs.api_server.objects.compound.exam.CheckPojo;
import tbs.api_server.objects.jpa.ExamCheck_Entity;
import tbs.api_server.objects.jpa.ExamUser;
import tbs.api_server.objects.simple.ExamReply;
import tbs.api_server.objects.simple.StandardAnswer;
import tbs.api_server.utility.AsyncTaskCenter;
import tbs.api_server.utility.ListAsyncHelper;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class ListInnerReply {
    @Resource
    ListAsyncHelper listAsyncHelper;
    @Resource
    ExamReplyMapper replyMapper;
    @Resource
    AnswerMapper answerMapper;

    @Resource
    ExamCheckMapper examCheckMapper;
    public List<CheckPojo.InnerReply> listInnerReply(List<Integer> questionList,int examid,ExamUser eu)
    {
        List<AsyncTaskCenter.AsyncToGet<CheckPojo.InnerReply>> tasks=new ArrayList<>(questionList.size());
        for(Integer qid:questionList)
        {
            tasks.add(new AsyncTaskCenter.AsyncToGet<CheckPojo.InnerReply>() {
                @Override
                public CheckPojo.InnerReply doSomeThing() {
                    CheckPojo.InnerReply r = new CheckPojo.InnerReply();
                    r.setQuestion(qid);
                    StandardAnswer ans= answerMapper.getAnswerForQuestion(qid);
                    r.setType(ans.getType());
                    ExamCheck_Entity examCheck_entity= examCheckMapper.findFirstByQuesIdAndExamerAndExamId(qid,eu.getUid(),examid);
                    if(examCheck_entity!=null)
                        r.setScore(examCheck_entity.getScore());
                    r.setReplyList(makeAnswerVo(examid,r,eu,ans));
                    return r;
                }
            });
        }
        return listAsyncHelper.waitForAll(tasks);
    }
    private List<AnswerVO> makeAnswerVo(int eid, CheckPojo.InnerReply rep, ExamUser eu, StandardAnswer answer)
    {
        List<AnswerVO> result=new ArrayList<>();
        if(answer==null)
            return result;
        if(answer.getType()== const_Question.TYPE_Select)
        {

            List<StandardAnswer.Select> s = JSON.parseArray(answer.getAnswer_content(), StandardAnswer.Select.class);
            int index=0;
            for(StandardAnswer.Select i:s)
            {
                AnswerVO vo=new AnswerVO();
                ExamReply reply= replyMapper.findByContentForSelect(eid,answer.getQues_id(),eu.getUid(),i.getText());
                if(reply!=null)
                {
                    rep.setReplyId(reply.getId());
                    vo.setReplyText(reply.getContent());
                }

                vo.setAnswerWord(i.getText());
                vo.setSign(i.getRight());
                result.add(vo);
            }
        }
        else if(answer.getType()==const_Question.TYPE_FillBlank)
        {
            List<StandardAnswer.FillBlank> f = JSON.parseArray(answer.getAnswer_content(), StandardAnswer.FillBlank.class);
            int index=0;
            for(StandardAnswer.FillBlank i:f)
            {
                AnswerVO vo=new AnswerVO();
                ExamReply reply= replyMapper.findByContent(eid,answer.getQues_id(),index++, eu.getUid(),i.getText());
                if(reply!=null)
                {
                    rep.setReplyId(reply.getId());
                    vo.setReplyText(reply.getContent());
                }

                vo.setAnswerWord(i.getText());
                vo.setSign(i.getEqual());
                result.add(vo);
            }
        }
        else if(answer.getType()==const_Question.TYPE_ShortAnswer)
        {
            String str="";
            List<String> strs = JSON.parseArray(answer.getAnswer_content(), String.class);
            if (!CollectionUtil.isEmpty(strs))
                str = strs.get(0);
            AnswerVO vo=new AnswerVO();
            ExamReply reply= replyMapper.findByContentForShort(eid,answer.getQues_id(),eu.getUid());
            if(reply!=null)
            {                    rep.setReplyId(reply.getId());

                vo.setReplyText(reply.getContent());
            }
            vo.setAnswerWord(str);
            result.add(vo);
        }
        return result;
    }
}
