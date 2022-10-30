package tbs.api_server.services;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.utility.Error;

public interface ResourceService
{
    ServiceResult getResourceById(int id) throws Error.BackendError;
    ServiceResult getResourceByNote(String note,int from,int num) throws Error.BackendError;
    ServiceResult getResourceByQuestion(int ques) throws Error.BackendError;
    ServiceResult getResourcesByType(int type,int from,int num) throws Error.BackendError;
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    ServiceResult UploadResource(int type,String path,String note) throws Error.BackendError;
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    ServiceResult DeleteResource(int id) throws Error.BackendError;
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    ServiceResult linkResource(int res_id,int ques_id) throws Error.BackendError;
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    ServiceResult unlinkResource(int res_id,int ques_id) throws Error.BackendError;
}
