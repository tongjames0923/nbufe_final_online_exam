package tbs.api_server.backend.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tbs.api_server.backend.mappers.*;
import tbs.api_server.backend.repos.ExamerMapper;
import tbs.api_server.config.ApplicationConfig;
import tbs.api_server.config.constant.const_Exam;
import tbs.api_server.config.constant.const_Exam_Reply;
import tbs.api_server.config.constant.const_User;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.compound.exam.CheckPojo;
import tbs.api_server.objects.jpa.ExamUser;
import tbs.api_server.objects.simple.*;
import tbs.api_server.services.ReplyService;
import tbs.api_server.utility.FileUtility;
import tbs.api_server.utils.BatchUtil;
import tbs.api_server.utils.MybatisBatchUtils;
import tbs.api_server.utils.SecurityTools;
import tbs.api_server.utility.UserUtility;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static tbs.api_server.utility.Error.*;

@Service
public class ReplyImp implements ReplyService {

    @Resource
    ExamReplyMapper replyMapper;

    @Resource
    BatchUtil<ExamReply> batchUtil;

    static class ExamReplyInsertor implements BatchUtil.Activitor<ExamReply> {

        @Override
        public void flush(MybatisBatchUtils obj, List<ExamReply> list) {
            obj.batchUpdateOrInsert(list, ExamReplyMapper.class, new BiFunction<ExamReply, ExamReplyMapper, Object>() {
                @Override
                public Object apply(ExamReply examReply, ExamReplyMapper examReplyMapper) {
                    return examReplyMapper.insertReply(examReply.getExam_id(), examReply.getQues_id(), examReply.getContent(), examReply.getSortcode(), examReply.getExamer_uid());
                }
            });
        }
    }

    static class ExamReplyUpdateScore implements BatchUtil.Activitor<ExamReply> {
        @Override
        public void flush(MybatisBatchUtils obj, List<ExamReply> list) {
            obj.batchUpdateOrInsert(list, ExamReplyMapper.class, new BiFunction<ExamReply, ExamReplyMapper, Object>() {
                @Override
                public Object apply(ExamReply examReply, ExamReplyMapper examReplyMapper) {
                    return examReplyMapper.updateScore(examReply.getId(), examReply.getScore());
                }
            });
        }
    }
    ExamReplyInsertor insertor = new ExamReplyInsertor();
    ExamReplyUpdateScore updateScore = new ExamReplyUpdateScore();

    @Override
    public ServiceResult uploadReply(int eid, String uid, List<CheckData> rs) throws BackendError {
        ExamUser e = examerMapper.findExamUserByExamidAndUid(eid, uid);
        if (e == null)
            throw _ERROR.throwError(EC_DB_SELECT_NOTHING, "本考试不存在本考生");
        if (replyMapper.canReply(eid, uid) != 1) {
            throw _ERROR.throwError(FC_DUPLICATE, "请勿重复提交答案");
        }

        for (CheckData c : rs) {
            int i = 0;
            for (String text : c.getText()) {
                ExamReply reply = new ExamReply(eid, c.getQueid(), const_Exam_Reply.UnCheck, i++,
                        0, text, e.getUid());
                batchUtil.write(reply, insertor, false);
            }
        }
        batchUtil.flush(insertor, true);
        return ServiceResult.makeResult(SUCCESS, null);
    }

    @Resource
    ExamPermissionMapper examPermissionMapper;
    @Resource
    ExamerMapper examerMapper;

    void checkPermit(UserSecurityInfo u, int examid) throws BackendError {
        boolean hasPer = false;
        if (u.getLevel() >= const_User.LEVEL_EXAM_STAFF)
            hasPer = true;
        else {
            ExamPermission permission = examPermissionMapper.getPermission(u.getId(), examid);
            hasPer = permission == null ? false : permission.getCheckable() == 1;
        }
        if (!hasPer)
            throw _ERROR.throwError(EC_LOW_PERMISSIONS, "不存在针对该考试的批阅权限");
    }


    @Resource
    QuestionMapper questionMapper;
    @Resource
    AnswerMapper answerMapper;

    @Override
    public ServiceResult list(int examid, UserSecurityInfo u) throws BackendError {
        checkPermit(u, examid);
        List<ExamUser> all = examerMapper.findAllByExamid(examid);
        List<CheckPojo> cps = new ArrayList<>();
        for (ExamUser eu : all) {
            CheckPojo cp = new CheckPojo();
            cp.setExamid(examid);
            cp.setExamUser(eu);
            List<ExamReply> els = replyMapper.listByExamUserIdAndExamId(examid, eu.getUid());
            HashMap<Integer, List<ExamReply>> replist = new HashMap<>();
            for (ExamReply reply : els) {
                int q = reply.getQues_id();
                if (replist.containsKey(q)) {
                    replist.get(q).add(reply);
                } else {
                    replist.put(q, new ArrayList<>(Arrays.asList(reply)));
                }
            }
            List<CheckPojo.InnerReply> replies = new ArrayList<>();
            replist.forEach(new BiConsumer<Integer, List<ExamReply>>() {
                @Override
                public void accept(Integer integer, List<ExamReply> examReplies) {
                    CheckPojo.InnerReply r = new CheckPojo.InnerReply();
                    r.setQuestion(integer);
                    r.setReplyList(examReplies);
                    r.setAnswer(answerMapper.getAnswerForQuestion(integer));
                    replies.add(r);
                }
            });
            cp.setReplyList(replies);
            cps.add(cp);
        }
        return ServiceResult.makeResult(SUCCESS, cps);
    }

    @Override
    public ServiceResult updateScore(int er, double score, UserSecurityInfo u) throws BackendError {
        ExamReply reply = replyMapper.findById(er);
        if (reply == null)
            throw _ERROR.throwError(EC_DB_SELECT_NOTHING, "不存在相关答卷");
        if (reply.getStatus() == const_Exam_Reply.Comfirmed)
            throw _ERROR.throwError(FC_UNAVALIABLE, "回答批阅已被确认，不可修改");
        checkPermit(u, reply.getExam_id());
        batchUtil.write(reply, updateScore, false);
        return ServiceResult.makeResult(SUCCESS, reply);
    }

    @Resource
    ExamMapper examMapper;

    @Override
    public ServiceResult confirm(int eid, UserSecurityInfo u) throws BackendError {
        checkPermit(u, eid);
        ExamInfo examInfo = examMapper.getExamByid(eid);
        if (examInfo.getExam_status() == const_Exam.EXAM_STATUS_CLOSED ||
                examInfo.getExam_status() == const_Exam.EXAM_STATUS_CHECKED &&
                        examInfo.getExam_status() == const_Exam.EXAM_STATUS_WAIT) {
            throw _ERROR.throwError(FC_UNAVALIABLE, "当前考试状态无法确认");
        }
        examMapper.updateExam(eid, const_Exam.col_status, const_Exam.EXAM_STATUS_CHECKED);
        replyMapper.updateStatus(eid,const_Exam_Reply.Comfirmed);
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
