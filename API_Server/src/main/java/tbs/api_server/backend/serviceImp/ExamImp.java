package tbs.api_server.backend.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import tbs.api_server.backend.mappers.ExamMapper;
import tbs.api_server.backend.mappers.ExamPermissionMapper;
import tbs.api_server.config.constant.const_Exam;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.ExamInfo;
import tbs.api_server.objects.simple.ExamPermission;
import tbs.api_server.services.ExamService;

import java.util.Date;
import java.util.List;

import static tbs.api_server.utility.Error.*;

@Service
public class ExamImp implements ExamService
{

    @Autowired
    ExamMapper mp;

    @Autowired
    ExamPermissionMapper permit;

    @Override
    public ServiceResult getExamByStatus(int status, int from, int num) throws BackendError
    {
        List<ExamInfo> list = mp.getExamsByStatus(status, from, num);
        if(list!=null&&list.size()>0)
        {
            return ServiceResult.makeResult(SUCCESS,list);
        }
        throw _ERROR.throwError(EC_DB_SELECT_NOTHING,"不存在此状态的考试",status);
    }

    @Override
    public ServiceResult getExamByName(String name) throws BackendError
    {
        ExamInfo examInfo = mp.getExamIDByExamName(name);
        if(examInfo!=null)
        {
            return ServiceResult.makeResult(SUCCESS,examInfo);
        }
        throw _ERROR.throwError(EC_DB_SELECT_NOTHING,"不存在此名称的考试",name);
    }
    @Override
    public ServiceResult uploadExam(int user,String name, Date beg, String note, Integer length, byte[] file) throws BackendError
    {
        int c= mp.uploadExam(name,beg,note,file,length);
        if(c<=0)
        throw _ERROR.throwError(EC_DB_INSERT_FAIL,"上传考试失败",null);
        ExamInfo info= mp.getExamIDByExamName(name);
        c= permit.putPermission(user,info.getExam_id(),1,1,1);
        if(c<=0)
            throw _ERROR.throwError(EC_DB_INSERT_FAIL,"考试权限除市场赋予失败");
        return ServiceResult.makeResult(SUCCESS,null);
    }

    @Override
    public ServiceResult deleteExam(int id, int userid) throws BackendError
    {
        if(needWrite(userid,id))
        {
           int c= mp.deleteExam(id);
           if(c>0)
               return ServiceResult.makeResult(SUCCESS);
           throw _ERROR.throwError(EC_DB_DELETE_FAIL,"删除考试失败");
        }
        else
        {
            throw _ERROR.throwError(EC_LOW_PERMISSIONS,"权限不足，不可删除考试");
        }
    }

    @Override
    public ServiceResult list(int from, int num) throws BackendError
    {
        List<ExamInfo> list = mp.list(from, num);
        if(list.size() > 0)
            return ServiceResult.makeResult(SUCCESS,list);
        throw _ERROR.throwError(EC_DB_SELECT_NOTHING,"不存在考试");

    }

    private boolean needCheck(int user,int examid)
    {
        ExamPermission permission= permit.getPermission(user,examid);
        if(permission==null)
            return false;
        return permission.getWritealbe()!=0||permission.getCheckable()!=0;
    }
    private boolean needWrite(int user,int examid)
    {
        ExamPermission permission= permit.getPermission(user,examid);
        if(permission==null)
            return false;
        return permission.getWritealbe()!=0;
    }

    @Override
    public ServiceResult updateStatus(int status,int examid) throws BackendError
    {
        int c= mp.updateExam(examid, const_Exam.col_status,status);
        if(c>0)
            return ServiceResult.makeResult(SUCCESS);
        throw _ERROR.throwError(EC_DB_UPDATE_FAIL,"更新状态失败");
    }

    @Override
    public ServiceResult updateLen(int len, int user, int examid) throws BackendError
    {
        int c= mp.updateExam(examid, const_Exam.col_len,len);
        if(c>0)
            return ServiceResult.makeResult(SUCCESS);
        throw _ERROR.throwError(EC_DB_UPDATE_FAIL,"更新状态失败");
    }

    @Override
    public ServiceResult updateNote(String note, int user, int examid) throws BackendError
    {
        int c= mp.updateExam(examid, const_Exam.col_notes,note);
        if(c>0)
            return ServiceResult.makeResult(SUCCESS);
        throw _ERROR.throwError(EC_DB_UPDATE_FAIL,"更新状态失败");
    }

    @Override
    public ServiceResult updateBegin(Date date, int user, int examid) throws BackendError
    {
        int c= mp.updateExam(examid, const_Exam.col_begin,date);
        if(c>0)
            return ServiceResult.makeResult(SUCCESS);
        throw _ERROR.throwError(EC_DB_UPDATE_FAIL,"更新状态失败");
    }

    @Override
    public ServiceResult updateName(String name, int user, int examid) throws BackendError
    {
        try
        {
            int c= mp.updateExam(examid, const_Exam.col_name,name);
            if(c>0)
                return ServiceResult.makeResult(SUCCESS);
            throw _ERROR.throwError(EC_DB_UPDATE_FAIL,"更新状态失败");
        }catch (DuplicateKeyException ec)
        {
           throw  _ERROR.throwError(FC_DUPLICATE,"重复考试名称");
        }catch (Exception e)
        {
            throw _ERROR.throwError(EC_UNKNOWN,e.getMessage(),e);
        }

    }
}
