package io.github.jojoti.utilguavaext;

/**
 * 返回唯一 Key
 * 1.此 interface 用于定义项目的唯一错误码使用枚举实现该接口
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public interface ErrorKey extends UniqueKey<Integer> {

    /**
     * 唯一的错误码
     *
     * @return
     */
    int getUniqueKey();

    @Override
    default Integer get() {
        return getUniqueKey();
    }

    default boolean isOk() {
        return getUniqueKey() == 0;
    }

    default boolean isError() {
        return getUniqueKey() != 0;
    }

}