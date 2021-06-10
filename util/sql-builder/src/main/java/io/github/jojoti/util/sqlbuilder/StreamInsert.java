/*
 * Copyright 2021 JoJo Wang , homepage: https://github.com/jojoti/experiment-jvm.
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
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

/**
 * Created by @JoJo Wang on 2016/7/31.
 *
 * @author JoJo Wang
 *

 */
public final class StreamInsert implements IQueryInsert {
    protected final List<String> valuesExp = Lists.newArrayList();
    protected final List<Object> values = Lists.newArrayList();
    private final StringBuffer sql = new StringBuffer();

    // 限制new对象
    private StreamInsert(String table) {
        this.sql.append("INSERT INTO ");
        this.sql.append(table);
    }

    public static StreamInsert from(String table, String field, String... fields) {
        StreamInsert streamInsert = new StreamInsert(table);
        streamInsert.sql.append(" (" + field + ", ");
        if (fields.length > 0) {
            streamInsert.sql.append(Joiner.on(", ").join(fields));// -> foo,bar);
        }
        streamInsert.sql.append(")");
        return streamInsert;
    }

    @Override
    public StreamInsert row(Object value, Object... values) {
        this.values.add(value);
        if (values.length > 0) {
            this.values.addAll(Lists.newArrayList(values));
        }

        StringBuffer placeholder = new StringBuffer("(?, ");
        for (int i = 0; i < values.length; i++) {
            placeholder.append("?, ");
        }
        this.valuesExp.add(placeholder.substring(0, placeholder.length() - 2) + ")");
        return this;
    }

    @Override
    public String toString() {
        return this.querySQL();
    }

    @Override
    public String querySQL() {
        this.sql.append(" VALUES ");
        if (this.valuesExp.size() > 1) {
            this.sql.append(Joiner.on(", ").join(this.valuesExp));
        } else {
            this.sql.append(this.valuesExp.get(0));
        }
        return this.sql.toString();
    }

    @Override
    public Iterator<Object> getPrepareIteValues() {
        return null;
    }

    @Override
    public Object[] getPrepareValues() {
        return new Object[0];
    }

    @Override
    public String getPrepareSQL() {
        return null;
    }
}