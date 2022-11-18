package tbs.api_server.publicAPI;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tbs.api_server.objects.NetResult;
import tbs.api_server.services.ReplyService;
import tbs.api_server.utility.ApiMethod;
import tbs.api_server.utility.Error;

@RestController
@RequestMapping("reply")
@Scope("prototype")
public class ReplyController
{


    @Autowired
    ReplyService service;


    @RequestMapping(value = "reply",method = RequestMethod.POST)
    @Transactional
    public NetResult upload(int examid, String number, String person, MultipartFile file)
    {
        return ApiMethod.make(new ApiMethod.IAction()
        {
            @Override
            public NetResult action() throws Error.BackendError, Exception
            {
                return NetResult.makeResult(service.uploadReply(examid, number, person, file),null);
            }
        }).method();
    }

    @RequestMapping(value = "check",method = RequestMethod.POST)
    @Transactional
    public NetResult uploadCheck(String number,int status,int checker,String password,MultipartFile file)
    {
        return ApiMethod.make(new ApiMethod.IAction()
        {
            @Override
            public NetResult action() throws Error.BackendError, Exception
            {
               return NetResult.makeResult(service.uploadCheck(number,status,checker,password,file),null);
            }
        }).method();
    }

    @RequestMapping("list")
    @Transactional
    public NetResult list(int examid)
    {
        return ApiMethod.make(new ApiMethod.IAction()
        {
            @Override
            public NetResult action() throws Error.BackendError, Exception
            {
                return NetResult.makeResult(service.listall(examid),null);
            }
        }).method();
    }


}
