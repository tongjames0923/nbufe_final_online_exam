package tbs.api_server.backend.serviceImp;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import tbs.api_server.backend.mappers.*;
import tbs.api_server.backend.repos.ExamerMapper;
import tbs.api_server.config.AccessManager;
import tbs.api_server.config.constant.const_Exam;
import tbs.api_server.config.constant.const_User;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.compound.exam.ExamInfoVO;
import tbs.api_server.objects.compound.exam.ExamPost;
import tbs.api_server.objects.compound.exam.ExamQuestion;
import tbs.api_server.objects.jpa.ExamUser;
import tbs.api_server.objects.simple.*;
import tbs.api_server.services.ExamService;
import tbs.api_server.utils.TimeUtil;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static tbs.api_server.utility.Error.*;

@Service
@Scope("prototype")
public class ExamImp implements ExamService {

    @Autowired
    ExamMapper mp;

    @Autowired
    ExamPermissionMapper permit;


    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionMapper questionMapper;

    @Override
    public ServiceResult countExams(UserSecurityInfo user) {
        if(user.getLevel()>=2)
        {
            return ServiceResult.makeResult(SUCCESS,mp.countStaff());
        }
        return ServiceResult.makeResult(SUCCESS, mp.countReadable(user.getId()));
    }

    @Override
    public ServiceResult listExamsForStudent(String number, String id, String name) {
        List<ExamInfoVO> ex = mp.listForStudent(id,name,number, 0, 10);
        return ServiceResult.makeResult(SUCCESS, ex);
    }

    @Override
    public ServiceResult getExamByStatus(int status, int from, int num) throws BackendError {
        List<ExamInfo> list = mp.getExamsByStatus(status, from, num);
        if (list != null && list.size() > 0) {
            return ServiceResult.makeResult(SUCCESS, list);
        }
        throw _ERROR.throwError(EC_DB_SELECT_NOTHING, "不存在此状态的考试", status);
    }

    @Override
    public ServiceResult getExamByName(String name) throws BackendError {
        ExamInfo examInfo = mp.getExamIDByExamName(name);
        if (examInfo != null) {
            return ServiceResult.makeResult(SUCCESS, examInfo);
        }
        throw _ERROR.throwError(EC_DB_SELECT_NOTHING, "不存在此名称的考试", name);
    }

    @Resource
    ExamLinkMapper examLinkMapper;

    @Resource
    ExamerMapper examerMapper;

    @Override
    public ServiceResult uploadExam(int user, ExamPost data)
            throws BackendError {
        String opertname = AccessManager.ACCESS_MANAGER.getLoginedFromHttp().getName();
        byte[] file = null;
        List<ExamQuestion> qs =data.getQuestions();

        int c = mp.uploadExam(data.getExam_name(), data.getExam_begin(), data.getExam_note(), data.getExam_len());
        if (c <= 0)
            throw _ERROR.throwError(EC_DB_INSERT_FAIL, "上传考试失败", null);
        ExamInfo info = mp.getExamIDByExamName(data.getExam_name());
        for(ExamUser u:data.getStudents())
        {
            u.setExamid(info.getExam_id());
        }
        examerMapper.saveAll(data.getStudents());
        c = permit.putPermission(user, info.getExam_id(), 1, 1, 1);
        if (c <= 0)
            throw _ERROR.throwError(EC_DB_INSERT_FAIL, "考试权限赋予失败");
        c = 0;
        for (ExamQuestion eq : qs) {
            c += examLinkMapper.insertQuestionLink(eq.getQues_id(), data.getExam_name(), (int) eq.getScore(), opertname);
        }
        if (c != qs.size())
            throw _ERROR.throwError(EC_DB_INSERT_FAIL, "插入考题数据不完整");

        return ServiceResult.makeResult(SUCCESS, null);
    }

