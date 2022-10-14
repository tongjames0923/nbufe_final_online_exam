package tbs.api_server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.UserSecurityInfo;
import tbs.api_server.services.UserService;
import tbs.api_server.utility.SecurityTools;
import tbs.api_server.utility.UserUtility;

import static tbs.api_server.config.constant.const_User.*;
@SpringBootTest
class ApiServerApplicationTests {
    @Autowired
    UserService userService;
    @Test
    void contextLoads() {
        try
        {
          String z= UserUtility.passwordEncode("hello world");
          int a=10;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        // System.out.println(UserUtility.min_username_length);
//        ServiceResult result = userService.loginUser("jack", "tongyi0923");
//
//        System.out.println(result);
//        if(result!=null&&result.getCode()==userLogin_Success)
//        {
//            UserSecurityInfo sc= (UserSecurityInfo) result.getObj();
//            result= userService.UpdateUserSecQuestion(sc.getId(),"you are a good man?", "yes!!!");
//            System.out.println(result);
//
//        }
    }

}
