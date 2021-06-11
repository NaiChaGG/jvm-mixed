///*
// * Copyright 2021 JoJo Wang , homepage: https://github.com/jojoti/experiment-jvm.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package io.github.jojoti.util.sqlbuilder;
//
//import com.google.common.collect.Iterators;
//import io.github.jojoti.util.sqlbuilder.joiner.Groups;
//import io.github.jojoti.util.sqlbuilder.joiner.Orders;
//import io.github.jojoti.util.sqlbuilder.joiner.Wheres;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
///**
// * Created by @JoJo Wang on 2016/8/23.
// *
// * @author JoJo Wang
// *
//
// */
//public class StreamSelectTest {
//    @Test
//    public void testForm() throws Exception {
//
//        StreamSelect streamSelect = StreamSelect.from("table");
//
//        assertEquals(streamSelect.getPrepareSQL(), "SELECT * FROM table");
//        assertEquals(streamSelect.getCountSql(), "SELECT COUNT(1) AS __count FROM table");
//        assertEquals(streamSelect.getValues().length, 0);
//        assertEquals(streamSelect.getLimit(), 20);
//        assertEquals(streamSelect.getOffset(), 0);
//
//        // 增加条件
//        streamSelect.where(Wheres.equal("append", 0));
//        assertEquals(streamSelect.getPrepareSQL(),
//                "SELECT * FROM table WHERE ( append = ? )");
//        assertEquals(streamSelect.getCountSql(),
//                "SELECT COUNT(1) AS __count FROM table WHERE ( append = ? )");
//        assertEquals(Iterators.size(streamSelect.getPrepareIteValues()), 1);
//        assertEquals(streamSelect.getLimit(), 20);
//        assertEquals(streamSelect.getOffset(), 0);
//    }
//
//    @Test
//    public void testFields() throws Exception {
//        StreamSelect streamSelect = StreamSelect.from("table", "_id");
//
//        assertEquals(streamSelect.getPrepareSQL(), "SELECT _id FROM table");
//        assertEquals(streamSelect.getCountSql(), "SELECT COUNT(1) AS __count FROM table");
//        assertEquals(Iterators.size(streamSelect.getPrepareIteValues()), 0);
//        assertEquals(streamSelect.getLimit(), 20);
//        assertEquals(streamSelect.getOffset(), 0);
//
//        // 增加条件
//        streamSelect.where(Wheres.equal("append", 0));
//        assertEquals(streamSelect.getPrepareSQL(),
//                "SELECT _id FROM table WHERE ( append = ? )");
//        assertEquals(streamSelect.getCountSql(),
//                "SELECT COUNT(1) AS __count FROM table WHERE ( append = ? )");
//        assertEquals(Iterators.size(streamSelect.getPrepareIteValues()), 1);
//        assertEquals(streamSelect.getLimit(), 20);
//        assertEquals(streamSelect.getOffset(), 0);
//    }
//
//    @Test
//    public void testFieldAppend() throws Exception {
//
//        StreamSelect streamSelect = StreamSelect.from("table", "_id");
//
//        assertEquals(streamSelect.getPrepareSQL(),
//                "SELECT _id, _id1 FROM table");
//        assertEquals(streamSelect.getCountSql(),
//                "SELECT COUNT(1) AS __count FROM table");
//        assertEquals(Iterators.size(streamSelect.getPrepareIteValues()), 0);
//        assertEquals(streamSelect.getLimit(), 20);
//        assertEquals(streamSelect.getOffset(), 0);
//
//        // 增加条件
//        streamSelect.where(Wheres.equal("append", 0));
//        assertEquals(streamSelect.getPrepareSQL(),
//                "SELECT _id, _id1 FROM table WHERE ( append = ? )");
//        assertEquals(streamSelect.getCountSql(),
//                "SELECT COUNT(1) AS __count FROM table WHERE ( append = ? )");
//        assertEquals(Iterators.size(streamSelect.getPrepareIteValues()), 1);
//        assertEquals(streamSelect.getLimit(), 20);
//        assertEquals(streamSelect.getOffset(), 0);
//
//    }
//
//    @Test
//    public void testOffset() throws Exception {
//        assertEquals(StreamSelect.from("table", "_id").offset(10).getOffset(), 10);
//    }
//
//    @Test
//    public void testLimit() throws Exception {
//        assertEquals(StreamSelect.from("table", "_id").limit(10).getLimit(), 10);
//    }
//
//    @Test
//    public void testJoin() throws Exception {
//
//        assertEquals(StreamSelect.from("table", "_id").where(
//                Wheres.like("foo", "bar")
//        ).getPrepareSQL(), "SELECT _id FROM table WHERE ( foo LIKE ? )");
//
//        assertEquals(StreamSelect.from("table", "_id").order(
//                Orders.by().asc("_id")
//        ).getPrepareSQL(), "SELECT _id FROM table ORDER BY _id ASC");
//
//        assertEquals(StreamSelect.from("table", "_id").order(
//                Orders.by().ASC("_id")
//        ).getPrepareSQL(), "SELECT _id FROM table GROUP BY _id");
//
//        assertEquals(StreamSelect.from("table", "_id").group(
//                Groups.by("_id").SUM("_id")
//
//        ).getPrepareSQL(), "SELECT _id, SUM(_id) AS __sum FROM table GROUP BY _id");
//
//        assertEquals(StreamSelect.from("table", "_id").group(
//                Groups.by("_id").SUM("_id").having(Wheres.equal("_id", 0))
//        ).getPrepareSQL(), "SELECT _id, SUM(_id) AS __sum FROM table GROUP BY _id HAVING ( _id = ? )");
//    }
//
//    @Test
//    public void testYeahCountSql() throws Exception {
//        assertEquals(StreamSelect.from("table", "_id").where(
//                Wheres.like("foo", "bar")
//        ).getCountSql(), "SELECT COUNT(1) AS __count FROM table WHERE ( foo LIKE ? )");
//    }
//
//    @Test
//    public void testAppendWhere() throws Exception {
//        assertEquals(StreamSelect.from("table", "_id").where(
//                Wheres.like("foo", "bar")
//        ).getPrepareSQL(), "SELECT _id FROM table WHERE ( foo LIKE ? )");
//    }
//
//    @Test
//    public void testGetWhereValues() throws Exception {
//        assertEquals(Iterators.size(StreamSelect.from("table", "_id").where(
//                Wheres.equal("foo", "bar")
//        ).getPrepareIteValues()), 1);
//    }
//
//    @Test
//    public void testsql() {
//        // select * from table1 where w1=2 and w2 <> 2 or (w3 = 2 or w4 = 2 and w5 = 2)
//        // and w6 = 0 order by w6 desc offset 0 limit 10
//
//        // select * from table1 where (w1=2) and (w2 <> 2) or (w3 = 2 or w4 = 2 and w5 = 2)
//        // and (w6 = 0) order by w6 desc offset 0 limit 10
//        StreamSelect.from("table1", "*")
//                .where(Wheres.greaterThan("w1", 2))
//                .andWhere(Wheres.notEqual("w2", 2))
//                .orWhere(Wheres.equal("w3", 2)
//                        .or(Wheres.equal("w4", 2))
//                        .and(Wheres.equal("w5", 2)))
//                .andWhere(Wheres.equal("w6", 0))
//                .order(Orders.by().desc("w6"))
//                .offset(0)
//                .limit(10);
//
//
//        StreamSelect yeahSelect = StreamSelect.from("table1")
//                .where(Wheres.greaterThan("w1", 2).and(
//                        Wheres.greaterThan("w2", 2)).or(
//                        Wheres.greaterThan("w3", 1)
//                                .or(Wheres.lessThan("w4", 2))
//                                .and(Wheres.equal("w5", "2"))
//                        )
//                                .and(Wheres.equal("w6", "0")).and(Wheres.in("inx", 1, 2, 4, 3))
//                )
//                .order(Orders.by().desc("w6"))
//                .offset(0)
//                .limit(10);
//        assertEquals(yeahSelect.getPrepareSQL(),
//                "SELECT * FROM table1 WHERE ( w1 > ? ) AND " +
//                        "( w2 > ? ) OR ( w3 > ?  OR ( w4 < ? ) AND ( w5 = ? ))" +
//                        " AND ( w6 = ? ) AND ( inx IN ( ?, ?, ?, ? ) ) ORDER BY w6 DESC");
//
//        assertEquals(yeahSelect.getCountSql(),
//                "SELECT COUNT(1) AS __count FROM table1 WHERE ( w1 > ? ) AND " +
//                        "( w2 > ? ) OR ( w3 > ?  OR ( w4 < ? ) AND ( w5 = ? ))" +
//                        " AND ( w6 = ? ) AND ( inx IN ( ?, ?, ?, ? ) )");
//
//        assertEquals(Iterators.size(yeahSelect.getPrepareIteValues()), 10);
//        assertEquals(yeahSelect.getLimit(), 10);
//        assertEquals(yeahSelect.getOffset(), 0);
//
//        yeahSelect.limit(50);
//        yeahSelect.offset(10);
//
//        // 增加条件
//        yeahSelect.where(Wheres.equal("append", 0));
//        assertEquals(yeahSelect.getPrepareSQL(),
//                "SELECT * FROM table1 WHERE ( w1 > ? ) AND " +
//                        "( w2 > ? ) OR ( w3 > ?  OR ( w4 < ? ) AND ( w5 = ? ))" +
//                        " AND ( w6 = ? ) AND ( inx IN ( ?, ?, ?, ? ) ) AND ( append = ? ) ORDER BY w6 DESC");
//
//        assertEquals(yeahSelect.getCountSql(),
//                "SELECT COUNT(1) AS __count FROM table1 WHERE ( w1 > ? ) AND " +
//                        "( w2 > ? ) OR ( w3 > ?  OR ( w4 < ? ) AND ( w5 = ? ))" +
//                        " AND ( w6 = ? ) AND ( inx IN ( ?, ?, ?, ? ) ) AND ( append = ? )");
//        assertEquals(Iterators.size(yeahSelect.getPrepareIteValues()), 11);
//        assertEquals(yeahSelect.getLimit(), 50);
//        assertEquals(yeahSelect.getOffset(), 10);
//
//        StreamSelect.from("table").order(Orders.by());
//    }
//}