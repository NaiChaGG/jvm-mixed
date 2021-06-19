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

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

/**
 * Created by @JoJo Wang on 2016/7/30.
 *
 * @author JoJo Wang
 */
public final class Wheres implements IWheres {

    // where 语句
    private final String whereExp;
    // where 值列表
    private final List<Object> values;
    // where 子句列表
    private List<WhereConcat> wheres;

    private Wheres(String whereExp, Object[] values) {
        this.whereExp = whereExp;
        this.values = Lists.newArrayList(values);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @param filed
     * @param expression
     * @param query
     * @param values
     * @return
     */
    public static Wheres query(String filed, Arithmetic expression, String query, Object[] values) {
        return new Wheres("( " + filed + " " + expression.getValue() + " (" + query + ")", values);
    }

    public static Wheres where(String filed, Arithmetic expression, Object value) {
        Object[] values = new Object[]{value};
        return query(filed, expression, "?", values);
    }

    public static Wheres where(String filed, Arithmetic expression, Object value, Object[] values) {
        if (values.length > 0) {
            StringBuffer placeholder = new StringBuffer();
            for (int i = 0; i < values.length; i++) {
                placeholder.append("?, ");
            }
            placeholder.append("?");
            // for = (?)
            return query(filed, expression, placeholder.toString(), Lists.newArrayList(value, values).toArray());
        }
        return where(filed, expression, value);
    }

    /**
     * 等于
     *
     * @param field
     * @param value
     * @return
     */
    public static Wheres equal(String field, String value) {
//        value.replace ("%", "/%");
//        value.replace ("_", "/_");
        //like (field, value);
        return where(field, Arithmetic.EQUAL, value);
    }

    public static Wheres equal(String field, long value) {
        return where(field, Arithmetic.EQUAL, value);
    }

    public static Wheres equal(String field, int value) {
        return where(field, Arithmetic.EQUAL, value);
    }

    public static Wheres equal(String field, short value) {
        return where(field, Arithmetic.EQUAL, value);
    }

    public static Wheres equal(String field, byte value) {
        return where(field, Arithmetic.EQUAL, value);
    }

    public static Wheres equal(String field, float value) {
        return where(field, Arithmetic.EQUAL, value);
    }

    public static Wheres equal(String field, double value) {
        return where(field, Arithmetic.EQUAL, value);
    }

    /**
     * 不等于
     *
     * @param field
     * @param value
     * @return
     */
    public static Wheres notEqual(String field, String value) {
        return where(field, Arithmetic.NOT_EQUAL, value);
    }

    public static Wheres notEqual(String field, long value) {
        return where(field, Arithmetic.NOT_EQUAL, value);
    }

    public static Wheres notEqual(String field, int value) {
        return where(field, Arithmetic.NOT_EQUAL, value);
    }

    public static Wheres notEqual(String field, short value) {
        return where(field, Arithmetic.NOT_EQUAL, value);
    }

    public static Wheres notEqual(String field, byte value) {
        return where(field, Arithmetic.NOT_EQUAL, value);
    }

    public static Wheres notEqual(String field, float value) {
        return where(field, Arithmetic.NOT_EQUAL, value);
    }

    public static Wheres notEqual(String field, double value) {
        return where(field, Arithmetic.NOT_EQUAL, value);
    }

    /**
     * 大于
     *
     * @param field
     * @param value
     * @return
     */
    public static Wheres greaterThan(String field, long value) {
        return where(field, Arithmetic.GREATER_THAN, value);
    }

    public static Wheres greaterThan(String field, int value) {
        return where(field, Arithmetic.GREATER_THAN, value);
    }

    public static Wheres greaterThan(String field, short value) {
        return where(field, Arithmetic.GREATER_THAN, value);
    }

    public static Wheres greaterThan(String field, byte value) {
        return where(field, Arithmetic.GREATER_THAN, value);
    }

    public static Wheres greaterThan(String field, float value) {
        return where(field, Arithmetic.GREATER_THAN, value);
    }

    public static Wheres greaterThan(String field, double value) {
        return where(field, Arithmetic.GREATER_THAN, value);
    }

    public static Wheres gt(String field, long value) {
        return greaterThan(field, value);
    }

    public static Wheres gt(String field, int value) {
        return greaterThan(field, value);
    }

    public static Wheres gt(String field, short value) {
        return greaterThan(field, value);
    }

    public static Wheres gt(String field, byte value) {
        return greaterThan(field, value);
    }

    public static Wheres gt(String field, float value) {
        return greaterThan(field, value);
    }

    public static Wheres gt(String field, double value) {
        return greaterThan(field, value);
    }

    /**
     * 小于
     *
     * @param field
     * @param value
     * @return
     */
    public static Wheres lessThan(String field, long value) {
        return where(field, Arithmetic.LESS_THAN, value);
    }

    public static Wheres lessThan(String field, int value) {
        return where(field, Arithmetic.LESS_THAN, value);
    }

    public static Wheres lessThan(String field, short value) {
        return where(field, Arithmetic.LESS_THAN, value);
    }

    public static Wheres lessThan(String field, byte value) {
        return where(field, Arithmetic.LESS_THAN, value);
    }

    public static Wheres lessThan(String field, float value) {
        return where(field, Arithmetic.LESS_THAN, value);
    }

    public static Wheres lessThan(String field, double value) {
        return where(field, Arithmetic.LESS_THAN, value);
    }

    public static Wheres lt(String field, long value) {
        return lessThan(field, value);
    }

    public static Wheres lt(String field, int value) {
        return lessThan(field, value);
    }

    public static Wheres lt(String field, short value) {
        return lessThan(field, value);
    }

    public static Wheres lt(String field, byte value) {
        return lessThan(field, value);
    }

    public static Wheres lt(String field, float value) {
        return lessThan(field, value);
    }

    public static Wheres lt(String field, double value) {
        return lessThan(field, value);
    }

    /**
     * 大于等于
     *
     * @param field
     * @param value
     * @return
     */
    public static Wheres greaterThanOrEqual(String field, long value) {
        return where(field, Arithmetic.GREATER_THAN_OR_EQUAL, value);
    }

    public static Wheres greaterThanOrEqual(String field, int value) {
        return where(field, Arithmetic.GREATER_THAN_OR_EQUAL, value);
    }

    public static Wheres greaterThanOrEqual(String field, short value) {
        return where(field, Arithmetic.GREATER_THAN_OR_EQUAL, value);
    }

    public static Wheres greaterThanOrEqual(String field, byte value) {
        return where(field, Arithmetic.GREATER_THAN_OR_EQUAL, value);
    }

    public static Wheres greaterThanOrEqual(String field, float value) {
        return where(field, Arithmetic.GREATER_THAN_OR_EQUAL, value);
    }

    public static Wheres greaterThanOrEqual(String field, double value) {
        return where(field, Arithmetic.GREATER_THAN_OR_EQUAL, value);
    }

    public static Wheres gte(String field, long value) {
        return greaterThanOrEqual(field, value);
    }

    public static Wheres gte(String field, int value) {
        return greaterThanOrEqual(field, value);
    }

    public static Wheres gte(String field, short value) {
        return greaterThanOrEqual(field, value);
    }

    public static Wheres gte(String field, byte value) {
        return greaterThanOrEqual(field, value);
    }

    public static Wheres gte(String field, float value) {
        return greaterThanOrEqual(field, value);
    }

    public static Wheres gte(String field, double value) {
        return greaterThanOrEqual(field, value);
    }

    /**
     * 小于等于
     *
     * @param field
     * @param value
     * @return
     */
    public static Wheres lessThanOrEqual(String field, long value) {
        return where(field, Arithmetic.LESS_THAN_OR_EQUAL, value);
    }

    public static Wheres lessThanOrEqual(String field, int value) {
        return where(field, Arithmetic.LESS_THAN_OR_EQUAL, value);
    }

    public static Wheres lessThanOrEqual(String field, short value) {
        return where(field, Arithmetic.LESS_THAN_OR_EQUAL, value);
    }

    public static Wheres lessThanOrEqual(String field, byte value) {
        return where(field, Arithmetic.LESS_THAN_OR_EQUAL, value);
    }

    public static Wheres lessThanOrEqual(String field, float value) {
        return where(field, Arithmetic.LESS_THAN_OR_EQUAL, value);
    }

    public static Wheres lessThanOrEqual(String field, double value) {
        return where(field, Arithmetic.LESS_THAN_OR_EQUAL, value);
    }

    public static Wheres lte(String field, long value) {
        return lessThanOrEqual(field, value);
    }

    public static Wheres lte(String field, int value) {
        return lessThanOrEqual(field, value);
    }

    public static Wheres lte(String field, short value) {
        return lessThanOrEqual(field, value);
    }

    public static Wheres lte(String field, byte value) {
        return lessThanOrEqual(field, value);
    }

    public static Wheres lte(String field, float value) {
        return lessThanOrEqual(field, value);
    }

    public static Wheres lte(String field, double value) {
        return lessThanOrEqual(field, value);
    }

    /**
     * 匹配
     *
     * @param filed
     * @param value
     * @return
     */
    public static Wheres like(String filed, String value) {
        return where(filed, Arithmetic.LIKE, value);
    }

    public static Wheres like(String filed, char value) {
        return where(filed, Arithmetic.LIKE, value);
    }

    /**
     * 不匹配
     *
     * @param filed
     * @param value
     * @return
     */
    public static Wheres notLike(String filed, String value) {
        return where(filed, Arithmetic.NOT_LIKE, value);
    }

    public static Wheres notLike(String filed, char value) {
        return where(filed, Arithmetic.NOT_LIKE, value);
    }

    /**
     * 多个值查询
     *
     * @param filed
     * @param value
     * @return
     */
    public static Wheres in(String filed, String value, String... values) {
        // Lists.newArrayList (value)
        return where(filed, Arithmetic.IN, value, values);
    }

    public static Wheres in(String filed, Long value, Long... values) {
        return where(filed, Arithmetic.IN, value, values);
    }

    public static Wheres in(String filed, Integer value, Integer... values) {
        return where(filed, Arithmetic.IN, value, values);
    }

    public static Wheres in(String filed, Short value, Short... values) {
        return where(filed, Arithmetic.IN, value, values);
    }

    public static Wheres in(String filed, Byte value, Byte... values) {
        return where(filed, Arithmetic.IN, value, values);
    }

    public static Wheres in(String filed, Float value, Float... values) {
        return where(filed, Arithmetic.IN, value, values);
    }

    public static Wheres in(String filed, Double value, Double... values) {
        return where(filed, Arithmetic.IN, value, values);
    }

    /**
     * 不在多个值内
     *
     * @param filed
     * @param value
     * @return
     */
    public static Wheres notIn(String filed, String value, String... values) {
        return where(filed, Arithmetic.NOT_IN, value, values);
    }

    public static Wheres notIn(String filed, Long value, Long... values) {
        return where(filed, Arithmetic.NOT_IN, value, values);
    }

    public static Wheres notIn(String filed, Integer value, Integer... values) {
        return where(filed, Arithmetic.NOT_IN, value, values);
    }

    public static Wheres notIn(String filed, Short value, Short... values) {
        return where(filed, Arithmetic.NOT_IN, value, values);
    }

    public static Wheres notIn(String filed, Byte value, Byte... values) {
        return where(filed, Arithmetic.NOT_IN, value, values);
    }

    public static Wheres notIn(String filed, Float value, Float... values) {
        return where(filed, Arithmetic.NOT_IN, value, values);
    }

    public static Wheres notIn(String filed, Double value, Double... values) {
        return where(filed, Arithmetic.NOT_IN, value, values);
    }

    public Wheres and(IWheres wheres) {
        WhereConcat whereConcat = new WhereConcat();
        whereConcat.exp = " AND ";
        whereConcat.wheres = wheres;
        this.wheres.add(whereConcat);
        return this;
    }

    public Wheres or(IWheres wheres) {
        WhereConcat whereConcat = new WhereConcat();
        whereConcat.exp = " OR ";
        whereConcat.wheres = wheres;
        this.wheres.add(whereConcat);
        return this;
    }

    /**
     * 获取where语句
     *
     * @return
     */
    public String getWheres() {
        return "";
    }

    /**
     * 获取where sql
     *
     * @return
     */
    public String sql() {
        return "WHERE " + getWheres();
    }

    @Override
    public String whereSQL() {
        return sql();
    }

    @Override
    public String toString() {
        return this.sql();
    }

    /**
     * 获取where values 迭代
     *
     * @return
     */
    public Iterator<Object> getWhereValues() {
        return values.iterator();
    }

    public Object[] getValues() {
        return values.toArray();
    }

    @Override
    public String querySQL() {
        return null;
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

    private class WhereConcat {
        private String exp;
        private IWheres wheres;
    }
}
