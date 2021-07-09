package io.github.jojoti.utilguavaext;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public enum ErrorKeyFoo implements ErrorKey<ErrorKeyFoo> {
    FIRST, SECONDS;

    static {
        ErrorKey.duplicatedKeys(ErrorKeyFoo.values());
    }

    @Override
    public int getValue() {
        return this.ordinal();
    }

    @Override
    public ErrorKeyFoo getEnumValue() {
        return this;
    }

}
