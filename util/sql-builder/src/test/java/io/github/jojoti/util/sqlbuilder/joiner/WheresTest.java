package io.github.jojoti.util.sqlbuilder.joiner;

import com.google.common.collect.Iterators;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Created by @JoJo Wang on 2016/8/2.
 *
 * @author JoJo Wang
 *
 */
public class WheresTest {

    @Test
    public void testEqual() throws Exception {
        Wheres wheres = Wheres.equal("_id1", 0);

        assertEquals(wheres.sql(), "WHERE ( _id1 = ? )");

        assertEquals(Iterators.size(wheres.getWhereValues()), 1);

        assertEquals(wheres.getWheres(), "( _id1 = ? )");
    }

    @Test
    public void testNotEqual() throws Exception {
        Wheres wheres = Wheres.notLike("_id1", "1");

        assertEquals(wheres.sql(), "WHERE ( _id1 NOT LIKE ? )");

        assertEquals(Iterators.size(wheres.getWhereValues()), 1);

        assertEquals(wheres.getWheres(), "( _id1 NOT LIKE ? )");
    }

    @Test
    public void testGreaterThan() throws Exception {
        Wheres wheres = Wheres.greaterThan("_id1", 1);

        assertEquals(wheres.sql(), "WHERE ( _id1 > ? )");

        assertEquals(Iterators.size(wheres.getWhereValues()), 1);

        assertEquals(wheres.getWheres(), "( _id1 > ? )");
    }

    @Test
    public void testLessThan1() throws Exception {
        Wheres wheres = Wheres.lessThan("_id1", 1);

        assertEquals(wheres.sql(), "WHERE ( _id1 < ? )");

        assertEquals(Iterators.size(wheres.getWhereValues()), 1);

        assertEquals(wheres.getWheres(), "( _id1 < ? )");
    }

    @Test
    public void testGreaterThanOrEqual() throws Exception {
        Wheres wheres = Wheres.greaterThanOrEqual("_id1", 1);

        assertEquals(wheres.sql(), "WHERE ( _id1 >= ? )");

        assertEquals(Iterators.size(wheres.getWhereValues()), 1);

        assertEquals(wheres.getWheres(), "( _id1 >= ? )");
    }

    @Test
    public void testLessThanOrEqual() throws Exception {
        Wheres wheres = Wheres.lessThanOrEqual("_id1", 1);

        assertEquals(wheres.sql(), "WHERE ( _id1 <= ? )");

        assertEquals(Iterators.size(wheres.getWhereValues()), 1);

        assertEquals(wheres.getWheres(), "( _id1 <= ? )");
    }

    @Test
    public void testLike() throws Exception {
        Wheres wheres = Wheres.like("_id1", "x");

        assertEquals(wheres.sql(), "WHERE ( _id1 LIKE ? )");

        assertEquals(Iterators.size(wheres.getWhereValues()), 1);

        assertEquals(wheres.getWheres(), "( _id1 LIKE ? )");
    }


    @Test
    public void testNotLike() throws Exception {
        Wheres wheres = Wheres.notLike("_id1", "x");

        assertEquals(wheres.sql(), "WHERE ( _id1 NOT LIKE ? )");

        assertEquals(Iterators.size(wheres.getWhereValues()), 1);

        assertEquals(wheres.getWheres(), "( _id1 NOT LIKE ? )");
    }

    @Test
    public void testIn() throws Exception {
        Wheres wheres = Wheres.in("_id1", "x", "1", "x");

        assertEquals(wheres.sql(), "WHERE ( _id1 IN ( ?, ?, ? ) )");

        assertEquals(Iterators.size(wheres.getWhereValues()), 3);

        assertEquals(wheres.getWheres(), "( _id1 IN ( ?, ?, ? ) )");
    }

    @Test
    public void testNotIn() throws Exception {
        Wheres wheres = Wheres.notIn("_id1", "x", "1", "x");

        assertEquals(wheres.sql(), "WHERE ( _id1 NOT IN ( ?, ?, ? ) )");

        assertEquals(Iterators.size(wheres.getWhereValues()), 3);

        assertEquals(wheres.getWheres(), "( _id1 NOT IN ( ?, ?, ? ) )");
    }

    @Test
    public void testAnd() throws Exception {
        Wheres wheres = Wheres.notIn("_id1", "x", "1", "x").and(Wheres.equal("and1", 0));

        assertEquals(wheres.sql(), "WHERE ( _id1 NOT IN ( ?, ?, ? ) ) AND ( and1 = ? )");

        assertEquals(Iterators.size(wheres.getWhereValues()), 4);

        assertEquals(wheres.getWheres(), "( _id1 NOT IN ( ?, ?, ? ) ) AND ( and1 = ? )");
    }

    @Test
    public void testOr() throws Exception {
        Wheres wheres = Wheres.notIn("_id1", "x", "1", "x").and(Wheres.equal("and1", 0)).or(Wheres.equal("or1", 0));

        assertEquals(wheres.sql(), "WHERE ( _id1 NOT IN ( ?, ?, ? ) ) AND ( and1 = ? ) OR ( or1 = ? )");

        assertEquals(Iterators.size(wheres.getWhereValues()), 5);

        assertEquals(wheres.getWheres(), "( _id1 NOT IN ( ?, ?, ? ) ) AND ( and1 = ? ) OR ( or1 = ? )");
    }
}