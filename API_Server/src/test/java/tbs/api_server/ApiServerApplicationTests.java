package tbs.api_server;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import tbs.api_server.services.UserService;
import tbs.api_server.utility.FileUtility;

import javax.annotation.Resource;

@SpringBootTest
class ApiServerApplicationTests {
    @Autowired
    UserService userService;


    @Resource
    RabbitTemplate template;
    @Value("${tbs.queue.log}")
    String logQ;
    @Test
    void contextLoads() {
        template.convertAndSend(logQ,"htlloo");
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
