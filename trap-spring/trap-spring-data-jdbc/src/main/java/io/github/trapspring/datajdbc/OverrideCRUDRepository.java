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
import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@Transactional(readOnly = true)
public class OverrideCRUDRepository<T, ID> extends SimpleJdbcRepository<T, ID> {

    public OverrideCRUDRepository(JdbcAggregateOperations entityOperations, PersistentEntity<T, ?> entity) {
        super(entityOperations, entity);
    }

    @Override
    public <S extends T> S save(S instance) {
        if (instance instanceof Persistable && ((Persistable<?>) instance).isNew()) {
            return super.save(instance);
        }
        throw new IllegalArgumentException("Entity must be isNew");
    }

    @Override
    public void delete(T instance) {
        // 禁止使用该方法
        // use @Query delete
        throw new DisableCrudRepositoryException("delete");
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        // 禁止使用该方法
        throw new DisableCrudRepositoryException("delete all");
    }

}
