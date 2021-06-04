package io.github.jojoti.util.sqlbuilder;

/**
 * Created by @JoJo Wang on 2017/4/10.
 */
public interface IQueryInsert extends IQuery, PrepareQuery {

    <T extends IQueryInsert> T row(Object value, Object... values);

}
