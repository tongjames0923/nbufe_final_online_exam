package tbs.api_server.services;

import org.springframework.web.multipart.MultipartFile;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.CheckData;
import tbs.api_server.objects.simple.UserSecurityInfo;
import tbs.api_server.utility.Error;

import java.util.List;

public interface ReplyService
{
    ServiceResult uploadReply(int eid,String uid,List<CheckData> rs) throws Error.BackendError;

    public ServiceResult list(int examid, UserSecurityInfo user) throws Error.BackendError;

    public ServiceResult updateScore(int er, double score,UserSecurityInfo u) throws Error.BackendError;

    public ServiceResult confirm(int eid,UserSecurityInfo u)throws Error.BackendError;

    public ServiceResult preCheck(UserSecurityInfo u,int e) throws Error.BackendError;
}
