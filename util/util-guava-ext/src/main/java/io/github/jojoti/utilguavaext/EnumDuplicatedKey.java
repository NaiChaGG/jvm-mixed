package io.github.jojoti.utilguavaext;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 扩展了 枚举的行为 可以快速支持 唯一 value 的枚举
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public interface EnumDuplicatedKey<T extends Enum<T>> {

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
            Preconditions.checkArgument(tUniqueKey.getEnumValue().equals(tUniqueKey));
            if (!unique.add(tUniqueKey.getValue())) {
                throw new UnsupportedOperationException("Class " + tUniqueKey.getEnumValue().getClass() + " value " + tUniqueKey.getValue() + " repeat.");
            }
        }
    }

    int getValue();

    T getEnumValue();

}
