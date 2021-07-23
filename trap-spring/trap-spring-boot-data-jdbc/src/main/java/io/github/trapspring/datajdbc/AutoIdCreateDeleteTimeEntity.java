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

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import javax.persistence.GenerationType;
import javax.persistence.MappedSuperclass;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@MappedSuperclass
@Table
public abstract class AutoIdCreateDeleteTimeEntity extends TrapBaseCreateDeleteTimeEntity {

    @Id
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Override
    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
