package tbs.api_server.publicAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tbs.api_server.objects.NetResult;
import tbs.api_server.services.AnswerService;
import tbs.api_server.utility.ApiMethod;
import tbs.api_server.utility.Error;

@RestController
@RequestMapping("answer/*")
public class AnswerController
{
    @Autowired
    AnswerService service;

    public NetResult upload(int ques, int user, MultipartFile answer, MultipartFile ananalysis)
    {
        return ApiMethod.make(new ApiMethod.IAction()
        {
            @Override
            public NetResult action() throws Error.BackendError, Exception
            {
                Byte[] bt = null;
                if (ananalysis != null)
                {
                    byte[] bf = ananalysis.getBytes();
                    bt = new Byte[bf.length];
                    for (int i = 0; i < bf.length; i++)
                        bf[i] = bt[i];
                }
                return NetResult.makeResult(service.uploadAnswer(ques, user, answer.getBytes(), bt), null);
            }
        }).method();
    }
    public NetResult delete(int ques,int user)
    {
        return ApiMethod.make(new ApiMethod.IAction()
        {
            @Override
            public NetResult action() throws Error.BackendError, Exception
            {
                return NetResult.makeResult(service.deleteAnswer(ques,user),null);
            }
        }).method();
    }
    public NetResult get(int ques,int user)
    {
        return ApiMethod.make(new ApiMethod.IAction()
        {
            @Override
            public NetResult action() throws Error.BackendError, Exception
            {
                return NetResult.makeResult(service.getAnswer(ques,user),null);
            }
        }).method();
    }
}
