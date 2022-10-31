package tbs.api_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tbs.api_server.config.ApplicationConfig;
import tbs.api_server.utility.FileUtility;

@SpringBootApplication
@EnableTransactionManagement
public class ApiServerApplication {

    public static void Starter()
    {
        String[] dirs={ApplicationConfig.resourceDir,ApplicationConfig.ReplyDir,ApplicationConfig.CheckFileDir};
        for(String str:dirs)
        {
            FileUtility.existDIR(str,null);
        }
    }

    public static void main(String[] args) {
        Starter();

        SpringApplication.run(ApiServerApplication.class, args);
    }

}
