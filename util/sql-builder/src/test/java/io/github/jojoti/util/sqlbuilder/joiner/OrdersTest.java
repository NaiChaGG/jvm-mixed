/*
 * Copyright 2021 JoJo Wang , homepage: https://github.com/jojoti.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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