package tbs.api_server.publicAPI;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tbs.api_server.config.NoNeedAccess;
import tbs.api_server.objects.NetResult;
import tbs.api_server.objects.simple.CheckData;
import tbs.api_server.objects.simple.ReplyUpload;
import tbs.api_server.objects.simple.UserSecurityInfo;
import tbs.api_server.services.ReplyService;
import tbs.api_server.utility.ApiMethod;
import tbs.api_server.utility.Error;

import java.util.List;

@RestController
@RequestMapping("reply/*")
public class ReplyController {


    @Autowired
    ReplyService service;


    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @Transactional
    @NoNeedAccess
    public NetResult upload(@RequestBody ReplyUpload u) {
        return ApiMethod.makeResultNoLogin(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Error.BackendError, Exception {
                return NetResult.makeResult(service.uploadReply(u.getExamid(), u.getUid(), u.getDatas()), null);
            }
        });
    }

    @RequestMapping("list")
    public NetResult list(int examid) {
        return ApiMethod.makeResult(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Error.BackendError, Exception {
                return NetResult.makeResult(service.list(examid, applyUser), null);
            }
        });
    }


    @RequestMapping("updateScore")
    @Transactional
    public NetResult setScore(int rep_id, double score) {
        return ApiMethod.makeResult(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Error.BackendError, Exception {
                return NetResult.makeResult(service.updateScore(rep_id, score, applyUser), null);
            }
        });
    }

    @RequestMapping("confirm")
    @Transactional
    public NetResult comfirm(int examid) {
        return ApiMethod.makeResult(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Error.BackendError, Exception {
                return NetResult.makeResult(service.confirm(examid,applyUser),null);
            }
        });
    }
    @RequestMapping("precheck")
    public NetResult precheck(int examid)
    {
        return ApiMethod.makeResult(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Error.BackendError, Exception {
                return NetResult.makeResult(service.preCheck(applyUser,examid),null);
            }
        });
    }
//    @RequestMapping(value = "check",method = RequestMethod.POST)
//    @Transactional
//    public NetResult uploadCheck(String number,int status,int checker,String password,MultipartFile file)
//    {
//        return ApiMethod.make(new ApiMethod.IAction()
//        {
//            @Override
//            public NetResult action(UserSecurityInfo applyUser) throws Error.BackendError, Exception
//            {
//               return NetResult.makeResult(service.uploadCheck(number,status,checker,password,file),null);
//            }
//        }).methodWithLogined();
//    }
//
//    @RequestMapping("list")
//    @Transactional
//    public NetResult list(int examid)
//    {
//        return ApiMethod.make(new ApiMethod.IAction()
//        {
//            @Override
//            public NetResult action(UserSecurityInfo applyUser) throws Error.BackendError, Exception
//            {
//                return NetResult.makeResult(service.listall(examid),null);
//            }
//        }).methodWithLogined();
//    }


}
