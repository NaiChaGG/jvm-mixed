package io.github.jojoti.util.sqlbuilder;

import io.github.jojoti.util.sqlbuilder.joiner.Arithmetic;

/**
 * Created by @JoJo Wang on 2017/4/10.
 */
public interface IQuerySelect extends IQuery, PrepareQuery, QueryWhere {

    <T extends IQuerySelect> T subQuery(String field, Arithmetic arithmetic, IQuerySelect querySelect);

    <T extends IQuerySelect> T orSubQuery(String field, Arithmetic arithmetic, IQuerySelect querySelect);

    <T extends IQuerySelect> T andSubQuery(String field, Arithmetic arithmetic, IQuerySelect querySelect);

    String getCountSql();

    int getLimit();

    long getOffset();

}
