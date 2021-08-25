package io.github.trapspring.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * @ClassNAME MetaObjectHandlerConfig
 * @Description TODO
 * @Author 奶茶
 * @Date 2021/8/3 4:41 下午
 * @Version 1.0
 */
public class MetaObjectHandlerConfig implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        setFieldValByName("createAt", System.currentTimeMillis(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateAt", System.currentTimeMillis(), metaObject);
    }
}
