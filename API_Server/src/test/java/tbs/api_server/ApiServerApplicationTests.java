package tbs.api_server;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.annotation.Validated;
import tbs.api_server.backend.mappers.QuestionMapper;
import tbs.api_server.backend.mappers.UserMapper;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.UserDetailInfo;
import tbs.api_server.objects.simple.UserSecurityInfo;
import tbs.api_server.services.UserService;

import java.nio.charset.StandardCharsets;

import static tbs.api_server.config.const_User.*;
@SpringBootTest
class ApiServerApplicationTests {
    @Autowired
    UserService userService;
    @Test
    void contextLoads() {
        ServiceResult result = userService.loginUser("jack", "tongyi0923");

        System.out.println(result);
        if(result!=null&&result.getCode()==userLogin_Success)
        {
            UserSecurityInfo sc= (UserSecurityInfo) result.getObj();
            result= userService.UpdateUserSecQuestion(sc.getId(),"you are a good man?", "yes!!!");
            System.out.println(result);

        }
    }

}
