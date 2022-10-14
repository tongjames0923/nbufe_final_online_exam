package tbs.api_server.publicAPI;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tbs.api_server.objects.NetResult;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.UserDetailInfo;
import tbs.api_server.objects.simple.UserSecurityInfo;
import tbs.api_server.services.UserService;
import static tbs.api_server.config.const_User.*;

@Controller
public class UserController {

    @Autowired
    UserService service;


    @RequestMapping("/login")
    @ResponseBody
    public NetResult<UserDetailInfo> login(String username,String password)
    {
        try
        {
            UserDetailInfo info = null;
            if(username.length()>=4&&password.length()>6)
            {
               ServiceResult<UserSecurityInfo> sc= service.loginUser(username, password);
               if(sc.getCode()==userLogin_Success)
               {
                   info=service.getUserInfo(sc.getObj().getId()).getObj();
                   return new NetResult<>(true
                           ,info,"success");
               }
               else
               {
                   return new NetResult<>(false, null,String.format("error:%d",sc.getCode()));
               }
            }
            else
            {
                return new NetResult<>(false,null,"invalid username or password");
            }
        }catch (Exception e)
        {
            return new NetResult<>(false,null,"UNkNOWN ERROR");
        }
    }


}
