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
import io.github.jojoti.util.sqlbuilder.joiner.Sets;
import io.github.jojoti.util.sqlbuilder.joiner.Wheres;

import java.util.Iterator;

/**
 * Created by @JoJo Wang on 2016/7/31.
 *
 * @author JoJo Wang
 *

 */
public class StreamUpdate extends StreamBase implements IQueryUpdate {
    private final StringBuffer sql = new StringBuffer();

    protected Iterator<Object> setValues;
    protected Iterator<Object> whereValues;

    // 限制new对象
    private StreamUpdate(String table) {
        this.sql.append("UPDATE ");
        this.sql.append(table);
    }

    public static StreamUpdate from(String table, Sets sets) {
        StreamUpdate streamUpdate = new StreamUpdate(table);
        streamUpdate.sql.append(" ");
        streamUpdate.sql.append(sets.getSetSQL());
        streamUpdate.setValues = sets.getSetsValues();
        return streamUpdate;
    }

    public static StreamUpdate from(String table, Sets sets, Wheres wheres) {
        StreamUpdate streamUpdate = from(table, sets);
        streamUpdate.join(wheres);
        return streamUpdate;
    }

    public StreamUpdate join(Wheres wheres) {
        this.sql.append(" ");
        this.sql.append(wheres.sql());
        this.whereValues = wheres.getWhereValues();
        return this;
    }

    @Override
    public StreamUpdate where(IWheres wheres) {
        this.wheres = wheres;
        return this;
    }

    @Override
    public StreamUpdate orWhere(IWheres wheres) {
        this.wheres.or(wheres);
        return this;
    }

    @Override
    public StreamUpdate andWhere(IWheres wheres) {
        this.wheres.and(wheres);
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
    public Iterator<Object> getSetValues() {
        return null;
    }

}