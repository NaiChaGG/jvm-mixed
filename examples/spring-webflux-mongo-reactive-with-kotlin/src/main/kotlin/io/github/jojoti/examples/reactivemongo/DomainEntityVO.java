package io.github.jojoti.examples.reactivemongo;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public class DomainEntityVO<T> {

    private final int code;
    private final T data;

    private DomainEntityVO(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public static <T> DomainEntityVO<T> ok(int code, T data) {
        return new DomainEntityVO<>(code, data);
    }

    public static <T> DomainEntityVO<T> ok() {
        return new DomainEntityVO<>(0, null);
    }

    public static <T> DomainEntityVO<T> ok(T data) {
        return new DomainEntityVO<>(0, data);
    }

    public static <T> DomainEntityVO<T> error(int code) {
        return new DomainEntityVO<>(code, null);
    }

    public static <T> DomainEntityVO<T> error(int code, T data) {
        return new DomainEntityVO<>(code, data);
    }

    public boolean isOk() {
        return code == 0;
    }

    public boolean isSuccess() {
        return code == 0;
    }

    public boolean isFailed() {
        return !isOk();
    }

    public T get() {
        return getData();
    }

    public T getData() {
        return data;
    }

    public int code() {
        return errorCode();
    }

    public int errorCode() {
        return code;
    }

    public <T_NEW> DomainEntityVO<T_NEW> errorConvertNewType() {
        return (DomainEntityVO<T_NEW>) this;
    }

    @Override
    public String toString() {
        return "DomainEntityVO{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }
}