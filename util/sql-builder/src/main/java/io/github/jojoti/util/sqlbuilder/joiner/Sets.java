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

package io.github.jojoti.util.sqlbuilder.joiner;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

/**
 * Created by @JoJo Wang on 2016/8/25.
 *
 * @author JoJo Wang
 */
public final class Sets {

    // sql buffer
    private final StringBuffer sqlBuffer = new StringBuffer();

    // set 值列表
    private final List<Object> setValues = Lists.newArrayList();

    /**
     * 填充字段
     *
     * @param field
     * @param value
     * @return
     */
    public static Sets on(String field, String value) {
        return _on(field, value);
    }

    /**
     * 填充字段
     *
     * @param field
     * @param value
     * @return
     */
    public static Sets on(String field, Long value) {
        return _on(field, value);
    }

    /**
     * 填充字段
     *
     * @param field
     * @param value
     * @return
     */
    public static Sets on(String field, Integer value) {
        return _on(field, value);
    }

    /**
     * 填充字段
     *
     * @param field
     * @param value
     * @return
     */
    public static Sets on(String field, Short value) {
        return _on(field, value);
    }

    /**
     * 填充字段
     *
     * @param field
     * @param value
     * @return
     */
    public static Sets on(String field, Byte value) {
        return _on(field, value);
    }

    /**
     * 填充字段
     *
     * @param field
     * @param value
     * @return
     */
    public static Sets on(String field, Double value) {
        return _on(field, value);
    }

    /**
     * 填充字段
     *
     * @param field
     * @param value
     * @return
     */
    public static Sets on(String field, Float value) {
        return _on(field, value);
    }

    /**
     * 填充字段
     *
     * @param field
     * @param value
     * @return
     */
    private static Sets _on(String field, Object value) {
        Sets sets = new Sets();
        sets._set(field, value);
        return sets;
    }

    /**
     * 填充字段
     *
     * @param field
     * @param value
     * @return
     */
    public Sets set(String field, String value) {
        this._set(field, value);
        return this;
    }

    /**
     * 填充字段
     *
     * @param field
     * @param value
     * @return
     */
    public Sets set(String field, Long value) {
        this._set(field, value);
        return this;
    }

    /**
     * 填充字段
     *
     * @param field
     * @param value
     * @return
     */
    public Sets set(String field, Integer value) {
        this._set(field, value);
        return this;
    }

    /**
     * 填充字段
     *
     * @param field
     * @param value
     * @return
     */
    public Sets set(String field, Short value) {
        this._set(field, value);
        return this;
    }

    /**
     * 填充字段
     *
     * @param field
     * @param value
     * @return
     */
    public Sets set(String field, Byte value) {
        this._set(field, value);
        return this;
    }

    /**
     * 填充字段
     *
     * @param field
     * @param value
     * @return
     */
    public Sets set(String field, Double value) {
        this._set(field, value);
        return this;
    }

    /**
     * 填充字段
     *
     * @param field
     * @param value
     * @return
     */
    public Sets set(String field, Float value) {
        this._set(field, value);
        return this;
    }

    /**
     * 获取set sql
     *
     * @return
     */
    public String getSetSQL() {
        return "SET " + sqlBuffer.substring(0, sqlBuffer.length() - 2);
    }

    /**
     * 获取set的值 迭代
     *
     * @return
     */
    public Iterator<Object> getSetsValues() {
        return this.setValues.iterator();
    }

    /**
     * 填充字段
     *
     * @param field
     * @param value
     * @return
     */
    private void _set(String field, Object value) {
        this.sqlBuffer.append(field + " = ?, ");
        this.setValues.add(value);
    }
}
