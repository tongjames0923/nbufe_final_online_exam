package tbs.api_server.backend.serviceImp;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import tbs.api_server.backend.mappers.ExamMapper;
import tbs.api_server.backend.mappers.ExamPermissionMapper;
import tbs.api_server.backend.mappers.QuestionMapper;
import tbs.api_server.backend.mappers.UserMapper;
import tbs.api_server.config.constant.const_Exam;
import tbs.api_server.config.constant.const_User;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.compound.exam.ExamPost;
import tbs.api_server.objects.compound.exam.ExamUser;
import tbs.api_server.objects.simple.ExamInfo;
import tbs.api_server.objects.simple.ExamPermission;
import tbs.api_server.objects.simple.Question;
import tbs.api_server.objects.simple.UserDetailInfo;
import tbs.api_server.services.ExamService;
import tbs.api_server.utility.TimeUtil;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import static tbs.api_server.utility.Error.*;

@Service
@Scope("prototype")
public class ExamImp implements ExamService
{

    @Autowired
    ExamMapper mp;

    @Autowired
    ExamPermissionMapper permit;


    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionMapper questionMapper;

    @Override
    public ServiceResult countExams(int user) {

            return ServiceResult.makeResult(SUCCESS,mp.countStaff());
    }

    @Override
    public ServiceResult getExamByStatus(int status, int from, int num) throws BackendError
    {
        List<ExamInfo> list = mp.getExamsByStatus(status, from, num);
        if (list != null && list.size() > 0)
        {
            return ServiceResult.makeResult(SUCCESS, list);
        }
        throw _ERROR.throwError(EC_DB_SELECT_NOTHING, "不存在此状态的考试", status);
    }

    @Override
    public ServiceResult getExamByName(String name) throws BackendError
    {
        ExamInfo examInfo = mp.getExamIDByExamName(name);
        if (examInfo != null)
        {
            return ServiceResult.makeResult(SUCCESS, examInfo);
        }
        throw _ERROR.throwError(EC_DB_SELECT_NOTHING, "不存在此名称的考试", name);
    }

    @Override
    public ServiceResult uploadExam(int user, ExamPost data)
            throws BackendError
    {
        byte[] file=null;
        try
        {
           file = JSON.toJSONString(data).getBytes(StandardCharsets.UTF_8);
        }catch (Exception e)
        {
            throw _ERROR.throwError(EC_UNKNOWN,"数据文件转换失败");
        }
        int c = mp.uploadExam(data.getExam_name(), data.getExam_begin(), data.getExam_note(),file, data.getExam_len());
        if (c <= 0)
            throw _ERROR.throwError(EC_DB_INSERT_FAIL, "上传考试失败", null);
        ExamInfo info = mp.getExamIDByExamName(data.getExam_name());
        c = permit.putPermission(user, info.getExam_id(), 1, 1, 1);
        if (c <= 0)
            throw _ERROR.throwError(EC_DB_INSERT_FAIL, "考试权限赋予失败");
        return ServiceResult.makeResult(SUCCESS, null);
    }

    @Override
    public ServiceResult deleteExam(int id, int userid) throws BackendError
    {
        if (needWrite(userid, id))
        {
            int c = mp.deleteExam(id);
            if (c > 0)
                return ServiceResult.makeResult(SUCCESS);
            throw _ERROR.throwError(EC_DB_DELETE_FAIL, "删除考试失败");
        } else
        {
            throw _ERROR.throwError(EC_LOW_PERMISSIONS, "权限不足，不可删除考试");
        }
    }

    @Override
    public ServiceResult list(int from, int num) throws BackendError
    {
        List<ExamInfo> list = mp.list(from, num);
        if (list.size() > 0)
            return ServiceResult.makeResult(SUCCESS, list);
        throw _ERROR.throwError(EC_DB_SELECT_NOTHING, "不存在考试");

    }

    private boolean needCheck(int user, int examid)
    {
        ExamPermission permission = permit.getPermission(user, examid);
        if (permission == null)
            return false;
        return permission.getWritealbe() != 0 || permission.getCheckable() != 0;
    }

    private boolean needWrite(int user, int examid)
    {

        UserDetailInfo userDetailInfo= userMapper.getUserDetailInfoByID(user);
        if(userDetailInfo==null)
        {
            return  false;
        }
        if(userDetailInfo.getLevel()==const_User.LEVEL_EXAM_STAFF)
        {
            return true;
        }
        ExamPermission permission = permit.getPermission(user, examid);
        if (permission == null)
            return false;
        return permission.getWritealbe() != 0;
    }

    @Override
    public ServiceResult updateStatus(int status, int examid) throws BackendError
    {
        int c = mp.updateExam(examid, const_Exam.col_status, status);
        if (c > 0)
            return ServiceResult.makeResult(SUCCESS);
        throw _ERROR.throwError(EC_DB_UPDATE_FAIL, "更新状态失败");
    }

