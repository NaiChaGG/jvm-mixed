package io.github.jojoti.util.sqlbuilder;

import io.github.jojoti.util.sqlbuilder.joiner.IWheres;
import io.github.jojoti.util.sqlbuilder.joiner.Wheres;

/**
 * Created by @JoJo Wang on 2016/7/31.
 *
 * @author JoJo Wang
 *

 */
public class StreamDelete extends StreamBase implements IQueryDelete {

    private final String sql;

    public StreamDelete(String table) {
        this.sql = "DELETE FROM " + table;
    }

    /**
     * @param table
     * @return
     */
    public static StreamDelete from(String table) {
        return new StreamDelete(table);
    }

    /**
     * @param table
     * @return
     */
    public static StreamDelete from(String table, Wheres wheres) {
        StreamDelete streamDelete = from(table);
        streamDelete.where(wheres);
        return streamDelete;
    }

    @Override
    public String querySQL() {
        if (this.wheres != null) {
            return this.sql + " " + this.wheres.querySQL();
        }
        return this.sql.toString();
    }

    @Override
    public String getPrepareSQL() {
        if (this.wheres != null) {
            return this.sql + " " + this.wheres.getPrepareSQL();
        }
        return this.sql.toString();
    }

    @Override
    public StreamDelete where(IWheres wheres) {
        this.wheres = wheres;
        return this;
    }

    @Override
    public StreamDelete orWhere(IWheres wheres) {
        this.wheres.or(wheres);
        return this;
    }

    @Override
    public StreamDelete andWhere(IWheres wheres) {
        this.wheres.and(wheres);
        return this;
    }

}