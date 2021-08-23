package io.github.trapspring.mybatisplus;

/**
 * @ClassNAME MybatisPlusConfig
 * @Description TODO
 * @Author 奶茶
 * @Date 2021/7/30 10:27 上午
 * @Version 1.0
 */

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置分页插件
 */
@Configuration
public class AutoMybatisPlusConfiguration {

    /**
     * 分页插件
     */
    @Bean
    @ConditionalOnMissingBean(PaginationInterceptor.class)
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 字段填充
     */
    @Bean
    @ConditionalOnMissingBean(MetaObjectHandlerConfig.class)
    public MetaObjectHandlerConfig mybatisObjectHandler() {
        return new MetaObjectHandlerConfig();
    }

}
