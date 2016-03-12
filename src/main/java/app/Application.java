package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableAutoConfiguration
@Configuration
@ComponentScan({"api", "security", "repositories", "models"})
@EnableMongoRepositories(basePackages = {"repositories"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}