package io.github.jojoti.grpcstartersbexamples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

/**
 * @author Wang Yue
 */
@SpringBootApplication
@EnableJdbcRepositories
@Configuration
@AutoConfigureAfter({HibernateJpaAutoConfiguration.class})
public class AppGRpcStarterKtExample {

    public static void main(String[] args) {
        SpringApplication.run(AppGRpcStarterKtExample.class, args);
    }

}
