/*
 * Copyright 2021 JoJo Wang , homepage: https://github.com/jojoti/jvm-mixed.
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

import com.google.common.base.Joiner;
import io.github.jojoti.util.sqlbuilder.joiner.*;

/**
 * Created by @JoJo Wang on 2017/4/10.
 */
public class StreamSelect extends StreamBase implements IQuerySelect {
    private final StringBuffer from = new StringBuffer();
    private final StringBuffer fields = new StringBuffer();

    private Orders orders;
    private Groups groups;

    public StreamSelect(String table) {
        this.from.append("FROM ");
        this.from.append(table);
    }

    public static StreamSelect from(String table) {
        return new StreamSelect(table);
    }

    public static StreamSelect from(String table, String field, String... fields) {
        StreamSelect select = from(table);
        select.fields.append(field);
        if (fields.length > 0) {
            select.fields.append(", ");
            select.fields.append(Joiner.on(", ").join(fields));// -> foo,bar);
        }
        return select;
    }

    public StreamSelect order(Orders orders) {
        this.orders = orders;
        return this;
    }

    public StreamSelect group(Groups groups) {
        this.groups = groups;
        return this;
    }

    @Override
    public StreamSelect subQuery(String field, Arithmetic arithmetic, IQuerySelect querySelect) {
        this.where(Wheres.query(field, arithmetic, querySelect.querySQL(), querySelect.getValues()));
        return this;
    }

    @Override
    public StreamSelect orSubQuery(String field, Arithmetic arithmetic, IQuerySelect querySelect) {
        this.orWhere(Wheres.query(field, arithmetic, querySelect.querySQL(), querySelect.getValues()));
        return this;
    }

    @Override
    public StreamSelect andSubQuery(String field, Arithmetic arithmetic, IQuerySelect querySelect) {
        this.andWhere(Wheres.query(field, arithmetic, querySelect.querySQL(), querySelect.getValues()));
        return this;
    }

    @Override
    public String querySQL() {
        return null;
    }

    @Override
    public String getPrepareSQL() {
        return null;
    }

    @Override
    public StreamSelect where(IWheres wheres) {
        this.wheres = wheres;
        return this;
    }

    @Override
    public StreamSelect orWhere(IWheres wheres) {
        this.wheres.or(wheres);
        return this;
    }

    @Override
    public StreamSelect andWhere(IWheres wheres) {
        this.wheres.and(wheres);
        return this;
    }

    @Override
    public String getCountSql() {
        return null;
    }

    public StreamSelect limit(int limit) {
        return this;
    }

    public StreamSelect offset(int offset) {
        return this;
    }

    @Override
    public int getLimit() {
        return 0;
    }

    @Override
    public long getOffset() {
        return 0;
    }
}
