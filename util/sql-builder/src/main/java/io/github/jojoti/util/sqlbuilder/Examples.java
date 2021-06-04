package io.github.jojoti.util.sqlbuilder;

import io.github.jojoti.util.sqlbuilder.joiner.Arithmetic;
import io.github.jojoti.util.sqlbuilder.joiner.Wheres;

/**
 * Created by @JoJo Wang on 2017/4/10.
 */
public class Examples {

    public static void main(String[] args) {
        StreamSelect streamSelect = StreamSelect.from("xx", "id")
                .where(null);

        // select * from table where a > 0 and b > (select a form b where a > 0)
        // 用这个还不如直接写 sql
        StreamSelect.from("table")
                .where(Wheres.greaterThan("a", 0))
                .orSubQuery("b", Arithmetic.GREATER_THAN,
                        StreamSelect.from("b", "a")
                                .where(Wheres.greaterThan("a", 0))
                );

    }

}
