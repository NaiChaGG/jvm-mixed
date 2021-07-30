package io.github.trapspring.datajdbc;

/**
 * @ClassNAME MybatisPlusConfig
 * @Description TODO
 * @Author 奶茶
 * @Date 2021/7/30 10:27 上午
 * @Version 1.0
 */

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置分页插件
 *
 */
@Configuration
public class AutoMybatisPlusConfiguration {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
