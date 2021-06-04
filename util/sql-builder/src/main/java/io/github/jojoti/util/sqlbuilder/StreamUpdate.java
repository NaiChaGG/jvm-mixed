package io.github.jojoti.util.sqlbuilder;

import io.github.jojoti.util.sqlbuilder.joiner.IWheres;
import io.github.jojoti.util.sqlbuilder.joiner.Sets;
import io.github.jojoti.util.sqlbuilder.joiner.Wheres;

import java.util.Iterator;

/**
 * Created by @JoJo Wang on 2016/7/31.
 *
 * @author JoJo Wang
 *

 */
public class StreamUpdate extends StreamBase implements IQueryUpdate {
    private final StringBuffer sql = new StringBuffer();

    protected Iterator<Object> setValues;
    protected Iterator<Object> whereValues;

    // 限制new对象
    private StreamUpdate(String table) {
        this.sql.append("UPDATE ");
        this.sql.append(table);
    }

    public static StreamUpdate from(String table, Sets sets) {
        StreamUpdate streamUpdate = new StreamUpdate(table);
        streamUpdate.sql.append(" ");
        streamUpdate.sql.append(sets.getSetSQL());
        streamUpdate.setValues = sets.getSetsValues();
        return streamUpdate;
    }

    public static StreamUpdate from(String table, Sets sets, Wheres wheres) {
        StreamUpdate streamUpdate = from(table, sets);
        streamUpdate.join(wheres);
        return streamUpdate;
    }

    public StreamUpdate join(Wheres wheres) {
        this.sql.append(" ");
        this.sql.append(wheres.sql());
        this.whereValues = wheres.getWhereValues();
        return this;
    }

    @Override
    public StreamUpdate where(IWheres wheres) {
        this.wheres = wheres;
        return this;
    }

    @Override
    public StreamUpdate orWhere(IWheres wheres) {
        this.wheres.or(wheres);
        return this;
    }

    @Override
    public StreamUpdate andWhere(IWheres wheres) {
        this.wheres.and(wheres);
        return this;
    }

    @Override
    public String querySQL() {
        return null;
    }

    @Override
    public String getPrepareSQL() {
        return null;
    }

    @Override
    public Iterator<Object> getSetValues() {
        return null;
    }

}