package tbs.api_server.services;

import tbs.api_server.objects.ServiceResult;

public interface QuestionService
{
    ServiceResult uploadQuestion(int que_type,int creator_id, byte[] que_file, Integer isopen, Integer tagid);

    ServiceResult deleteQuestion(int quesid,int userid);

    ServiceResult findQuestionsByTags(int[] tags,int from,int num);
    ServiceResult findQuestionsByType(int[] types,int from,int num);

    ServiceResult listQuestions(int from,int num);

    ServiceResult updateQuestionValue(int ques_id,String field,Object value);


    ServiceResult deleteQuestion(int quesid);

//    ServiceResult linkResource(int quesid,int resource);
//    ServiceResult unlinkResource(int quesid,int resource);
//    ServiceResult getResourcesForQuestion(int quesid);


    ServiceResult questionsLength();



}
