/*
 * Copyright 2021 JoJo Wang , homepage: https://github.com/jojoti/experiment-jvm.
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

package io.github.jojoti.util.sqlbuilder;

import io.github.jojoti.util.sqlbuilder.joiner.Wheres;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by @JoJo Wang on 2016/8/23.
 *
 * @author JoJo Wang
 *

 */
public class StreamDeleteTest {
    @Test
    public void testForm() throws Exception {

        StreamDelete streamDelete = StreamDelete.from("delete1").where(
                Wheres.equal("test1", 0)
                        .and(Wheres.like("122", "xx"))
        );

        assertEquals("DELETE FROM delete1 WHERE ( test1 = ? ) AND ( 122 LIKE ? )", streamDelete.getPrepareSQL());
    }

    @Test
    public void testJoin() throws Exception {

        StreamDelete streamDelete = StreamDelete.from("delete1").where(
                Wheres.equal("test1", 0)
                        .and(Wheres.like("122", "xx"))
        );

        assertEquals("DELETE FROM delete1 WHERE ( test1 = ? ) AND ( 122 LIKE ? )", streamDelete.getPrepareSQL());
        assertEquals(streamDelete.getValues().length, 2);
    }

    @Test
    public void testGetWhereValues() throws Exception {
        StreamDelete streamDelete = StreamDelete.from("delete1").where(
                Wheres.equal("test1", 0)
                        .and(Wheres.like("122", "xx"))
        );

        assertEquals("DELETE FROM delete1 WHERE ( test1 = ? ) AND ( 122 LIKE ? )", streamDelete.getPrepareSQL());
        assertEquals(streamDelete.getValues().length, 2);
    }
}