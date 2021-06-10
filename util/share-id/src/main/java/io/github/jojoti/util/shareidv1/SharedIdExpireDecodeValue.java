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

package io.github.jojoti.util.shareidv1;

public class SharedIdExpireDecodeValue {

    private final Long id;
    private final Long expireTime;
    private final Long expireId;
    private final Long signErrorId;

    public SharedIdExpireDecodeValue(Long id, Long expireTime, Long expireId, Long signErrorId) {
        this.id = id;
        this.expireTime = expireTime;
        this.expireId = expireId;
        this.signErrorId = signErrorId;
    }

    public Long getId() {
        return id;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public Long getExpireId() {
        return expireId;
    }

    public boolean isOk() {
        return this.id != null;
    }

    public boolean isExpired() {
        return this.expireId != null;
    }

    public boolean isSignError() {
        return this.signErrorId != null;
    }

    public Long getSignErrorId() {
        return signErrorId;
    }
}