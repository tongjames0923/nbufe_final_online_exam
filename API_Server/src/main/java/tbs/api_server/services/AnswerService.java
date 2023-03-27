package tbs.api_server.services;

import tbs.api_server.objects.ServiceResult;
import tbs.api_server.utility.Error;

public interface AnswerService
{
    ServiceResult getAnswer(int ques_id,int user) throws Error.BackendError;
    ServiceResult uploadAnswer(int ques_id,int user,byte[] answer,byte[] analysis) throws Error.BackendError;
    ServiceResult deleteAnswer(int ques_id,int user) throws Error.BackendError;
}
