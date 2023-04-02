package tbs.logserver;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableRabbit
@EnableTransactionManagement
public class LogServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogServerApplication.class, args);
    }

}
