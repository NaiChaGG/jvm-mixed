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

package io.github.jojoti.utilsversioncode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
class VersionCodeTest {

    @org.junit.jupiter.api.Test
    void encode() {
        assertEquals(VersionCode.getDefault().encode("1.1.1"), 10101);
    }

    @org.junit.jupiter.api.Test
    void compare() {
        assertTrue(VersionCode.getDefault().compare("2.1.1", "1.9.99"));
    }

    @org.junit.jupiter.api.Test
    void decode() {
//        assertEquals("1.1.1", VersionCode.getDefault().decode(10101));
    }
}