package tbs.logserver.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Value("${tbs.queue.log}")
    String logQ;
    @Bean
    public Queue LogQueue()
    {
        return new Queue(logQ);
    }
}
