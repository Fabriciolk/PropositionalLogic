package cache;

import notification.Observer;
import propositionalLogic.proposition.properties.State;

import java.util.List;
import java.util.Map;

public interface ICacheManager<K, V>
{
    V get(K key);

    Map<K, V> getAll(List<K> keys);

    V getAndRemove(K key);

    void put(K key, V value);

    void clear();

    boolean containKey(K key);

    void addObserver(K key, State state, Observer observer);

    void removeObserver(K key, State state, Observer observer);
}
