package io.github.jojoti.utilguavaext;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public enum ErrorKeyFoo implements ErrorKey<ErrorKeyFoo> {
    FIRST, SECONDS;

    static {
        EnumDuplicatedKey.duplicatedKeys(ErrorKeyFoo.values());
    }

    @Override
    public Integer getValue() {
        return this.ordinal();
    }

    @Override
    public ErrorKeyFoo getEnumValue() {
        return this;
    }

}
