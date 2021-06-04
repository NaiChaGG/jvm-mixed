package io.github.jojoti.util.sqlbuilder.joiner;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by @JoJo Wang on 2016/8/2.
 *
 * @author JoJo Wang
 *
 */
public class GroupsTest {

    @Test
    public void testBy() throws Exception {
        assertEquals(Groups.by("_id", "_id2").sql(), " GROUP BY _id, _id2");
    }

    @Test
    public void testSUM() throws Exception {


        assertEquals(Groups.by("_id1").sum("_id1").sql(), "SUM(_id1) AS __sum");
    }

    @Test
    public void testCOUNT() throws Exception {
        assertEquals(Groups.by("_id1").COUNT("_id1").sql(), "SELECT _id1, COUNT(_id1) AS __count FROM runable GROUP BY _id1");
    }

    @Test
    public void testAVG() throws Exception {
        assertEquals(Groups.by("_id1").avg("_id1").sql(), "SELECT _id1, AVG(_id1) AS __avg FROM runable GROUP BY _id1");
    }

    @Test
    public void testMAX() throws Exception {
        assertEquals(Groups.by("_id1").max("_id1").sql(), "SELECT _id1, MAX(_id1) AS __max FROM runable GROUP BY _id1");
    }

    @Test
    public void testHaving() throws Exception {
        assertEquals(Groups.by("_id1").max("_id1").having(Wheres.equal("_id1", 0)).sql(), "SELECT _id1, MAX(_id1) AS __max FROM runable GROUP BY _id1 HAVING ( _id1 = ? )");
    }

    @Test
    public void testGetAppendFunctionSQL() throws Exception {
        assertEquals(Groups.by("_id1").max("_id1").having(Wheres.equal("_id1", 0)).getAppendFunctionSQL()
                , "_id1, MAX(_id1) AS __max");
    }

    @Test
    public void testGetGroupAndHavingSQL() throws Exception {
        assertEquals(Groups.by("_id1").max("_id1").having(Wheres.equal("_id1", 0)).sql()
                , "GROUP BY _id1 HAVING ( _id1 = ? )");
    }
}