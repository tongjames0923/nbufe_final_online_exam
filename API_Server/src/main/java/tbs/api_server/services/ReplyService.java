package tbs.api_server.services;

import org.springframework.web.multipart.MultipartFile;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.CheckData;
import tbs.api_server.utility.Error;

import java.util.List;

public interface ReplyService
{
    public ServiceResult uploadReply(String en, int eid, String pid, String pname,List<CheckData> rs) throws Error.BackendError;

}
