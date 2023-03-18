package tbs.api_server.utils;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import tbs.api_server.objects.simple.LogPojo;

import javax.annotation.Resource;

@Component
public class LogQueueSender {
    @Resource
    RabbitTemplate template;


    @Async
    public void send(String route,LogPojo logPojo)
    {
        template.convertAndSend(route,logPojo);
    }
}
