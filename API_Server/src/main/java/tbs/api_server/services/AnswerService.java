package tbs.api_server.services;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tbs.api_server.objects.ServiceResult;

public interface AnswerService
{
    ServiceResult getAnswer(int ques_id);
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    ServiceResult uploadAnswer(int ques_id,int user,byte[] answer,Byte[] analysis);
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    ServiceResult deleteAnswer(int ques_id,int user);
}
