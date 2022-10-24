package tbs.api_server.services;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tbs.api_server.objects.ServiceResult;

public interface ResourceService
{
    ServiceResult getResourceById(int id);
    ServiceResult getResourceByNote(String note,int from,int num);
    ServiceResult getResourceByQuestion(int ques);
    ServiceResult getResourcesByType(int type,int from,int num);
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    ServiceResult UploadResource(int type,String path,String note);
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    ServiceResult DeleteResource(int id);
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    ServiceResult linkResource(int res_id,int ques_id);
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    ServiceResult unlinkResource(int res_id,int ques_id);
}
