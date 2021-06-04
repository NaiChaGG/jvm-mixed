package io.github.jojoti.util.sqlbuilder.joiner;

import io.github.jojoti.util.sqlbuilder.IQuery;
import io.github.jojoti.util.sqlbuilder.PrepareQuery;

/**
 * Created by @JoJo Wang on 2017/4/12.
 */
public interface IWheres extends IQuery, PrepareQuery {

    Wheres and(IWheres wheres);

    Wheres or(IWheres wheres);

    String whereSQL();

}
