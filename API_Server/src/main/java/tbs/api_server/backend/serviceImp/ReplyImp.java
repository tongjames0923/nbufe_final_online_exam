package tbs.api_server.backend.serviceImp;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tbs.api_server.backend.mappers.*;
import tbs.api_server.backend.repos.ExamCheckMapper;
import tbs.api_server.backend.repos.ExamerMapper;
import tbs.api_server.config.ApplicationConfig;
import tbs.api_server.config.constant.const_Exam;
import tbs.api_server.config.constant.const_Exam_Reply;
import tbs.api_server.config.constant.const_Question;
import tbs.api_server.config.constant.const_User;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.compound.exam.CheckPojo;
import tbs.api_server.objects.jpa.ExamCheck_Entity;
import tbs.api_server.objects.jpa.ExamUser;
import tbs.api_server.objects.simple.*;
import tbs.api_server.services.ReplyService;
import tbs.api_server.utility.AsyncTaskCenter;
import tbs.api_server.utility.FileUtility;
import tbs.api_server.utils.BatchUtil;
import tbs.api_server.utils.MybatisBatchUtils;
import tbs.api_server.utils.SecurityTools;
import tbs.api_server.utility.UserUtility;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.*;
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


    ExamReplyInsertor insertor = new ExamReplyInsertor();

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
                ExamReply reply = new ExamReply(eid, c.getQueid(), i++, text, e.getUid());
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

//        if (reply == null)
//            throw _ERROR.throwError(EC_DB_SELECT_NOTHING, "不存在相关答卷");
//        if (reply.getStatus() == const_Exam_Reply.Comfirmed)
//            throw _ERROR.throwError(FC_UNAVALIABLE, "回答批阅已被确认，不可修改");
//        checkPermit(u, reply.getExam_id());
//        return ServiceResult.makeResult(SUCCESS, reply);
        return null;
    }

    @Resource
    ExamMapper examMapper;

    @Resource
    ExamCheckMapper checkMapper;

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
        replyMapper.updateStatus(eid, const_Exam_Reply.Comfirmed);
        return ServiceResult.makeResult(SUCCESS, null);
    }

    @Resource
    AsyncTaskCenter asyncTaskCenter;

    @Resource
    ExamLinkMapper linkMapper;

    static class AnswerCount {
        private int should = 0;
        private int all = 0;

        public void up() {
            should++;
        }

        public int getShould() {
            return should;
        }

        public void setShould(int should) {
            this.should = should;
        }

        public int getAll() {
            return all;
        }

        public void setAll(int all) {
            this.all = all;
        }
    }

    @Resource
    ExamLinkMapper examLinkMapper;

    @Override
    @Transactional
    public ServiceResult preCheck(UserSecurityInfo u, int eid) throws Exception {
        checkPermit(u, eid);
        ExamInfo examInfo = examMapper.getExamByid(eid);
        if (examInfo.getExam_status()!=const_Exam.EXAM_STATUS_CLOSED) {
            if(examInfo.getExam_status()>const_Exam.EXAM_STATUS_CLOSED)
            throw _ERROR.throwError(FC_UNAVALIABLE, "当前考试已预批阅");
            else
                throw _ERROR.throwError(FC_UNAVALIABLE, "当前考试状态尚未开始");

        }
        asyncTaskCenter.doWithAsync(new AsyncTaskCenter.AsyncToDo() {
            @Override
            public String key() {
                return String.format("examPrecheck-%d", eid);
            }

            @Override
            public void doSomeThing() {
                List<StandardAnswer> answers = answerMapper.listAnswerByQuestions(examInfo.getExam_name());
                for (StandardAnswer answer : answers) {

                    List<ExamReply> replies = replyMapper.findbyExamidAndQuestion(eid, answer.getQues_id());
                    HashMap<String, List<ExamReply>> persons = new HashMap<>();
                    for (ExamReply rep : replies) {
                        if (!persons.containsKey(rep.getExamer_uid())) {
                            persons.put(rep.getExamer_uid(),new ArrayList<>());
                        }
                        persons.get(rep.getExamer_uid()).add(rep);
                    }
                    HashMap<String, String> temp = new HashMap<>();
                    String str = null;
                    AnswerCount answerCount = new AnswerCount();
                    double b = linkMapper.score(examInfo.getExam_name(), answer.getQues_id());
                    try {
                        switch (answer.getType()) {
                            case const_Question.TYPE_Select:
                                List<StandardAnswer.Select> s = JSON.parseArray(answer.getAnswer_content(), StandardAnswer.Select.class);
                                answerCount.setAll(s.size());
                                for (StandardAnswer.Select s1 : s) {
                                    temp.put(s1.getText(), s1.getRight());
                                    if (s1.getRight().equals(StandardAnswer.Select.YES))
                                        answerCount.up();
                                }
                                break;
                            case const_Question.TYPE_FillBlank:
                                List<StandardAnswer.FillBlank> f = JSON.parseArray(answer.getAnswer_content(), StandardAnswer.FillBlank.class);
                                answerCount.setAll(f.size());
                                answerCount.setShould(f.size());
                                for (StandardAnswer.FillBlank f1 : f) {
                                    temp.put(f1.getText(), f1.getEqual());
                                }
                                break;
                            case const_Question.TYPE_ShortAnswer:
                                List<String> strs = JSON.parseArray(answer.getAnswer_content(), String.class);
                                answerCount.setAll(1);
                                answerCount.setShould(1);
                                if (!CollectionUtil.isEmpty(strs))
                                    str = strs.get(0);
                                break;
                        }
                        for (List<ExamReply> reps : persons.values()) {
                            int cnt = 0;
                            for (ExamReply reply : reps) {
                                if (answer.getType() == const_Question.TYPE_Select)
                                    cnt += dealSelect(reply, temp);
                                else if (answer.getType() == const_Question.TYPE_FillBlank)
                                    cnt += dealFillBlank(reply, temp);
                                else if (answer.getType() == const_Question.TYPE_ShortAnswer) {
                                    cnt += dealShortAnswer(reply.getContent(), str);
                                }
                            }
                            ExamCheck_Entity examCheck = new ExamCheck_Entity();
                            examCheck.setExamer(reps.get(0).getExamer_uid());
                            examCheck.setQuesId(answer.getQues_id());
                            examCheck.setExamId(examInfo.getExam_id());
                            if (cnt >= answerCount.getShould()) {
                                examCheck.setScore((int) b);
                            } else
                                examCheck.setScore(0);
                            checkMapper.save(examCheck);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        examMapper.updateExam(eid,const_Exam.col_status,const_Exam.EXAM_STATUS_PRECHECK);
        return ServiceResult.makeResult(SUCCESS);
    }

    int dealSelect(ExamReply reply, HashMap<String, String> maps) {
        if (maps.containsKey(reply.getContent()) && maps.get(reply.getContent()).equals(StandardAnswer.Select.YES)) {
            return 1;
        }
        return 0;
    }

    int dealFillBlank(ExamReply reply, HashMap<String, String> maps) {
        for (Map.Entry<String, String> mp : maps.entrySet()) {
            if (mp.getValue().equals(StandardAnswer.FillBlank.FULLEQUAL)) {
                if (mp.getKey().equals(reply.getContent()))
                    return 1;
            } else {
                if (mp.getKey().contains(reply.getContent()))
                    return 1;
            }
        }
        return 0;
    }

    int dealShortAnswer(String rep, String ans) {
        return rep.equals(ans) ? 1 : 0;
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
