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

package io.github.jojoti.grpcstartersbram;

import com.google.common.collect.ImmutableList;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public interface SessionCreator extends Session {

    SessionUser valid(String token, ImmutableList<String> attachInline);

    final class NewToken {
        private final String slat;
        private final String tokenBase64;

        public NewToken(String slat, String tokenBase64) {
            this.slat = slat;
            this.tokenBase64 = tokenBase64;
        }

        public String getSlat() {
            return slat;
        }

        public String getTokenBase64() {
            return tokenBase64;
        }
    }

}
