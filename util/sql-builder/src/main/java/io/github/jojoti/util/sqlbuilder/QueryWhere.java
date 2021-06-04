package io.github.jojoti.util.sqlbuilder;

import io.github.jojoti.util.sqlbuilder.joiner.IWheres;

import java.util.Iterator;

/**
 * Created by @JoJo Wang on 2017/4/10.
 */
public interface QueryWhere {

    <T extends QueryWhere> T where(IWheres wheres);

    <T extends QueryWhere> T orWhere(IWheres wheres);

    <T extends QueryWhere> T andWhere(IWheres wheres);

    Iterator<Object> getWhereValues();

    Object[] getValues();

}
