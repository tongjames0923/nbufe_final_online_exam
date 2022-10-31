package tbs.api_server.services;

import org.springframework.web.multipart.MultipartFile;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.utility.Error;

public interface ReplyService
{
    ServiceResult uploadReply(int examid, String number, String person, MultipartFile file)
            throws Error.BackendError, Exception;
    ServiceResult uploadCheck(String nubmer,int status,int checker,String password,MultipartFile file) throws Error.BackendError, Exception;
    ServiceResult listall(int examid) throws Error.BackendError;

}
