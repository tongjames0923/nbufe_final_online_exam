package tbs.logserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;
import tbs.logserver.services.ILogService;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

@Configuration
public class ServiceExporter {

    @Value("${tbs.log.service}")
    String serviceName;
    @Value("${tbs.log.port}")
    int port;


    @PreDestroy
    void flushData()
    {
        BatchCenter.Center.flush(true);
        System.out.println("flushed all data to database.bye~");
    }


    @Bean
    public RmiServiceExporter logService(ILogService service)
    {
        RmiServiceExporter exporter=new RmiServiceExporter();
        exporter.setService(service);
        exporter.setServiceName(serviceName);
        exporter.setRegistryPort(port);
        exporter.setServiceInterface(ILogService.class);
        return exporter;
    }
}
