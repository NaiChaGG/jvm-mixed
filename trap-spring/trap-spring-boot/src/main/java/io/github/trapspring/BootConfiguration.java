package io.github.trapspring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@Configuration(proxyBeanMethods = false)
public class BootConfiguration {

    @Bean
    public Debugs debugs(@Value("${debug:false}") boolean debug, @Value("${trace:false}") boolean trace) {
        return new Debugs(debug, trace);
    }

}
