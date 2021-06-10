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

import io.github.jojoti.util.sqlbuilder.joiner.Arithmetic;
import io.github.jojoti.util.sqlbuilder.joiner.Wheres;

/**
 * Created by @JoJo Wang on 2017/4/10.
 */
public class Examples {

    public static void main(String[] args) {
        StreamSelect streamSelect = StreamSelect.from("xx", "id")
                .where(null);

        // select * from table where a > 0 and b > (select a form b where a > 0)
        // 用这个还不如直接写 sql
        StreamSelect.from("table")
                .where(Wheres.greaterThan("a", 0))
                .orSubQuery("b", Arithmetic.GREATER_THAN,
                        StreamSelect.from("b", "a")
                                .where(Wheres.greaterThan("a", 0))
                );

    }

}
