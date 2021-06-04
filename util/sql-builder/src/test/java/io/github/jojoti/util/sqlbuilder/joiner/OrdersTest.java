package io.github.jojoti.util.sqlbuilder.joiner;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by @JoJo Wang on 2016/8/2.
 *
 * @author JoJo Wang
 *
 */
public class OrdersTest {

    @Test
    public void testBy() throws Exception {
        assertEquals(Orders.by().sql(), "");
    }

    @Test
    public void testDesc() throws Exception {
        assertEquals(Orders.by().
                        desc("_id1", "_id2", "_id3").
                        asc("_id1", "_id2", "_id3").sql(),
                "ORDER BY _id1, _id2, _id3 DESC,_id1, _id2, _id3 ASC");
    }

    @Test
    public void testAsc() throws Exception {
        assertEquals(Orders.by().asc("_id1", "_id2", "_id3").asc("_id1", "_id2", "_id3").sql(),
                "ORDER BY _id1, _id2, _id3 ASC,_id1, _id2, _id3 ASC");
    }

}