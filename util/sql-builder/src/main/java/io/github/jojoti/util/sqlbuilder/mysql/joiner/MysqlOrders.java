/*
 * Copyright 2021 JoJo Wang , homepage: https://github.com/jojoti.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
