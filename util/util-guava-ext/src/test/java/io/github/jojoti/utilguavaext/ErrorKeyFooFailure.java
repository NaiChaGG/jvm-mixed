package io.github.jojoti.utilguavaext;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public enum ErrorKeyFooFailure implements ErrorKey<ErrorKeyFooFailure> {
    ;

    static {
        ErrorKey.duplicatedKeys(ErrorKeyFooFailure.values());
    }

    @Override
    public int getValue() {
        return 0;
    }

    @Override
    public ErrorKeyFooFailure getEnumValue() {
        return null;
    }

}
