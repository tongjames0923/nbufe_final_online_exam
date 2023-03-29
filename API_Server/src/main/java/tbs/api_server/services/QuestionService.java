package tbs.api_server.services;

import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.UserSecurityInfo;
import tbs.api_server.utility.Error;

public interface QuestionService
{

    ServiceResult uploadQuestion(int que_type,String title, int creator_id, byte[] que_file, Integer isopen,String ans) throws Error.BackendError;


    ServiceResult deleteQuestion(int quesid,int userid) throws Error.BackendError;

    ServiceResult findQuestionsByTags(int[] tags,int from,int num) throws Error.BackendError;
    ServiceResult findQuestionsByType(int[] types,int from,int num) throws Error.BackendError;
    ServiceResult findQuestionsByTitle(String title,int from,int num) throws Error.BackendError;

    ServiceResult findQuestionsByID(int id) throws Error.BackendError;

    ServiceResult findQuestionsByTag(String tagname) throws  Error.BackendError;

    ServiceResult listQuestions(int from,int num) throws Error.BackendError;

    ServiceResult updateQuestionValue(int ques_id,String field,Object value) throws Error.BackendError;

    ServiceResult questionsLength(UserSecurityInfo user) throws Error.BackendError;

    ServiceResult updateTags(int[] tags,int ques);

}
