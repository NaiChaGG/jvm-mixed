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

package io.github.jojoti.util.sqlbuilder;

import io.github.jojoti.util.sqlbuilder.joiner.IWheres;

import java.util.Iterator;

/**
 * Created by @JoJo Wang on 2017/4/10.
 */
public abstract class StreamBase implements IQuery, PrepareQuery, QueryWhere {

    protected IWheres wheres;

    @Override
    public Iterator<Object> getPrepareIteValues() {
        return this.wheres.getPrepareIteValues();
    }

    @Override
    public Object[] getPrepareValues() {
        return this.wheres.getPrepareValues();
    }

    @Override
    public Iterator<Object> getWhereValues() {
        return this.getPrepareIteValues();
    }

    @Override
    public Object[] getValues() {
        return this.getPrepareValues();
    }

}
