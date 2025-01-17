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

package io.github.trapspring.mybatisplus;

import javax.persistence.MappedSuperclass;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@MappedSuperclass
// @Table 必须注解， 否则 Spring Data JDBC - Could not safely identify store assignment for repository candidate interface io.github.jojoti.grpcstartersbexamples.AccountGuestRepository. If you want this repository to be a JDBC repository, consider annotating your entities with one of these annotations: org.springframework.data.relational.core.mapping.Table.
// 会导致无法加载 bean
public abstract class TrapBaseCreateUpdateDeleteTimeEntity extends TrapBaseCreateUpdateTimeEntity {

    @javax.persistence.Column(columnDefinition = "bigint(20) unsigned not null default 0")
    private long deleteAt;

    public long getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(long deleteAt) {
        this.deleteAt = deleteAt;
    }

}
