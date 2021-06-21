package io.github.jojoti.utilguavaext;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public class DTOBoolean<T> {

    private final boolean success;
    private final T data;

    private DTOBoolean(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public static <T> DTOBoolean<T> ok(T t) {
        return new DTOBoolean<>(true, t);
    }

    public static <T> DTOBoolean<T> failed(T t) {
        return new DTOBoolean<>(false, t);
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isFailed() {
        return !isSuccess();
    }

    public T getData() {
        return data;
    }

}