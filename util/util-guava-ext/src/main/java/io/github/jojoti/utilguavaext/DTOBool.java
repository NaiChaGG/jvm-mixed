package io.github.jojoti.utilguavaext;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public class DTOBool<T> {

    private final boolean success;
    private final T data;

    private DTOBool(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public static <T> DTOBool<T> ok(T t) {
        return new DTOBool<>(true, t);
    }

    public static <T> DTOBool<T> failed(T t) {
        return new DTOBool<>(false, t);
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