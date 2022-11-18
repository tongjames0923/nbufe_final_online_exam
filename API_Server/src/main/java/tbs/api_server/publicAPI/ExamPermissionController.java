package tbs.api_server.publicAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tbs.api_server.objects.NetResult;
import tbs.api_server.objects.simple.ExamPermission;
import tbs.api_server.objects.simple.UserDetailInfo;
import tbs.api_server.services.ExamPermissionService;
import tbs.api_server.utility.ApiMethod;

import java.util.ArrayList;
import java.util.HashMap;

import static tbs.api_server.utility.Error.*;

@RestController
@RequestMapping("exampermissions/*")
@Scope("prototype")
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
            public NetResult action() throws BackendError, Exception
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
        }).method();
    }
    @RequestMapping("set")
    @Transactional
    public NetResult setPermission(int examid, int userid, int targetid,
                                   @RequestParam(required = false) Integer read,
                                   @RequestParam(required = false) Integer write,
                                   @RequestParam(required = false) Integer check)
    {
        return ApiMethod.make(new ApiMethod.IAction()
        {
            @Override
            public NetResult action() throws BackendError, Exception
            {
                ExamPermission permission= (ExamPermission) service.getPermission(examid, userid).getObj();
                if(permission.getWritealbe()==1)
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
        }).method();
    }
    @RequestMapping("list")
    public NetResult userOwnPermission(int userid,int from,int num)
    {
        return ApiMethod.make(new ApiMethod.IAction()
        {
            @Override
            public NetResult action() throws BackendError, Exception
            {
                ArrayList<ExamPermission>[] arrayList=new ArrayList[3];
                try
                {
                    arrayList[0]= (ArrayList<ExamPermission>) service.getExamsFORREAD(userid, from, num).getObj();
                }catch (Exception e)
                {
                }
                try
                {
                    arrayList[1]= (ArrayList<ExamPermission>) service.getExamsFORWRITE(userid, from, num).getObj();
                }catch (Exception e)
                {

                }
                try
                {
                    arrayList[2]= (ArrayList<ExamPermission>) service.getExamsFORCHECK(userid, from, num).getObj();
                }catch (Exception e)
                {

                }
                String[] names={"readable", "writable", "checkable"};
                int j=0;
                HashMap<String,ArrayList<ExamPermission>> map = new HashMap<>();
                for(ArrayList<ExamPermission> i:arrayList)
                {
                    if(i!=null)
                    {
                        map.put(names[j++],i );
                    }
                }
                return NetResult.makeResult(SUCCESS,null,map);
            }
        }).method();
    }
}
