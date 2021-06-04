package io.github.jojoti.util.sqlbuilder;

import java.util.Iterator;

/**
 * Created by @JoJo Wang on 2017/4/10.
 */
public interface PrepareQuery {

    Iterator<Object> getPrepareIteValues();

    Object[] getPrepareValues();

    String getPrepareSQL();

}
