package cache;

import notification.GenericSubject;
import notification.NotificationData;
import notification.Observer;
import propositionalLogic.proposition.properties.State;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheManager<K, V> implements ICacheManager<K, V>
{
    private final Map<K, GenericSubject<State>> observersMap = new HashMap<>();
    private final Map<K, CacheValue<V>> cache;

    public CacheManager(Map<K, CacheValue<V>> cache)
    {
        this.cache = cache;
    }

    @Override
    public V get(K key) {
        return cache.get(key).value();
    }

    @Override
    public Map<K, V> getAll(List<K> keys) {
        Map<K, V> map = new HashMap<>();

        for (K key : keys) map.put(key, cache.get(key).value());

        return map;
    }

    @Override
    public V getAndRemove(K key) {
        notify(key, State.REMOVED, null);
        if (cache.containsKey(key)) return cache.remove(key).value();
        else return null;
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, createCacheValue(value));
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public boolean containKey(K key) {
        return cache.containsKey(key);
    }

    private CacheValue<V> createCacheValue(V value) {
        return () -> value;
    }

    public void notify(K key, State state, NotificationData data)
    {
        if (observersMap.containsKey(key)) observersMap.get(key).notifyEvent(state, data);

    }

    @Override
    public void addObserver(K key, State state, Observer observer)
    {
        if (!observersMap.containsKey(key)) {
            observersMap.put(key, new GenericSubject<>());
        }
        observersMap.get(key).addObserver(state, observer);
    }

    @Override
    public void removeObserver(K key, State state, Observer observer) {
        if (observersMap.containsKey(key)) {
            observersMap.get(key).removeObserver(state, observer);
        }
    }
}
