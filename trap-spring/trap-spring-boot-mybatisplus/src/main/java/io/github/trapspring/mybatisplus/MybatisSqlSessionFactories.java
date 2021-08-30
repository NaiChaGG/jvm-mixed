package io.github.trapspring.mybatisplus;

import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * @ClassNAME MybatisConfigTest
 * @Description TODO
 * @Author 奶茶
 * @Date 2021/8/27 8:20 下午
 * @Version 1.0
 */
public class MybatisSqlSessionFactories {

    private List<SqlSessionFactory> sqlSessionFactories;

    public MybatisSqlSessionFactories(List<SqlSessionFactory> sqlSessionFactories) {
        this.sqlSessionFactories = sqlSessionFactories;
    }


}
