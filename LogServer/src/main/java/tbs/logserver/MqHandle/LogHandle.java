package tbs.logserver.MqHandle;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tbs.api_server.objects.simple.LogPojo;
import tbs.logserver.backend.mappers.LogMapper;

import javax.annotation.Resource;
import java.io.IOException;

@Component
public class LogHandle {

    @Resource
    LogMapper mapper;
    @RabbitListener(queues = "log_queue")
    @Transactional
    public void writeLog(LogPojo log, Channel cha, Message msg)
    {
       int t= mapper.writeLog(log);
       if(t>0) {
           try {
               cha.basicAck(msg.getMessageProperties().getDeliveryTag(),false);
           } catch (IOException e) {
               e.printStackTrace();
           }
       }

    }
}
