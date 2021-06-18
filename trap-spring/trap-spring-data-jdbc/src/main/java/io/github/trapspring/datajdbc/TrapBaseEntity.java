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

package io.github.trapspring.datajdbc;

import org.springframework.data.domain.Persistable;

import javax.persistence.MappedSuperclass;

/**
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@MappedSuperclass
public abstract class TrapBaseEntity implements Persistable<Long> {

    // 禁止使用 save update 方法
    @Override
    public final boolean isNew() {
        return true;
    }

}
