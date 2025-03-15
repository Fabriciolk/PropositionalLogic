package propositionalLogic.cache.proposition;

import cache.CacheManager;
import cache.ICacheManager;
import propositionalLogic.proposition.Proposition;

import java.util.Collection;
import java.util.HashMap;

public class PropositionCache
{
    private static PropositionCache singletonInstance;
    private static final ICacheManager<String, PropertyCache> cacheManager = new CacheManager<>(new HashMap<>());

    public PropositionCache() {}

    public static PropositionCache getInstance()
    {
        if (singletonInstance != null) return singletonInstance;
        singletonInstance = new PropositionCache();
        return singletonInstance;
    }

    public static synchronized void registerProposition(Proposition proposition)
    {
        if (cacheManager.containKey(proposition.toString())) return;
        cacheManager.put(proposition.toString(), new PropertyCache(proposition));
    }

    public static synchronized void registerProposition(Collection<Proposition> propositions)
    {
        for (Proposition proposition : propositions) {
            registerProposition(proposition);
        }
    }

    public static void unregisterProposition(Proposition proposition)
    {
        cacheManager.getAndRemove(proposition.toString());
    }

    public static void unregisterProposition(Collection<Proposition> propositions)
    {
        for (Proposition proposition : propositions) {
            unregisterProposition(proposition);
        }
    }

    public PropertyCache getCache(Proposition proposition)
    {
        registerProposition(proposition);
        return cacheManager.get(proposition.toString());
    }
}
