package tbs.api_server.services;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tbs.api_server.objects.ServiceResult;

public interface QuestionService
{
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    ServiceResult uploadQuestion(int que_type,String title, int creator_id, byte[] que_file, Integer isopen, Integer tagid);

    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    ServiceResult deleteQuestion(int quesid,int userid);

    ServiceResult findQuestionsByTags(int[] tags,int from,int num);
    ServiceResult findQuestionsByType(int[] types,int from,int num);
    ServiceResult findQuestionsByTitle(String title,int from,int num);

    ServiceResult listQuestions(int from,int num);

    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    ServiceResult updateQuestionValue(int ques_id,String field,Object value);

    ServiceResult questionsLength();



}
