package tbs.api_server.services;

import tbs.api_server.objects.ServiceResult;
import tbs.api_server.utility.Error;

public interface ResourceService
{
    ServiceResult getResourceById(int id) throws Error.BackendError;
    ServiceResult getResourceByNote(String note,int from,int num) throws Error.BackendError;
    ServiceResult getResourceByQuestion(int ques) throws Error.BackendError;
    ServiceResult getResourcesByType(int type,int from,int num) throws Error.BackendError;
    ServiceResult UploadResource(int type,String path,String note) throws Error.BackendError;
    ServiceResult DeleteResource(int id) throws Error.BackendError;
    ServiceResult linkResource(int res_id,int ques_id) throws Error.BackendError;
    ServiceResult unlinkResource(int res_id,int ques_id) throws Error.BackendError;
    ServiceResult updateResourceNote(int res_id,String note) throws Error.BackendError;
}
