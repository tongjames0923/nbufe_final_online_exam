package tbs.api_server.backend.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import tbs.api_server.backend.mappers.ExamPermissionMapper;
import tbs.api_server.backend.mappers.UserMapper;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.compound.exam.ExamPermitionVo;
import tbs.api_server.objects.simple.ExamPermission;
import tbs.api_server.objects.simple.UserDetailInfo;
import tbs.api_server.services.ExamPermissionService;

import java.util.ArrayList;
import java.util.List;

import static tbs.api_server.utility.Error.*;

@Service
@Scope("prototype")
public class ExamPermissionImp implements ExamPermissionService
{


    @Autowired
    private ExamPermissionMapper mp;
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServiceResult getPermission(int examid) {
        List<ExamPermission> permissions=mp.getExamPermittion(examid);
        List<ExamPermitionVo> list=new ArrayList<>();
        for(ExamPermission e :permissions)
        {
          UserDetailInfo detailInfo=  userMapper.getUserDetailInfoByID(e.getUser());
          list.add(new ExamPermitionVo(e,detailInfo));
        }
        return ServiceResult.makeResult(SUCCESS,list);
    }

    @Override
    public ServiceResult getPermission(int exam_id, int userid) throws BackendError {
        ExamPermission ep= mp.getPermission(userid,exam_id);
        if(ep!=null)
        return ServiceResult.makeResult(SUCCESS, ep);
        else
            throw _ERROR.throwError(EC_DB_SELECT_NOTHING,"不存在针对该考试的权限",exam_id);
    }

    @Override
    public ServiceResult getCheckerList(int examid, int from, int num) throws BackendError {
        List<Integer> ls= mp.getCheckerList(examid, from, num);
        ArrayList<UserDetailInfo> userDetailInfos =new ArrayList<>();
        for(int i:ls)
        {
            userDetailInfos.add(userMapper.getUserDetailInfoByID(i));
        }
        if(userDetailInfos.size()>0)
        return ServiceResult.makeResult(userDetailInfos.size(),userDetailInfos);
        else
            throw _ERROR.throwError(EC_DB_SELECT_NOTHING,"该考试不存在批阅人",examid);
    }

    @Override
    public ServiceResult getReadList(int examid, int from, int num) throws BackendError
    {
        List<Integer> ls= mp.getReaderList(examid, from, num);
        ArrayList<UserDetailInfo> userDetailInfos =new ArrayList<>();
        for(int i:ls)
        {
            userDetailInfos.add(userMapper.getUserDetailInfoByID(i));
        }
        if(userDetailInfos.size()>0)
            return ServiceResult.makeResult(userDetailInfos.size(),userDetailInfos);
        else
            throw _ERROR.throwError(EC_DB_SELECT_NOTHING,"该考试不存在可读人",examid);
    }

    @Override
    public ServiceResult getWriteList(int examid, int from, int num) throws BackendError
    {
        List<Integer> ls= mp.getWriterList(examid, from, num);
        ArrayList<UserDetailInfo> userDetailInfos =new ArrayList<>();
        for(int i:ls)
        {
            userDetailInfos.add(userMapper.getUserDetailInfoByID(i));
        }
        if(userDetailInfos.size()>0)
            return ServiceResult.makeResult(userDetailInfos.size(),userDetailInfos);
        else
            throw _ERROR.throwError(EC_DB_SELECT_NOTHING,"该考试不存在编写人",examid);
    }

    @Override
    public ServiceResult setPermission(int examid, int userid, Boolean read, Boolean write, Boolean check)
    {
            ExamPermission permission = mp.getPermission(userid,examid);
            int r=read?1:0,w=write?1:0,c=check?1:0;
            if(permission!=null)
            {
                int upc=0;
                if(permission.getCheckable()!=c)
                {
                    upc+=mp.updatePermissionAtCheckable(userid,examid,c);
                }
                if(permission.getReadable()!=r)
                {
                    upc+=mp.updatePermissionAtReadable(userid,examid,r);
                }
                if(permission.getWriteable()!=w)
                {
                    upc+=mp.updatePermissionAtWriteable(userid,examid,w);
                }
                return ServiceResult.makeResult(SUCCESS,upc);
            }
            else
            {
                return ServiceResult.makeResult(SUCCESS,mp.putPermission(userid,examid,r,w,c)) ;
            }
    }


    @Override
    public ServiceResult getExamsFORREAD(int userid, int from, int num) throws BackendError {
        List<ExamPermission> permissions = mp.getReadable(userid,from,num);
        if(permissions.size()>0)
        return ServiceResult.makeResult(permissions.size(),permissions);
        else
            throw _ERROR.throwError(EC_DB_SELECT_NOTHING,"用户不存在可读的考试");
    }

    @Override
    public ServiceResult getExamsFORWRITE(int userid, int from, int num) throws BackendError {
        List<ExamPermission> permissions = mp.getWritealbe(userid,from,num);
        if(permissions.size()>0)
        return ServiceResult.makeResult(permissions.size(),permissions);
        else
            throw _ERROR.throwError(EC_DB_SELECT_NOTHING,"用户不存在可修改的考试");
    }

    @Override
    public ServiceResult getExamsFORCHECK(int user, int from, int num) throws BackendError {
        List<ExamPermission> permissions = mp.getCheckable(user,from,num);
        if(permissions.size()>0)
        return ServiceResult.makeResult(permissions.size(),permissions);
        else
            throw _ERROR.throwError(EC_DB_SELECT_NOTHING,"不存在可批阅的考试");
    }
}
