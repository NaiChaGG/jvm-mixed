package io.github.jojoti.util.sqlbuilder;

import com.google.common.collect.Iterators;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by @JoJo Wang on 2016/8/23.
 *
 * @author JoJo Wang
 *

 */
public class StreamInsertTest {
    @Test
    public void testForm() throws Exception {
        StreamInsert streamInsert = StreamInsert.from("runable", "test", "test1")
                .row("fdsfsdfsdf", 1)
                .row("xxxxx", 1);

        assertEquals("INSERT INTO runable (id, name) VALUES (?, ?), (?, ?)", streamInsert.getPrepareSQL());

    }

    @Test
    public void testFields() throws Exception {
        StreamInsert streamInsert = StreamInsert.from("runable", "id", "name")
                .row("fdsfsdfsdf", 1)
                .row("xxxxx", 1);

        assertEquals("INSERT INTO runable (id, name) VALUES (?, ?), (?, ?)", streamInsert.getPrepareSQL());
    }

    @Test
    public void testValues() throws Exception {
        StreamInsert streamInsert = StreamInsert.from("runable", "id", "name")
                .row("fdsfsdfsdf", 1)
                .row("xxxxx", 1);

        assertEquals("INSERT INTO runable (id, name) VALUES (?, ?), (?, ?)", streamInsert.getPrepareSQL());
        assertEquals(streamInsert.getPrepareValues(), 4);
    }

    @Test
    public void testGetValues() throws Exception {
        StreamInsert streamInsert = StreamInsert.from("runable", "id", "name")
                .row("fdsfsdfsdf", 1)
                .row("xxxxx", 1);

        assertEquals("INSERT INTO runable (id, name) VALUES (?, ?), (?, ?)", streamInsert.getPrepareSQL());
        assertEquals(Iterators.size(streamInsert.getPrepareIteValues()), 4);
    }
}