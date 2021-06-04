package io.github.jojoti.util.sqlbuilder.joiner;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by @JoJo Wang on 2016/7/30.
 *
 * @author JoJo Wang
 *

 */
public class Orders {

    // 排序字段
    protected final List<String> orderFields = Lists.newArrayList();

    protected Orders() {
    }

    /**
     * 排序实例
     *
     * @return
     */
    public static Orders by() {
        return new Orders();
    }

    /**
     * 倒叙
     *
     * @param fields
     * @return
     */
    public Orders desc(String... fields) {
        return DESC(fields);
    }

    /**
     * 倒叙
     *
     * @param fields
     * @return
     */
    public Orders DESC(String... fields) {
        return _sort(Sort.DESC, fields);
    }

    /**
     * 正序
     *
     * @param fields
     * @return
     */
    public Orders asc(String... fields) {
        return ASC(fields);
    }

    /**
     * 正序
     *
     * @param fields
     * @return
     */
    public Orders ASC(String... fields) {
        return _sort(Sort.ASC, fields);
    }


    /**
     * 排序sql
     *
     * @return
     */
    public String sql() {
        return orderFields.size() <= 0 ? "" : "ORDER BY " + Joiner.on(',').join(orderFields);
    }

    /**
     * 添加排序字段
     *
     * @param sort
     * @param fields
     * @return
     */
    private Orders _sort(Sort sort, String... fields) {
        orderFields.add(Joiner.on(", ").join(fields) + " " + sort);
        return this;
    }

    /**
     * 排序枚举
     */
    private enum Sort {
        DESC("DESC"), ASC("ASC"), MIXED("MIXED");

        private String value;

        Sort(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
