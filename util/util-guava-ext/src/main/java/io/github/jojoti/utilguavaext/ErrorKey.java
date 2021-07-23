package io.github.jojoti.utilguavaext;

/**
 * 返回唯一 Key
 * 1.此 interface 用于定义项目的唯一错误码使用枚举实现该接口
 * 迂回扩展 java enum 类 ...
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public interface ErrorKey<T extends Enum<T>> extends EnumDuplicatedKey<Integer, T> {

    default boolean isOk() {
        return getValue() == 0;
    }

    default boolean isError() {
        return getValue() != 0;
    }

}