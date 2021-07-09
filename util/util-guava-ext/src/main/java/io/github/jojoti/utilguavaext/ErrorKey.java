package io.github.jojoti.utilguavaext;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 返回唯一 Key
 * 1.此 interface 用于定义项目的唯一错误码使用枚举实现该接口
 * 迂回扩展 java enum 类 ...
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public interface ErrorKey<T extends Enum<T>> {

    default boolean isOk() {
        return getValue() == 0;
    }

    default boolean isError() {
        return getValue() != 0;
    }

    int getValue();

    T getEnumValue();

    /**
     * 校验 枚举的值是否是重复的
     *
     * @param val
     * @param <T>
     */
    static <T extends Enum<T>> void duplicatedKeys(ErrorKey<T>[] val) {
        Set<Integer> unique = Sets.newHashSet();
        for (ErrorKey<T> tUniqueKey : val) {
            // 必须 return this
            Preconditions.checkNotNull(tUniqueKey.getEnumValue());
            if (!unique.add(tUniqueKey.getValue())) {
                throw new UnsupportedOperationException("Class " + tUniqueKey.getEnumValue().getClass() + " value " + tUniqueKey.getValue() + " repeat.");
            }
        }
    }

}