package tbs.api_server.publicAPI;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tbs.api_server.backend.mappers.*;
import tbs.api_server.backend.serviceImp.ReplyImp;
import tbs.api_server.config.NoNeedAccess;
import tbs.api_server.config.constant.const_Resource_Type;
import tbs.api_server.objects.NetResult;
import tbs.api_server.objects.simple.ExamPermission;
import tbs.api_server.objects.simple.ExamReply;
import tbs.api_server.objects.simple.Question;
import tbs.api_server.objects.simple.UserSecurityInfo;
import tbs.api_server.services.ResourceService;
import tbs.api_server.utility.FileUtility;
import tbs.api_server.utility.UserUtility;

import static tbs.api_server.publicAPI.ResourceController.Help.getFile;
import static tbs.api_server.utility.Error.SUCCESS;

@RestController
@RequestMapping("/file/*")
public class FileController
{

    @Autowired
    ResourceService service;

    @NoNeedAccess
    @RequestMapping(value = "/res/image", produces = "image/*")
    public byte[] downloadImg(int id)
    {

        return getFile(service, id, const_Resource_Type.Image);
    }
    @NoNeedAccess
    @RequestMapping(value = "/res/text", produces = "text/*")
    public byte[] downloadText(int id)
    {
        return getFile(service, id, const_Resource_Type.Text);
    }
    @NoNeedAccess
    @RequestMapping(value = "/res/video", produces = "video/mp4")
    public byte[] downloadvideo(int id)
    {
        return getFile(service, id, const_Resource_Type.Video);
    }
    @NoNeedAccess
    @RequestMapping(value = "/res/audio", produces = "audio/*")
    public byte[] downloadAuio(int id)
    {
        return getFile(service, id, const_Resource_Type.Audio);
    }

    @Autowired
    ExamMapper examMapper;
    @NoNeedAccess
    @RequestMapping("exam")
    public String downloadExam(int id)
    {
        return examMapper.getExamFile(id);
    }

    @Autowired
    QuestionMapper qmp;
    @NoNeedAccess
    @RequestMapping(value = "question")
    public NetResult downloadQuestion(int id)
    {
        String s=qmp.getQuestionFile(id);
        return NetResult.makeResult(SUCCESS,null,s);
    }


    @Autowired
    UserMapper mp;
    @Autowired
    ExamReplyMapper examReplyMapper;

    @Autowired
    ExamPermissionMapper examPermissionMapper;
//    @NoNeedAccess
//    @RequestMapping(value = "reply")
//    public byte[] downloadRep(String number, int user, String password)
//    {
//        try
//        {
//            UserSecurityInfo sc = mp.getUserSecurityInfo(user);
//            if (sc == null)
//                return null;
//            if (UserUtility.passwordEncode(password).equals(sc.getPassword()))
//            {
//                ExamReply reply = examReplyMapper.getExamReply(number);
//                if (reply == null)
//                    return null;
//                ExamPermission permissi = examPermissionMapper.getPermission(user, reply.getExam_id());
//                if (permissi == null)
//                    return null;
//                if (permissi.getCheckable() == 0 && permissi.getWriteable() == 0)
//                    return null;
//                FileUtility.BaseThen then = new FileUtility.FileReadThen();
//                FileUtility.existFile(ReplyImp.Help.makeRepPath(reply.getExam_number(), reply.getPerson_id()), then);
//                return (byte[]) then.result();
//            }
//            return null;
//
//        } catch (Exception e)
//        {
//            return null;
//        }
//    }
//    @NoNeedAccess
//    @RequestMapping("check")
//    public byte[] downloadCheck(String number)
//    {
//        try
//        {
//            ExamReply reply = examReplyMapper.getExamReply(number);
//            if (reply == null)
//                return null;
//            if (reply.getCheck_file().length() < 3)
//                return null;
//            FileUtility.BaseThen then = new FileUtility.FileReadThen();
//            FileUtility.existFile(ReplyImp.Help.makeCheckPath(reply.getExam_number(), reply.getPerson_id()), then);
//            return (byte[]) then.result();
//        } catch (Exception e)
//        {
//            return null;
//        }
//    }


}
