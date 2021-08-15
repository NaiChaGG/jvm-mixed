///*
// * Copyright 2021 JoJo Wang , homepage: https://github.com/jojoti/jvm-mixed.
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
//package io.github.jojoti.util.sqlbuilder.joiner;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
///**
// * Created by @JoJo Wang on 2016/8/2.
// *
// * @author JoJo Wang
// *
// */
//public class GroupsTest {
//
//    @Test
//    public void testBy() throws Exception {
//        assertEquals(Groups.by("_id", "_id2").sql(), " GROUP BY _id, _id2");
//    }
//
//    @Test
//    public void testSUM() throws Exception {
//
//
//        assertEquals(Groups.by("_id1").sum("_id1").sql(), "SUM(_id1) AS __sum");
//    }
//
//    @Test
//    public void testCOUNT() throws Exception {
//        assertEquals(Groups.by("_id1").COUNT("_id1").sql(), "SELECT _id1, COUNT(_id1) AS __count FROM runable GROUP BY _id1");
//    }
//
//    @Test
//    public void testAVG() throws Exception {
//        assertEquals(Groups.by("_id1").avg("_id1").sql(), "SELECT _id1, AVG(_id1) AS __avg FROM runable GROUP BY _id1");
//    }
//
//    @Test
//    public void testMAX() throws Exception {
//        assertEquals(Groups.by("_id1").max("_id1").sql(), "SELECT _id1, MAX(_id1) AS __max FROM runable GROUP BY _id1");
//    }
//
//    @Test
//    public void testHaving() throws Exception {
//        assertEquals(Groups.by("_id1").max("_id1").having(Wheres.equal("_id1", 0)).sql(), "SELECT _id1, MAX(_id1) AS __max FROM runable GROUP BY _id1 HAVING ( _id1 = ? )");
//    }
//
//    @Test
//    public void testGetAppendFunctionSQL() throws Exception {
//        assertEquals(Groups.by("_id1").max("_id1").having(Wheres.equal("_id1", 0)).getAppendFunctionSQL()
//                , "_id1, MAX(_id1) AS __max");
//    }
//
//    @Test
//    public void testGetGroupAndHavingSQL() throws Exception {
//        assertEquals(Groups.by("_id1").max("_id1").having(Wheres.equal("_id1", 0)).sql()
//                , "GROUP BY _id1 HAVING ( _id1 = ? )");
//    }
//}