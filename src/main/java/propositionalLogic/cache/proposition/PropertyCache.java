package propositionalLogic.cache.proposition;

import cache.CacheManager;
import cache.ICacheManager;
import propositionalLogic.proposition.Atom;
import propositionalLogic.proposition.Proposition;
import propositionalLogic.proposition.PropositionType;
import propositionalLogic.proposition.properties.Property;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PropertyCache
{
    private final Proposition owner;
    private ICacheManager<Property, Boolean> booleanCache;
    private ICacheManager<Property, Set<Proposition>> propositionSetCache;
    private ICacheManager<Property, Set<Atom>> AtomSetCache;
    private ICacheManager<Property, Proposition> propositionCache;
    private ICacheManager<Property, Map<PropositionType, Integer>> typesCountersCache;

    public PropertyCache(Proposition owner)
    {
        this.owner = owner;

        booleanCache = new CacheManager<>(new HashMap<>());
        propositionSetCache = new CacheManager<>(new HashMap<>());
        AtomSetCache = new CacheManager<>(new HashMap<>());
        propositionCache = new CacheManager<>(new HashMap<>());
        typesCountersCache = new CacheManager<>(new HashMap<>());
    }

    public ICacheManager<Property, Boolean> getBooleanCache() {
        return booleanCache;
    }

    public <T> ICacheManager<Property, T> getCacheByType(Class<T> tClass) {
        System.out.println(booleanCache.getClass());
        System.out.println(tClass);
        return null;
    }

    public ICacheManager<Property, Set<Proposition>> getPropositionSetCache() {
        return propositionSetCache;
    }

    public ICacheManager<Property, Set<Atom>> getAtomSetCache() {
        return AtomSetCache;
    }

    public ICacheManager<Property, Proposition> getPropositionCache() {
        return propositionCache;
    }

    public ICacheManager<Property, Map<PropositionType, Integer>> getTypesCounters() {
        return typesCountersCache;
    }
}
