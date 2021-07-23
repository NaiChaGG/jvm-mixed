package io.github.jojoti.utilguavaext;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public enum ErrorKeyFooFailure implements ErrorKey<ErrorKeyFooFailure> {
    ;

    static {
        EnumDuplicatedKey.duplicatedKeys(ErrorKeyFooFailure.values());
    }

    @Override
    public Integer getValue() {
        return 0;
    }

    @Override
    public ErrorKeyFooFailure getEnumValue() {
        return null;
    }

}
