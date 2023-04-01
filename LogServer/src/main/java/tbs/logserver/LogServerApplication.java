package tbs.logserver;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tbs.api_server.objects.jpa.Repo.CustomPagingWithoutCountRepository;
import tbs.api_server.objects.jpa.Repo.impl.CustomPagingWithoutCountRepositoryImpl;

@SpringBootApplication
@EnableRabbit
@EnableJpaAuditing
@EntityScan(basePackages = {"tbs.api_server.objects.jpa","tbs.api_server"})
@ComponentScan(basePackages = "tbs")
@EnableJpaRepositories(basePackages = "tbs",repositoryBaseClass = CustomPagingWithoutCountRepositoryImpl.class)
public class LogServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogServerApplication.class, args);
    }

}
