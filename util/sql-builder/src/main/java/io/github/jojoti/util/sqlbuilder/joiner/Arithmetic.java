/*
 * Copyright 2021 JoJo Wang , homepage: https://github.com/jojoti/jvm-mixed.
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

/**
 * Created by @JoJo Wang on 2017/4/10.
 */
public enum Arithmetic {
    LIKE("LIKE"), NOT_LIKE("NOT LIKE"),
    EQUAL("="), NOT_EQUAL("<>"),
    GREATER_THAN(">"), LESS_THAN("<"),
    GREATER_THAN_OR_EQUAL(">="), LESS_THAN_OR_EQUAL("<="),
    IN("IN"), NOT_IN("NOT IN"),
    ;

    private String value;

    Arithmetic(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
