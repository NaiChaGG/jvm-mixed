package io.github.jojoti.util.sqlbuilder.mysql.joiner;

import com.google.common.base.Joiner;
import io.github.jojoti.util.sqlbuilder.joiner.Orders;

/**
 * Created by @JoJo Wang on 2017/4/10.
 */
public class MysqlOrders extends Orders {

    /**
     * 自定义顺序
     *
     * @param fields
     * @return
     */
    public Orders mixed(String... fields) {
        return MIXED(fields);
    }

    /**
     * 自定义顺序
     *
     * @param fields
     * @return
     */
    public Orders MIXED(String... fields) {
        // TODO 这里目前只处理mysql,且只是id自定义顺序
        orderFields.add("FIELD " + Joiner.on(", ").join(fields));
        return this;
    }
}
