package tbs.api_server.backend.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tbs.api_server.backend.mappers.ExamPermissionMapper;
import tbs.api_server.backend.mappers.ExamReplyMapper;
import tbs.api_server.backend.mappers.UserMapper;
import tbs.api_server.config.ApplicationConfig;
import tbs.api_server.config.constant.const_Exam_Reply;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.CheckData;
import tbs.api_server.objects.simple.ExamPermission;
import tbs.api_server.objects.simple.ExamReply;
import tbs.api_server.objects.simple.UserSecurityInfo;
import tbs.api_server.services.ReplyService;
import tbs.api_server.utility.FileUtility;
import tbs.api_server.utils.BatchUtil;
import tbs.api_server.utils.MybatisBatchUtils;
import tbs.api_server.utils.SecurityTools;
import tbs.api_server.utility.UserUtility;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static tbs.api_server.utility.Error.*;

@Service
@Scope("prototype")
public class ReplyImp implements ReplyService
{

    @Resource
    ExamReplyMapper replyMapper;

    @Resource
    BatchUtil<ExamReply> batchUtil;

    static class ExamReplyInsertor implements BatchUtil.Activitor<ExamReply>
    {

        @Override
        public void flush(MybatisBatchUtils obj, List<ExamReply> list) {
            obj.batchUpdateOrInsert(list, ExamReplyMapper.class, new BiFunction<ExamReply, ExamReplyMapper, Object>() {
                @Override
                public Object apply(ExamReply examReply, ExamReplyMapper examReplyMapper) {
                    return examReplyMapper.insertReply(examReply.getExam_id(), examReply.getExam_number(), examReply.getPerson_id(), examReply.getPerson_name(),examReply.getQues_id(), examReply.getContent(),examReply.getSortcode());
                }
            });
        }
    }


    @Override
    public ServiceResult uploadReply(String en, int eid, String pid, String pname,List<CheckData> rs) throws BackendError {
        if(replyMapper.canReply(eid, en, pid,pname)!=1)
        {
            throw _ERROR.throwError(FC_DUPLICATE,"请勿重复提交答案");
        }
        ExamReplyInsertor insertor=new ExamReplyInsertor();
        int i=0;
        for(CheckData c :rs)
        {

            for(String text:c.getText())
            {
                ExamReply reply=new ExamReply(eid,c.getQueid(),text,en,pid,pname,i++);
                batchUtil.write(reply,insertor,false);
            }
        }
        batchUtil.flush(insertor,true);
        return ServiceResult.makeResult(SUCCESS,null);
    }
//    public static class Help
//    {
//        public static String makeRepPath(String number, String person) throws Exception
//        {
//
//            String str = ApplicationConfig.Default_Text + number + person + ApplicationConfig.VERSION;
//
//            str = SecurityTools.Encrypt_str(str);
//            return ApplicationConfig.ReplyDir + "/" + str + ".rep";
//        }
//
//        public static String makeCheckPath(String number, String person) throws Exception
//        {
//            String str = ApplicationConfig.Default_Text + number + person + ApplicationConfig.VERSION;
//            str = SecurityTools.Encrypt_str(str);
//            return ApplicationConfig.CheckFileDir + "/" + str + ".chk";
//        }
//
//        public static int writeToFile(String path, InputStream file) throws Exception
//        {
//            FileUtility.BaseThen then = new FileUtility.FileWriteThen(file, true);
//            FileUtility.existFile(path, then);
//            return (int) then.result();
//        }
//    }
//
//
//    @Autowired
//    ExamReplyMapper mp;
//
//    @Override
//    public ServiceResult uploadReply(int examid, String number, String person, MultipartFile file)
//            throws BackendError, Exception
//    {
//        String reppath = Help.makeRepPath(number, person);
//        int total= Help.writeToFile(reppath,file.getInputStream());
//        if (total != file.getSize())
//            throw _ERROR.throwError(EC_FILESYSTEM_ERROR, "写入大小与文件大小不一致",total);
//        int c = mp.insertReply(examid, number, person, reppath);
//        if (c > 0)
//        {
//            return ServiceResult.makeResult(SUCCESS, total);
//        } else
//        {
//            throw _ERROR.throwError(EC_DB_INSERT_FAIL, "插入回答失败");
//        }
//    }
//
//    @Autowired
//    ExamPermissionMapper examPermissionMapper;
//
//    @Autowired
//    UserMapper ump;
//
//    @Override
//    public ServiceResult uploadCheck(String nubmer, int status, int checker, String password, MultipartFile file)
//            throws BackendError, Exception
//    {
//        ExamReply reply = mp.getExamReply(nubmer);
//        if (reply == null)
//            throw _ERROR.throwError(FC_UNAVALIABLE, "不存在答题卡，无法批阅");
//        if (reply.getStatus() == const_Exam_Reply.Comfirmed)
//            throw _ERROR.throwError(FC_DUPLICATE, "该试卷已被确认批阅");
//        UserSecurityInfo sc = ump.getUserSecurityInfo(checker);
//        if (sc == null)
//            throw _ERROR.throwError(FC_NOTFOUND, "不存在的批阅人");
//        if (!(UserUtility.passwordEncode(password).equals(sc.getPassword())))
//        {
//            throw _ERROR.throwError(FC_WRONG_PASSTEXT, "验证密码错误");
//        }
//        ExamPermission permissi = examPermissionMapper.getPermission(checker, reply.getExam_id());
//        if (permissi == null)
//            throw _ERROR.throwError(FC_NOTFOUND, "您对该考试不存在任何权限");
//        if (permissi.getCheckable() == 0 && permissi.getWriteable() == 0)
//            throw _ERROR.throwError(EC_LOW_PERMISSIONS, "您对该考试的权限不足，无法提交批阅");
//
//        String checkPath = Help.makeCheckPath(nubmer,reply.getPerson_id());
//        int total=Help.writeToFile(checkPath,file.getInputStream());
//        if(total!=file.getSize())
//            throw _ERROR.throwError(EC_FILESYSTEM_ERROR,"写入文件大小与接受大小不一致",total);
//        int c = mp.updateReplyCheck(nubmer, checkPath, status);
//        if (c > 0)
//            return ServiceResult.makeResult(SUCCESS);
//        throw _ERROR.throwError(EC_DB_UPDATE_FAIL, "上传批阅文件失败");
//    }
//
//    @Override
//    public ServiceResult listall(int examid) throws BackendError
//    {
//        List<ExamReply> ls = mp.getReplys(examid);
//        if (ls.size() > 0)
//            return ServiceResult.makeResult(SUCCESS, ls);
//        throw _ERROR.throwError(EC_DB_SELECT_NOTHING, "该考试尚无答题卡");
//    }

}
