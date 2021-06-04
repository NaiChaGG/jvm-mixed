package io.github.jojoti.util.sqlbuilder;

import com.google.common.collect.Iterators;
import io.github.jojoti.util.sqlbuilder.joiner.Sets;
import io.github.jojoti.util.sqlbuilder.joiner.Wheres;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by @JoJo Wang on 2016/8/23.
 *
 * @author JoJo Wang
 *

 */
public class StreamUpdateTest {
    @Test
    public void testForm() throws Exception {
        StreamUpdate streamUpdate = StreamUpdate.from("table", Sets.on("filed1", 0))
                .where(Wheres.equal("f1", 0)
                        .and(Wheres.equal("1", 1))
                );

        assertEquals("UPDATE table1 SET filed1 = ? WHERE ( f1 = ? ) AND ( 1 = ? )", streamUpdate.getPrepareSQL());
    }

    @Test
    public void testJoin() throws Exception {
        StreamUpdate streamUpdate = StreamUpdate.from("table1", Sets.on("filed1", 0))
                .where(
                        Wheres.equal("f1", 0)
                                .and(Wheres.equal("1", 1))
                );

        assertEquals("UPDATE table1 SET filed1 = ? WHERE ( f1 = ? ) AND ( 1 = ? )", streamUpdate.getPrepareSQL());
        assertEquals(Iterators.size(streamUpdate.getPrepareIteValues()), 2);
    }

    @Test
    public void testSet() throws Exception {
        StreamUpdate streamUpdate = StreamUpdate.from("table1", Sets.on("filed1", 0)).where(
                Wheres.equal("f1", 0)
                        .and(Wheres.equal("1", 1))
        );

        assertEquals("UPDATE table1 SET filed1 = ? WHERE ( f1 = ? ) AND ( 1 = ? )", streamUpdate.getPrepareSQL());
        assertEquals(Iterators.size(streamUpdate.getPrepareIteValues()), 2);
        assertEquals(Iterators.size(streamUpdate.getSetValues()), 1);
    }

    @Test
    public void testGetSetValues() throws Exception {
        StreamUpdate streamUpdate = StreamUpdate.from("table1", Sets.on("filed1", 0)).where(
                Wheres.equal("f1", 0)
                        .and(Wheres.equal("1", 1))
        );

        assertEquals("UPDATE table1 SET filed1 = ? WHERE ( f1 = ? ) AND ( 1 = ? )", streamUpdate.getPrepareSQL());
        assertEquals(Iterators.size(streamUpdate.getPrepareIteValues()), 2);
        assertEquals(Iterators.size(streamUpdate.getSetValues()), 1);
    }

    @Test
    public void testGetWhereValues() throws Exception {
        StreamUpdate streamUpdate = StreamUpdate.from("table1", Sets.on("filed1", 0)).where(
                Wheres.equal("f1", 0)
                        .and(Wheres.equal("1", 1))
        );

        assertEquals("UPDATE table1 SET filed1 = ? WHERE ( f1 = ? ) AND ( 1 = ? )", streamUpdate.getPrepareSQL());
        assertEquals(Iterators.size(streamUpdate.getPrepareIteValues()), 2);
        assertEquals(Iterators.size(streamUpdate.getSetValues()), 1);
    }
}