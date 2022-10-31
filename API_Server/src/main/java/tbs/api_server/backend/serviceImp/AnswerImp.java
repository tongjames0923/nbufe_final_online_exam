package tbs.api_server.backend.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import tbs.api_server.backend.mappers.AnswerMapper;
import tbs.api_server.backend.mappers.ExamPermissionMapper;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.ExamPermission;
import tbs.api_server.objects.simple.StandardAnswer;
import tbs.api_server.services.AnswerService;
import tbs.api_server.utility.Error;

import static tbs.api_server.utility.Error.*;

@Service
public class AnswerImp implements AnswerService
{
    @Autowired
    AnswerMapper mp;
    @Autowired
    ExamPermissionMapper exmp;

    @Override
    public ServiceResult getAnswer(int ques_id,int user) throws Error.BackendError
    {
        ExamPermission permission = exmp.getPermission(user, ques_id);
        if (permission == null)
            throw Error._ERROR.throwError(FC_UNAVALIABLE, "用户不存在相关权限", new int[]{ques_id, user});
        if (permission.getWritealbe() ==0&&permission.getCheckable()==0)
            throw _ERROR.throwError(EC_LOW_PERMISSIONS, "查看答案需要拥有可写或可批阅权限");
        StandardAnswer answer = mp.getAnswerForQuestion(ques_id);
        if (answer == null)
            throw Error._ERROR.throwError(Error.EC_DB_SELECT_NOTHING, "此问题不存在答案", ques_id);

        return ServiceResult.makeResult(SUCCESS, answer);
    }

    @Override
    public ServiceResult uploadAnswer(int ques_id, int user, byte[] answer, Byte[] analysis) throws BackendError
    {
        ExamPermission permission = exmp.getPermission(user, ques_id);
        if (permission == null)
            throw Error._ERROR.throwError(FC_UNAVALIABLE, "用户不存在相关权限", new int[]{ques_id, user});
        if (permission.getWritealbe() ==0)
            throw _ERROR.throwError(EC_LOW_PERMISSIONS, "上传答案需要拥有可写权限");
        try
        {
            int c = mp.insertAnswer(ques_id, answer, analysis);
            if (c > 0)
                return ServiceResult.makeResult(SUCCESS, null);
            else
                throw _ERROR.throwError(EC_DB_INSERT_FAIL, "提交答案失败");

        } catch (DuplicateKeyException e)
        {
            throw _ERROR.throwError(FC_DUPLICATE, "不能为同一道题重复提交答案，请先删除答案");
        }catch (BackendError e)
        {
            throw  e;
        }

    }

    @Override
    public ServiceResult deleteAnswer(int ques_id, int user) throws BackendError
    {
        ExamPermission permission = exmp.getPermission(user, ques_id);
        if (permission == null)
            throw Error._ERROR.throwError(FC_UNAVALIABLE, "用户不存在相关权限", new int[]{ques_id, user});
        if (permission.getWritealbe() == 0)
            throw _ERROR.throwError(EC_LOW_PERMISSIONS, "删除答案需要拥有可写权限");
        int c=mp.deleteAnswer(ques_id);
        if(c>0)
            return ServiceResult.makeResult(SUCCESS);
        else
            throw _ERROR.throwError(EC_DB_DELETE_FAIL,"不存在问题的答案",ques_id);
    }
}
