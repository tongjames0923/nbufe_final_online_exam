package tbs.api_server;

import cn.hutool.extra.spring.EnableSpringUtil;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tbs.api_server.config.ApplicationConfig;
import tbs.api_server.utility.FileUtility;

@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
@EnableAsync
@EnableAspectJAutoProxy
@EnableSpringUtil
@EnableRabbit
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
