package tbs.api_server.services;

import tbs.api_server.objects.ServiceResult;
import tbs.api_server.utility.Error;

public interface ExamPermissionService
{
    ServiceResult getPermission(int exam_id, int userid) throws Error.BackendError;

    ServiceResult getCheckerList(int examid,int from,int num) throws Error.BackendError;
    ServiceResult getReadList(int examid,int from,int num) throws Error.BackendError;
    ServiceResult getWriteList(int examid,int from,int num) throws Error.BackendError;


    ServiceResult setPermission(int examid, int userid,Boolean read,Boolean write,Boolean check);
    ServiceResult getExamsFORREAD(int userid,int from,int num) throws Error.BackendError;
    ServiceResult getExamsFORWRITE(int userid,int from,int num) throws Error.BackendError;
    ServiceResult getExamsFORCHECK(int user,int from,int num) throws Error.BackendError;




}
