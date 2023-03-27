package tbs.api_server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.retry.annotation.EnableRetry;
import tbs.logserver.services.ILogService;

@Configuration
@EnableRetry
public class RemoteInvokeConfig {

    @Value("${tbs.log.server}")
    String log_server;
    @Bean
    public RmiProxyFactoryBean logService()
    {
        RmiProxyFactoryBean bean=new RmiProxyFactoryBean();
        bean.setServiceInterface(ILogService.class);
        bean.setServiceUrl(log_server+"logService");
        return bean;
    }
}
