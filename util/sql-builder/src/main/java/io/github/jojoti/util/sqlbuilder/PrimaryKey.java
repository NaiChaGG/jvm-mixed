package io.github.jojoti.util.sqlbuilder;

/**
 * Created by @JoJo Wang on 2016/8/18.
 *
 * @author JoJo Wang
 *

 */
public interface PrimaryKey<T> {

    /**
     * 主键接口
     *
     * @return T
     */
    T getId();

}