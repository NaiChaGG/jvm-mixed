package io.github.jojoti.util.sqlbuilder;

import io.github.jojoti.util.sqlbuilder.joiner.Wheres;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by @JoJo Wang on 2016/8/23.
 *
 * @author JoJo Wang
 *

 */
public class StreamDeleteTest {
    @Test
    public void testForm() throws Exception {

        StreamDelete streamDelete = StreamDelete.from("delete1").where(
                Wheres.equal("test1", 0)
                        .and(Wheres.like("122", "xx"))
        );

        assertEquals("DELETE FROM delete1 WHERE ( test1 = ? ) AND ( 122 LIKE ? )", streamDelete.getPrepareSQL());
    }

    @Test
    public void testJoin() throws Exception {

        StreamDelete streamDelete = StreamDelete.from("delete1").where(
                Wheres.equal("test1", 0)
                        .and(Wheres.like("122", "xx"))
        );

        assertEquals("DELETE FROM delete1 WHERE ( test1 = ? ) AND ( 122 LIKE ? )", streamDelete.getPrepareSQL());
        assertEquals(streamDelete.getValues().length, 2);
    }

    @Test
    public void testGetWhereValues() throws Exception {
        StreamDelete streamDelete = StreamDelete.from("delete1").where(
                Wheres.equal("test1", 0)
                        .and(Wheres.like("122", "xx"))
        );

        assertEquals("DELETE FROM delete1 WHERE ( test1 = ? ) AND ( 122 LIKE ? )", streamDelete.getPrepareSQL());
        assertEquals(streamDelete.getValues().length, 2);
    }
}