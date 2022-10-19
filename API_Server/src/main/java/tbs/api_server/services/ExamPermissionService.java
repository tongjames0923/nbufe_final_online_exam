package tbs.api_server.services;

import tbs.api_server.objects.ServiceResult;

public interface ExamPermissionService
{
    ServiceResult getPermission(int exam_id, int userid);

    ServiceResult getCheckerList(int examid,int from,int num);

    ServiceResult setPermission(int examid, int userid,Boolean read,Boolean write,Boolean check);
    ServiceResult getExamsFORREAD(int userid,int from,int num);
    ServiceResult getExamsFORWRITE(int userid,int from,int num);
    ServiceResult getExamsFORCHECK(int user,int from,int num);


}
