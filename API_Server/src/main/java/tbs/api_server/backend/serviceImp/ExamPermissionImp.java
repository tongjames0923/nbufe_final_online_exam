package tbs.api_server.backend.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tbs.api_server.backend.mappers.ExamPermissionMapper;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.ExamPermission;
import tbs.api_server.objects.simple.UserDetailInfo;
import tbs.api_server.services.ExamPermissionService;
import tbs.api_server.utility.Error;
import tbs.api_server.utility.MapperStore;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExamPermissionImp implements ExamPermissionService
{


    @Autowired
    private ExamPermissionMapper mp;
    @Override
    public ServiceResult getPermission(int exam_id, int userid)
    {
        ExamPermission ep= mp.getPermission(exam_id, userid);
        return ServiceResult.makeResult(ep==null?0:1, ep);
    }

    @Override
    public ServiceResult getCheckerList(int examid, int from, int num)
    {
        List<Integer> ls= mp.getCheckerList(examid, from, num);
        ArrayList<UserDetailInfo> userDetailInfos =new ArrayList<>();
        for(int i:ls)
        {
            userDetailInfos.add(MapperStore.userMapper.getUserDetailInfoByID(i));
        }

        return ServiceResult.makeResult(userDetailInfos.size(),userDetailInfos);
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public ServiceResult setPermission(int examid, int userid, Boolean read, Boolean write, Boolean check)
    {
        try
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
                if(permission.getWritealbe()!=w)
                {
                    upc+=mp.updatePermissionAtWriteable(userid,examid,w);
                }
                return ServiceResult.makeResult(upc);
            }
            else
            {
                return ServiceResult.makeResult(mp.putPermission(userid,examid,r,w,c)) ;
            }
        }
        catch (Exception e)
        {
            Error._ERROR.rollback();
            return ServiceResult.makeResult(-1);
        }
    }


    @Override
    public ServiceResult getExamsFORREAD(int userid, int from, int num)
    {
        List<ExamPermission> permissions = mp.getReadable(userid,from,num);
        return ServiceResult.makeResult(permissions.size(),permissions);
    }

    @Override
    public ServiceResult getExamsFORWRITE(int userid, int from, int num)
    {
        List<ExamPermission> permissions = mp.getWritealbe(userid,from,num);
        return ServiceResult.makeResult(permissions.size(),permissions);
    }

    @Override
    public ServiceResult getExamsFORCHECK(int user, int from, int num)
    {
        List<ExamPermission> permissions = mp.getCheckable(user,from,num);
        return ServiceResult.makeResult(permissions.size(),permissions);
    }
}
