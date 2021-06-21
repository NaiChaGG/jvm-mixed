package io.github.jojoti.utilguavaext;

import com.google.common.collect.*;

import java.util.List;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public interface GuavaCollects {

    static <K, V> Multimap<K, V> listGroupMultimap(Iterable<V> v, KeyCreator<K, V> keyCreatorCreate) {
        return listGroupMultimap(Lists.newArrayList(v), keyCreatorCreate);
    }

    /**
     * values 允许重复
     * <p>
     * 对数据进行分组 List<T> -> Multimap<K, V> {"i":["1", "2"]}
     *
     * @param v
     * @param keyCreatorCreate
     * @param <K>
     * @param <V>
     * @return
     */
    static <K, V> Multimap<K, V> listGroupMultimap(List<V> v, KeyCreator<K, V> keyCreatorCreate) {
        final var group = GuavaCollects.<K, V>newHashArrayMultimap();
        for (V v1 : v) {
            group.put(keyCreatorCreate.getKey(v1), v1);
        }
        return group;
    }

    static <K, V> Multimap<K, V> newHashArrayMultimap() {
        return Multimaps.<K, V>newMultimap(Maps.newHashMap(), Lists::newArrayList);
    }

    static <K, V> Multimap<K, V> newHashSetsMultimap() {
        return Multimaps.<K, V>newMultimap(Maps.newHashMap(), Sets::newHashSet);
    }

    static <R, C, V> Table<R, C, V> newConcurrentTable() {
        return Tables.newCustomTable(Maps.newConcurrentMap(), Maps::newConcurrentMap);
    }

    static <R, C, V> Table<R, C, V> newTable() {
        return Tables.newCustomTable(Maps.newHashMap(), Maps::newConcurrentMap);
    }

    interface KeyCreator<K, V> {

        K getKey(V v);

    }

}