package tbs.logserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.remoting.rmi.RmiServiceExporter;
import tbs.api_server.config.constant.JpaDtoConverter;
import tbs.logserver.services.ILogService;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.Map;

@Configuration
public class ServiceExporter {

    @Value("${tbs.log.service}")
    String serviceName;
    @Value("${tbs.log.port}")
    int port;
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    void addConvertForJpa() {
        Map<String, Object> map = applicationContext.getBeansWithAnnotation(JpaDtoConverter.class);
        for (Object o : map.values()) {
            Class c = o.getClass();
            GenericConversionService genericConversionService = ((GenericConversionService) DefaultConversionService.getSharedInstance());
            genericConversionService.addConverter(Map.class, c, m -> {
                try {
                    Object obj = c.newInstance();
                    for (Field field : c.getDeclaredFields()) {
                        String name = field.getName();
                        if (m.containsKey(name)) {
                            field.setAccessible(true);
                            field.set(obj, m.get(name));
                        }
                    }
                    return obj;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return null;
                }
            });
        }
    }

    @Bean
    public RmiServiceExporter logService(ILogService service) {
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setService(service);
        exporter.setServiceName(serviceName);
        exporter.setRegistryPort(port);
        exporter.setServiceInterface(ILogService.class);
        return exporter;
    }
}
