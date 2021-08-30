package io.github.trapspring.mybatisplus;

/**
 * @ClassNAME MybatisPlusConfig
 * @Description TODO
 * @Author 奶茶
 * @Date 2021/7/30 10:27 上午
 * @Version 1.0
 */

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 配置分页插件
 */
@Configuration
public class AutoMybatisPlusConfiguration{

    private static final Logger logger = LoggerFactory.getLogger(AutoMybatisPlusConfiguration.class);

    @Bean
    @ConditionalOnMissingBean(MybatisSqlSessionFactories.class)
    public MybatisSqlSessionFactories initSqlSessionFactoryMetaData(List<SqlSessionFactory> sqlSessionFactories){
        sqlSessionFactories.forEach(sqlSessionFactory -> {
            org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.openSession().getConfiguration();
            MybatisConfiguration mybatisConfiguration = (MybatisConfiguration)configuration;
            mybatisConfiguration.getGlobalConfig().setMetaObjectHandler(new MetaObjectHandler() {
                @Override
                public void insertFill(MetaObject metaObject) {
                    setFieldValByName("createAt", System.currentTimeMillis(), metaObject);
                }

                @Override
                public void updateFill(MetaObject metaObject) {
                    this.setFieldValByName("updateAt", System.currentTimeMillis(), metaObject);
                }
            });
            mybatisConfiguration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class);
            mybatisConfiguration.setMapUnderscoreToCamelCase(true);
            mybatisConfiguration.setAutoMappingBehavior(AutoMappingBehavior.FULL);
            mybatisConfiguration.addInterceptor(new PaginationInterceptor());
        });
        return new MybatisSqlSessionFactories(sqlSessionFactories);
    }

}
