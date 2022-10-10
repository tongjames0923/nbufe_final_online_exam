package tbs.api_server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.annotation.Validated;
import tbs.api_server.backend.mappers.UserMapper;
import tbs.api_server.objects.simple.UserSecurityInfo;

import static tbs.api_server.config.const_User.*;
@SpringBootTest
class ApiServerApplicationTests {

    @Autowired
    UserMapper mp;
    @Test
    void contextLoads() {


       // System.out.println(mp.deleteUser(4));
        //System.out.println(mp.setValueForUserSecurity(2, usec_ques, "are you a good man ?"));
    }

}
