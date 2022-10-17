package tbs.api_server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("tbs.api_server.backend.mappers")
@EnableTransactionManagement
public class ApiServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiServerApplication.class, args);
    }

}
