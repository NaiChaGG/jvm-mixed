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

package io.github.jojoti.util.sqlbuilder;

import io.github.jojoti.util.sqlbuilder.joiner.IWheres;
import io.github.jojoti.util.sqlbuilder.joiner.Wheres;

/**
 * Created by @JoJo Wang on 2016/7/31.
 *
 * @author JoJo Wang
 *

 */
public class StreamDelete extends StreamBase implements IQueryDelete {

    private final String sql;

    public StreamDelete(String table) {
        this.sql = "DELETE FROM " + table;
    }

    /**
     * @param table
     * @return
     */
    public static StreamDelete from(String table) {
        return new StreamDelete(table);
    }

    /**
     * @param table
     * @return
     */
    public static StreamDelete from(String table, Wheres wheres) {
        StreamDelete streamDelete = from(table);
        streamDelete.where(wheres);
        return streamDelete;
    }

    @Override
    public String querySQL() {
        if (this.wheres != null) {
            return this.sql + " " + this.wheres.querySQL();
        }
        return this.sql.toString();
    }

    @Override
    public String getPrepareSQL() {
        if (this.wheres != null) {
            return this.sql + " " + this.wheres.getPrepareSQL();
        }
        return this.sql.toString();
    }

    @Override
    public StreamDelete where(IWheres wheres) {
        this.wheres = wheres;
        return this;
    }

    @Override
    public StreamDelete orWhere(IWheres wheres) {
        this.wheres.or(wheres);
        return this;
    }

    @Override
    public StreamDelete andWhere(IWheres wheres) {
        this.wheres.and(wheres);
        return this;
    }

}