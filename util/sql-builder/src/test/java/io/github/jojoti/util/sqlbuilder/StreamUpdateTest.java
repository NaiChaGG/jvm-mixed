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
//package io.github.jojoti.util.sqlbuilder;
//
//import com.google.common.collect.Iterators;
//import io.github.jojoti.util.sqlbuilder.joiner.Sets;
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
//public class StreamUpdateTest {
//    @Test
//    public void testForm() throws Exception {
//        StreamUpdate streamUpdate = StreamUpdate.from("table", Sets.on("filed1", 0))
//                .where(Wheres.equal("f1", 0)
//                        .and(Wheres.equal("1", 1))
//                );
//
//        assertEquals("UPDATE table1 SET filed1 = ? WHERE ( f1 = ? ) AND ( 1 = ? )", streamUpdate.getPrepareSQL());
//    }
//
//    @Test
//    public void testJoin() throws Exception {
//        StreamUpdate streamUpdate = StreamUpdate.from("table1", Sets.on("filed1", 0))
//                .where(
//                        Wheres.equal("f1", 0)
//                                .and(Wheres.equal("1", 1))
//                );
//
//        assertEquals("UPDATE table1 SET filed1 = ? WHERE ( f1 = ? ) AND ( 1 = ? )", streamUpdate.getPrepareSQL());
//        assertEquals(Iterators.size(streamUpdate.getPrepareIteValues()), 2);
//    }
//
//    @Test
//    public void testSet() throws Exception {
//        StreamUpdate streamUpdate = StreamUpdate.from("table1", Sets.on("filed1", 0)).where(
//                Wheres.equal("f1", 0)
//                        .and(Wheres.equal("1", 1))
//        );
//
//        assertEquals("UPDATE table1 SET filed1 = ? WHERE ( f1 = ? ) AND ( 1 = ? )", streamUpdate.getPrepareSQL());
//        assertEquals(Iterators.size(streamUpdate.getPrepareIteValues()), 2);
//        assertEquals(Iterators.size(streamUpdate.getSetValues()), 1);
//    }
//
//    @Test
//    public void testGetSetValues() throws Exception {
//        StreamUpdate streamUpdate = StreamUpdate.from("table1", Sets.on("filed1", 0)).where(
//                Wheres.equal("f1", 0)
//                        .and(Wheres.equal("1", 1))
//        );
//
//        assertEquals("UPDATE table1 SET filed1 = ? WHERE ( f1 = ? ) AND ( 1 = ? )", streamUpdate.getPrepareSQL());
//        assertEquals(Iterators.size(streamUpdate.getPrepareIteValues()), 2);
//        assertEquals(Iterators.size(streamUpdate.getSetValues()), 1);
//    }
//
//    @Test
//    public void testGetWhereValues() throws Exception {
//        StreamUpdate streamUpdate = StreamUpdate.from("table1", Sets.on("filed1", 0)).where(
//                Wheres.equal("f1", 0)
//                        .and(Wheres.equal("1", 1))
//        );
//
//        assertEquals("UPDATE table1 SET filed1 = ? WHERE ( f1 = ? ) AND ( 1 = ? )", streamUpdate.getPrepareSQL());
//        assertEquals(Iterators.size(streamUpdate.getPrepareIteValues()), 2);
//        assertEquals(Iterators.size(streamUpdate.getSetValues()), 1);
//    }
//}