    @Override
    public ServiceResult deleteExam(int id, UserSecurityInfo userid) throws BackendError {
        if (needWrite(userid, id)) {
            int c = mp.deleteExam(id);
            if (c > 0)
                return ServiceResult.makeResult(SUCCESS);
            throw _ERROR.throwError(EC_DB_DELETE_FAIL, "删除考试失败");
        } else {
            throw _ERROR.throwError(EC_LOW_PERMISSIONS, "权限不足，不可删除考试");
        }
    }

    @Override
    public ServiceResult list(UserSecurityInfo user, int from, int num) throws BackendError {
        List<ExamInfo> list=null;
        if(user.getLevel()>=2)
        {
            list= mp.list(from, num);
        }
        else
            list=mp.listWithPermit(user.getId(), from,num);
        if (list.size() > 0)
            return ServiceResult.makeResult(SUCCESS, list);
        throw _ERROR.throwError(EC_DB_SELECT_NOTHING, "不存在考试");
    }

    private boolean needCheck(int user, int examid) {
        ExamPermission permission = permit.getPermission(user, examid);
        if (permission == null)
            return false;
        return permission.getWriteable() != 0 || permission.getCheckable() != 0;
    }

    private boolean needWrite(UserSecurityInfo user, int examid) {

        if (user.getLevel() == const_User.LEVEL_EXAM_STAFF) {
            return true;
        }
        ExamPermission permission = permit.getPermission(user.getId(), examid);
        if (permission == null)
            return false;
        return permission.getWriteable() != 0;
    }


    @Override
    public ServiceResult updateStatus(int status, int examid) throws BackendError {
        int c = mp.updateExam(examid, const_Exam.col_status, status);
        if (c > 0)
            return ServiceResult.makeResult(SUCCESS);
        throw _ERROR.throwError(EC_DB_UPDATE_FAIL, "更新状态失败");
    }

    @Override
    public ServiceResult updateLen(int len, UserSecurityInfo user, int examid) throws BackendError {
        ExamInfo info = mp.getExamByid(examid);
        if (info == null)
            throw _ERROR.throwError(EC_DB_SELECT_NOTHING, "不存在的考试id");
        if (needWrite(user, examid)) {
            if (TimeUtil.isStart(info))
                throw _ERROR.throwError(FC_UNAVALIABLE, "考试开始后不支持更新考试时长");
            int c = mp.updateExam(examid, const_Exam.col_len, len);
            if (c > 0)
                return ServiceResult.makeResult(SUCCESS);
            throw _ERROR.throwError(EC_DB_UPDATE_FAIL, "更新考试长度失败");
        } else
            throw _ERROR.throwError(EC_LOW_PERMISSIONS, "更改时长必须拥有写入权限");


    }

    @Override
    public ServiceResult updateNote(String note, UserSecurityInfo user, int examid) throws BackendError {
        if (needWrite(user, examid)) {
            int c = mp.updateExam(examid, const_Exam.col_notes, note);
            if (c > 0)
                return ServiceResult.makeResult(SUCCESS);
            throw _ERROR.throwError(EC_DB_UPDATE_FAIL, "更新备注失败");
        } else
            throw _ERROR.throwError(EC_LOW_PERMISSIONS, "更改时长必须拥有写入权限");

    }

    @Override
    public ServiceResult updateBegin(Date date, UserSecurityInfo user, int examid) throws BackendError {
        if (date.before(new Date()))
            throw _ERROR.throwError(EC_InvalidParameter, "不支持修改到过去时间");
        ExamInfo info = mp.getExamByid(examid);
        if (info == null)
            throw _ERROR.throwError(EC_DB_SELECT_NOTHING, "不存在的考试id");
        if (needWrite(user, examid)) {
            if (TimeUtil.isStart(info))
                throw _ERROR.throwError(FC_UNAVALIABLE, "考试开始后不支持考试开始时间");
            int c = mp.updateExam(examid, const_Exam.col_begin, date);
            if (c > 0)
                return ServiceResult.makeResult(SUCCESS);
            throw _ERROR.throwError(EC_DB_UPDATE_FAIL, "更新考试开始时间失败");
        } else
            throw _ERROR.throwError(EC_LOW_PERMISSIONS, "更改考试开始时间必须拥有写入权限");


    }