    @Override
    public ServiceResult updateLen(int len, int user, int examid) throws BackendError
    {
        ExamInfo info= mp.getExamByid(examid);
        if(info==null)
            throw _ERROR.throwError(EC_DB_SELECT_NOTHING,"不存在的考试id");
        if(needWrite(user,examid))
        {
            if(TimeUtil.isStart(info))
                throw _ERROR.throwError(FC_UNAVALIABLE,"考试开始后不支持更新考试时长");
            int c = mp.updateExam(examid, const_Exam.col_len, len);
            if (c > 0)
                return ServiceResult.makeResult(SUCCESS);
            throw _ERROR.throwError(EC_DB_UPDATE_FAIL, "更新考试长度失败");
        }
        else
            throw _ERROR.throwError(EC_LOW_PERMISSIONS,"更改时长必须拥有写入权限");


    }

    @Override
    public ServiceResult updateNote(String note, int user, int examid) throws BackendError
    {
        if(needWrite(user,examid))
        {
            int c = mp.updateExam(examid, const_Exam.col_notes, note);
            if (c > 0)
                return ServiceResult.makeResult(SUCCESS);
            throw _ERROR.throwError(EC_DB_UPDATE_FAIL, "更新备注失败");
        }
        else
            throw _ERROR.throwError(EC_LOW_PERMISSIONS,"更改时长必须拥有写入权限");

    }

    @Override
    public ServiceResult updateBegin(Date date, int user, int examid) throws BackendError
    {
        if(date.before(new Date()))
            throw _ERROR.throwError(EC_InvalidParameter,"不支持修改到过去时间");
        ExamInfo info= mp.getExamByid(examid);
        if(info==null)
            throw _ERROR.throwError(EC_DB_SELECT_NOTHING,"不存在的考试id");
        if(needWrite(user,examid)) {
            if (TimeUtil.isStart(info))
                throw _ERROR.throwError(FC_UNAVALIABLE, "考试开始后不支持考试开始时间");
            int c = mp.updateExam(examid, const_Exam.col_begin, date);
            if (c > 0)
                return ServiceResult.makeResult(SUCCESS);
            throw _ERROR.throwError(EC_DB_UPDATE_FAIL, "更新考试开始时间失败");
        }
        else
            throw _ERROR.throwError(EC_LOW_PERMISSIONS,"更改考试开始时间必须拥有写入权限");


    }

    @Override
    public ServiceResult updateName(String name, int user, int examid) throws BackendError
    {
        try
        {
            if(needWrite(user,examid))
            {
                int c = mp.updateExam(examid, const_Exam.col_name, name);
                if (c > 0)
                    return ServiceResult.makeResult(SUCCESS);
                throw _ERROR.throwError(EC_DB_UPDATE_FAIL, "更新考试名称失败");
            }
            else
            throw _ERROR.throwError(EC_LOW_PERMISSIONS,"更改考试名称必须拥有写入权限");
        } catch (DuplicateKeyException ec)
        {
            throw _ERROR.throwError(FC_DUPLICATE, "重复考试名称");
        } catch (Exception e)
        {
            throw _ERROR.throwError(EC_UNKNOWN, e.getMessage(), e);
        }

    }

    @Override
    public ServiceResult getExamByNote(String note, int from, int num) throws BackendError {

        List<ExamInfo> ls=mp.findByNote(note,from,num);
        if(ls.size()>0)
            return ServiceResult.makeResult(SUCCESS,ls);
        throw _ERROR.throwError(EC_DB_SELECT_NOTHING,"查找不到类似备注的考试");
    }

    @Override
    public ServiceResult getExamBeforeTime(Date d, int from, int num) throws BackendError {

        List<ExamInfo> ls=mp.findByTime(d,from,num);
        if(ls.size()>0)
            return ServiceResult.makeResult(SUCCESS,ls);
        throw _ERROR.throwError(EC_DB_SELECT_NOTHING,"查找不到早于该时间的考试",d);
    }

    @Override
    public ServiceResult getFullExamInfoById(int exam_id) throws BackendError {
        ExamInfo info= mp.getExamByid(exam_id);
        if(info==null)
        {
            throw _ERROR.throwError(EC_DB_SELECT_NOTHING,"考试不存在");
        }
        ExamPost obj=JSON.parseObject(mp.getExamFile(exam_id),ExamPost.class);

        for(int i=0;i<obj.getQuestions().size();i++)
        {
            Question q=questionMapper.getQuestionByID(obj.getQuestions().get(i).getQues_id());
            obj.getQuestions().get(i).setDetail(q);
        }
        return ServiceResult.makeResult(SUCCESS,obj);
    }

    @Override
    public ServiceResult StudentLogin(String name, String id, String number, int exam) throws BackendError {
         ExamPost data= (ExamPost) getFullExamInfoById(exam).getObj();
         for(ExamUser u:data.getStudents())
         {
             if(u.getId().equals(id)&&u.getName().equals(name)&&u.getNumber().equals(number))
                 return ServiceResult.makeResult(SUCCESS,data);
         }
         throw _ERROR.throwError(FC_NOTFOUND,"该考试不存在相关考生数据");
    }

}
