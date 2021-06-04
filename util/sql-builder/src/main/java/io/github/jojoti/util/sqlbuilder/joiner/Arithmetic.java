package io.github.jojoti.util.sqlbuilder.joiner;

/**
 * Created by @JoJo Wang on 2017/4/10.
 */
public enum Arithmetic {
    LIKE("LIKE"), NOT_LIKE("NOT LIKE"),
    EQUAL("="), NOT_EQUAL("<>"),
    GREATER_THAN(">"), LESS_THAN("<"),
    GREATER_THAN_OR_EQUAL(">="), LESS_THAN_OR_EQUAL("<="),
    IN("IN"), NOT_IN("NOT IN"),
    ;

    private String value;

    Arithmetic(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
