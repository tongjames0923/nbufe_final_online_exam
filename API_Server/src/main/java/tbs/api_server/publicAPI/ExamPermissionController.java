package tbs.api_server.publicAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tbs.api_server.objects.NetResult;
import tbs.api_server.objects.simple.ExamPermission;
import tbs.api_server.objects.simple.UserDetailInfo;
import tbs.api_server.objects.simple.UserSecurityInfo;
import tbs.api_server.services.ExamPermissionService;
import tbs.api_server.utility.ApiMethod;

import java.util.ArrayList;
import java.util.HashMap;

import static tbs.api_server.utility.Error.*;

@RestController
@RequestMapping("exampermissions/*")
public class ExamPermissionController
{

    @Autowired
    ExamPermissionService service;

    @RequestMapping("aboutUser")
    public NetResult exampermissions(int examid,int from,int num)
    {
       return ApiMethod.make(new ApiMethod.IAction()
        {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception
            {
                ArrayList<UserDetailInfo>[] arrayList=new ArrayList[3];
                try
                {
                    arrayList[0]= (ArrayList<UserDetailInfo>) service.getReadList(examid, from, num).getObj();
                }catch (Exception e)
                {
                }
                try
                {
                    arrayList[1]= (ArrayList<UserDetailInfo>) service.getWriteList(examid, from, num).getObj();
                }catch (Exception e)
                {

                }
                try
                {
                    arrayList[2]= (ArrayList<UserDetailInfo>) service.getCheckerList(examid, from, num).getObj();
                }catch (Exception e)
                {

                }
                String[] names={"readable", "writable", "checkable"};
                int j=0;
                HashMap<String,ArrayList<UserDetailInfo>> map = new HashMap<>();
                for(ArrayList<UserDetailInfo> i:arrayList)
                {
                    if(i!=null)
                    {
                        map.put(names[j++],i );
                    }
                }
                return NetResult.makeResult(SUCCESS,null,map);
            }
        }).methodWithLogined();
    }
    @RequestMapping("set")
    @Transactional
    public NetResult setPermission(int examid, int targetid,
                                   @RequestParam(required = false,defaultValue = "1") Integer read,
                                   @RequestParam(required = false,defaultValue = "0") Integer write,
                                   @RequestParam(required = false,defaultValue = "0") Integer check)
    {
        return ApiMethod.make(new ApiMethod.IAction()
        {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception
            {
                ExamPermission permission= (ExamPermission) service.getPermission(examid, applyUser.getId()).getObj();
                if(permission.getWriteable()==1||applyUser.getLevel()>=2)
                {
                    Boolean r=null,w=null,c=null;
                    if(read!=null)
                        r=read!=0;
                    if(write!=null)
                        w=write!=0;
                    if(check!=null)
                        c=check!=0;
                   return NetResult.makeResult(service.setPermission(examid,targetid,r,w,c),null);
                }
                else
                    throw _ERROR.throwError(EC_LOW_PERMISSIONS,"权限必须是可写用户才能更改权限设置");
            }
        }).methodWithLogined();
    }
    @RequestMapping("listExamAccess")
    public NetResult examUsers(int examid)
    {
        return ApiMethod.makeResult(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                return NetResult.makeResult(service.getPermission(examid),null);
            }
        });
    }
}
