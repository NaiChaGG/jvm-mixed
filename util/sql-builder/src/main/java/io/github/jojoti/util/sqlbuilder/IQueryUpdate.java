package io.github.jojoti.util.sqlbuilder;

import java.util.Iterator;

/**
 * Created by @JoJo Wang on 2017/4/10.
 */
public interface IQueryUpdate extends IQuery, PrepareQuery, QueryWhere {

    Iterator<Object> getSetValues();

}
