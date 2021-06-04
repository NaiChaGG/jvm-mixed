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
