package ru.dsoccer1980.cache;

import java.util.Optional;

public interface CacheEngine<K, V> {

    void put(MyElement<K, V> element);

    Optional<MyElement<K, V>> get(K key);

    int getHitCount();

    int getMissCount();

    void dispose();

    void update(MyElement<K, V> element);
}
