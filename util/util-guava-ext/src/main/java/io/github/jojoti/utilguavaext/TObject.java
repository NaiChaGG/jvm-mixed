package io.github.jojoti.utilguavaext;

/**
 * 不安全的 范型推导类
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public final class TObject {

    private final Object t;

    TObject(Object t) {
        this.t = t;
    }

    public static <T> TObject newTObject(T t) {
        return new TObject(t);
    }

    public <T> T getT() {
        return (T) this.t;
    }

}
