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

package io.github.jojoti.util.sqlbuilder.joiner;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by @JoJo Wang on 2016/7/30.
 *
 * @author JoJo Wang
 */
public final class Groups {

    // 保存分组字段
    private final String[] fields;
    // 聚合函数list
    private final List<String> aggregateList = Lists.newArrayList();
    // 分组条件
    private Wheres havingWheres;

    /**
     * 分组字段
     *
     * @param fields
     */
    private Groups(String[] fields) {
        this.fields = fields;
    }

    /**
     * 分组字段
     *
     * @param fields
     * @return
     */
    public static Groups by(String field, String... fields) {
        String[] values;
        if (fields.length > 0) {
            values = new String[fields.length + 1];
            values[0] = field;
            for (int i = 0; i < fields.length; i++) {
                values[i + 1] = fields[i];
            }
        } else {
            values = new String[]{field};
        }
        return new Groups(values);
    }

    /**
     * 求和
     *
     * @param field
     * @return
     */
    public Groups SUM(String field) {
        this._aggregate(field, Function.SUM);
        return this;
    }

    /**
     * 求和
     *
     * @param field
     * @return
     */
    public Groups sum(String field) {
        return SUM(field);
    }

    /**
     * 计数
     *
     * @param field
     * @return
     */
    public Groups COUNT(String field) {
        _aggregate(field, Function.COUNT);
        return this;
    }

    /**
     * 计数
     *
     * @param field
     * @return
     */
    public Groups count(String field) {
        return COUNT(field);
    }

    /**
     * 平均数
     *
     * @param field
     * @return
     */
    public Groups AVG(String field) {
        _aggregate(field, Function.AVG);
        return this;
    }

    /**
     * 平均数
     *
     * @param field
     * @return
     */
    public Groups avg(String field) {
        return AVG(field);
    }

    /**
     * 最大值
     *
     * @param field
     * @return
     */
    public Groups MAX(String field) {
        _aggregate(field, Function.MAX);
        return this;
    }

    /**
     * 最大值
     *
     * @param field
     * @return
     */
    public Groups max(String field) {
        return MAX(field);
    }

    /**
     * 获取追加的函数sql
     *
     * @return
     */
    public String getAppendFunctionSQL() {
        return _generatorFields() +
                (aggregateList.size() > 0 ? ", " + Joiner.on(',').join(aggregateList) : "");
    }

    /**
     * 获取分组sql
     *
     * @return
     */
    public String sql() {
        if (this.havingWheres != null) {
            return "GROUP BY " + _generatorFields() + " HAVING " + this.havingWheres.getWheres();
        }
        return "GROUP BY " + _generatorFields();
    }

    /**
     * 筛选
     *
     * @param wheres
     * @return
     */
    public Groups having(Wheres wheres) {
        this.havingWheres = wheres;
        return this;
    }

    /**
     * 生成表达式sql
     *
     * @return
     */
    private String _generatorFields() {
        return Joiner.on(", ").join(fields);
    }

    /**
     * 生成函数表达式
     *
     * @param field
     * @param function
     */
    private void _aggregate(String field, Function function) {
        this.aggregateList.add(function.get_value() + "(" + field + ") AS " + function.get_asName());
    }

    /**
     * 函数枚举值
     */
    private enum Function {
        SUM("SUM", "__sum"),
        MAX("MAX", "__max"),
        AVG("AVG", "__avg"),
        COUNT("COUNT", "__count");

        private String _value;
        private String _asName;

        Function(String value, String asName) {
            this._value = value;
            this._asName = asName;
        }

        public String get_value() {
            return _value;
        }

        public String get_asName() {
            return _asName;
        }
    }

}
