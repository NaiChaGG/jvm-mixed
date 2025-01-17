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
//public class StreamInsertTest {
//    @Test
//    public void testForm() throws Exception {
//        StreamInsert streamInsert = StreamInsert.from("runable", "test", "test1")
//                .row("fdsfsdfsdf", 1)
//                .row("xxxxx", 1);
//
//        assertEquals("INSERT INTO runable (id, name) VALUES (?, ?), (?, ?)", streamInsert.getPrepareSQL());
//
//    }
//
//    @Test
//    public void testFields() throws Exception {
//        StreamInsert streamInsert = StreamInsert.from("runable", "id", "name")
//                .row("fdsfsdfsdf", 1)
//                .row("xxxxx", 1);
//
//        assertEquals("INSERT INTO runable (id, name) VALUES (?, ?), (?, ?)", streamInsert.getPrepareSQL());
//    }
//
//    @Test
//    public void testValues() throws Exception {
//        StreamInsert streamInsert = StreamInsert.from("runable", "id", "name")
//                .row("fdsfsdfsdf", 1)
//                .row("xxxxx", 1);
//
//        assertEquals("INSERT INTO runable (id, name) VALUES (?, ?), (?, ?)", streamInsert.getPrepareSQL());
//        assertEquals(streamInsert.getPrepareValues(), 4);
//    }
//
//    @Test
//    public void testGetValues() throws Exception {
//        StreamInsert streamInsert = StreamInsert.from("runable", "id", "name")
//                .row("fdsfsdfsdf", 1)
//                .row("xxxxx", 1);
//
//        assertEquals("INSERT INTO runable (id, name) VALUES (?, ?), (?, ?)", streamInsert.getPrepareSQL());
//        assertEquals(Iterators.size(streamInsert.getPrepareIteValues()), 4);
//    }
//}