    @Override
    public ServiceResult updateName(String name, UserSecurityInfo user, int examid) throws BackendError {
        try {
            if (needWrite(user, examid)) {
                int c = mp.updateExam(examid, const_Exam.col_name, name);
                if (c > 0)
                    return ServiceResult.makeResult(SUCCESS);
                throw _ERROR.throwError(EC_DB_UPDATE_FAIL, "更新考试名称失败");
            } else
                throw _ERROR.throwError(EC_LOW_PERMISSIONS, "更改考试名称必须拥有写入权限");
        } catch (DuplicateKeyException ec) {
            throw _ERROR.throwError(FC_DUPLICATE, "重复考试名称");
        } catch (Exception e) {
            throw _ERROR.throwError(EC_UNKNOWN, e.getMessage(), e);
        }

    }

    @Override
    public ServiceResult getExamByNote(String note, int from, int num) throws BackendError {

        List<ExamInfo> ls = mp.findByNote(note, from, num);
        if (ls.size() > 0)
            return ServiceResult.makeResult(SUCCESS, ls);
        throw _ERROR.throwError(EC_DB_SELECT_NOTHING, "查找不到类似备注的考试");
    }

    @Override
    public ServiceResult getExamBeforeTime(Date d, int from, int num) throws BackendError {

        List<ExamInfo> ls = mp.findByTime(d, from, num);
        if (ls.size() > 0)
            return ServiceResult.makeResult(SUCCESS, ls);
        throw _ERROR.throwError(EC_DB_SELECT_NOTHING, "查找不到早于该时间的考试", d);
    }

    @Override
    public ServiceResult getFullExamInfoById(int exam_id,boolean fulluserlist) throws BackendError {
        ExamInfo info = mp.getExamByid(exam_id);
        if (info == null) {
            throw _ERROR.throwError(EC_DB_SELECT_NOTHING, "考试不存在");
        }
        ExamPost obj =new ExamPost();
        obj.setExam_begin(info.getExam_begin());
        obj.setExam_len(info.getExam_len());
        obj.setExam_name(info.getExam_name());
        obj.setExam_note(info.getExam_note());
        if(fulluserlist)
            obj.setStudents(examerMapper.findAllByExamid(exam_id));
        List<ExamQuestionLink> links = examLinkMapper.getExamQuestions(obj.getExam_name());
        for (ExamQuestionLink l : links) {
            Question q = questionMapper.getQuestionByID(l.getQuestionid());
            q.setQue_file(questionMapper.getQuestionFile(l.getQuestionid()));
            ExamQuestion eq = new ExamQuestion();
            eq.setDetail(q);
            eq.setScore(l.getScore());
            eq.setQues_id(q.getQue_id());
            obj.getQuestions().add(eq);
        }
        return ServiceResult.makeResult(SUCCESS, obj);
    }

    @Override
    public ServiceResult updateScore(String examid, int qid, int score) throws BackendError {
        try {
            int c= examLinkMapper.updateScoreByQandE(qid,examid,score);
            if(c>0)
                return ServiceResult.makeResult(SUCCESS);
        }catch (Exception e)
        {

        }
        throw _ERROR.throwError(EC_DB_UPDATE_FAIL,"修改考题分值失败");
    }

    @Override
    public ServiceResult StudentLogin(String nameuid, int exam) throws BackendError {
        ExamPost data = (ExamPost) getFullExamInfoById(exam,false).getObj();
        ExamUser u= examerMapper.findOne(exam,nameuid);
        if(u!=null)
        {
            ExamPost post = new ExamPost();
            post.setStudents(new ArrayList<>(Collections.singletonList(u)));
            post.setExam_begin(data.getExam_begin());
            post.setExam_len(data.getExam_len());
            post.setExam_note(data.getExam_note());
            post.setExam_name(data.getExam_name());
            post.setQuestions(data.getQuestions());
            return ServiceResult.makeResult(SUCCESS,post);
        }
        throw _ERROR.throwError(FC_NOTFOUND, "该考试不存在相关考生数据");
    }

}
