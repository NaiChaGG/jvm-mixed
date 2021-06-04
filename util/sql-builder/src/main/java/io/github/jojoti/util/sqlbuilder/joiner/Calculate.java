package io.github.jojoti.util.sqlbuilder.joiner;

/**
 * Created by @JoJo Wang on 2016/8/11.
 *
 * @author JoJo Wang
 */
@Deprecated
public final class Calculate {

    private String exp;

    private Calculate() {

    }

    public static Calculate plus(String field1, String field2) {

        // a1 + a2
        return new Calculate();
    }

    private enum Operator {

        PLUS("+"),
        MINUS("-"),
        MULTIPLE("*"),
        DIVIDE("/"),

        //
        ;

        private String value;

        Operator(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
