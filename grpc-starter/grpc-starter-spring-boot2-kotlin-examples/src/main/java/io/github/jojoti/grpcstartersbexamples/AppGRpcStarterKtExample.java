package io.github.jojoti.grpcstartersbexamples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

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
