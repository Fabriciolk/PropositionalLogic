package propositionalLogic.proposition.connectives.types;

import notification.NotificationEvent;
import notification.Observer;
import org.jetbrains.annotations.NotNull;
import propositionalLogic.cache.proposition.PropertyCache;
import propositionalLogic.proposition.notification.PropertyNotification;
import propositionalLogic.proposition.properties.Property;
import propositionalLogic.cache.proposition.PropositionCache;
import propositionalLogic.proposition.*;
import propositionalLogic.proposition.connectives.ConnectiveBuilder;
import propositionalLogic.proposition.properties.PropertyEvent;
import propositionalLogic.proposition.properties.State;
import propositionalLogic.transformation.normalForm.NormalForm;
import propositionalLogic.transformation.normalForm.NormalFormManager;

import java.util.*;
import java.util.function.Predicate;

public abstract class Connective implements Proposition
{
    private final List<Proposition> propositions = new LinkedList<>();

    protected final ConnectiveBuilder connectiveBuilder;
    protected final PropertyCache propertyCache;
    protected final PropertyNotification propertyNotification;
    protected final NormalFormManager normalFormManager;

    protected Connective(Proposition... propositions)
    {
        // TODO: Verificar o tempo de acesso ao banco de dados e, dependendo, decidir
        //  se acessa o BD ou calcula o valor de um atributo caso ele não esteja em cache.

        this.propositions.addAll(Arrays.asList(propositions));
        this.connectiveBuilder = new ConnectiveBuilder();
        this.propertyNotification = new PropertyNotification(this);
        this.propertyCache = PropositionCache.getInstance().getCache(this);
        this.normalFormManager = new NormalForm(this);

        registerNotifications();
    }

    protected abstract @NotNull String getImpressionTemplate();

    public abstract @NotNull Proposition applyConjunctiveNormalFormOnSelf();

    public abstract @NotNull Proposition applyDisjunctiveNormalFormOnSelf();

    @Override
    public boolean isAtom()
    {
        return false;
    }

    @Override
    public List<Proposition> getChildren()
    {
        return propositions;
    }

    @Override
    public Set<Proposition> getAllChildren()
    {
        if (propertyCache.getPropositionSetCache().containKey(Property.ALL_CHILDREN)) {
            return propertyCache.getPropositionSetCache().get(Property.ALL_CHILDREN);
        }

        Set<Proposition> propositionsFound = new HashSet<>(this.search(p -> true));

        propertyCache.getPropositionSetCache().put(Property.ALL_CHILDREN, propositionsFound);
        return propositionsFound;
    }

    @Override
    public Set<Atom> getAllAtoms()
    {
        if (propertyCache.getAtomSetCache().containKey(Property.ALL_ATOMS)) {
            return propertyCache.getAtomSetCache().get(Property.ALL_ATOMS);
        }

        Set<Atom> atoms = new HashSet<>();
        this.search(Proposition::isAtom).forEach(atom -> atoms.add((Atom) atom));

        propertyCache.getAtomSetCache().put(Property.ALL_ATOMS, atoms);
        return atoms;
    }

    @Override
    public void setValuesToAtoms(Map<Atom, Boolean> atomsValues)
    {
        Set<Atom> atoms = this.getAllAtoms();

        atoms.forEach(atom ->
        {
            if (atomsValues.containsKey(atom)) atom.setValue(atomsValues.get(atom));
        });
    }

    @Override
    public boolean hasOnly(Set<PropositionType> types)
    {
        if (propertyCache.getBooleanCache().containKey(Property.HAS_ONLY_TYPES)) {
            return propertyCache.getBooleanCache().get(Property.HAS_ONLY_TYPES);
        }

        boolean hasOnlyTypes = false;

        for (Proposition proposition : getChildren())
        {
            if (!types.contains(proposition.getType())) break;
            else if (!proposition.isAtom()) {
                hasOnlyTypes = proposition.hasOnly(types);
                break;
            }
        }

        propertyCache.getBooleanCache().put(Property.HAS_ONLY_TYPES, hasOnlyTypes);
        return hasOnlyTypes;
    }

    @Override
    public Map<PropositionType, Integer> countTypes()
    {
        // TODO: Usar AOP para pegar cache?
        if (propertyCache.getTypesCounters().containKey(Property.COUNT_TYPES)) {
            return propertyCache.getTypesCounters().get(Property.COUNT_TYPES);
        }

        Map<PropositionType, Integer> countByType = new HashMap<>();

        countByType.put(this.getType(), 1);

        for (Proposition child : getChildren())
        {
            Map<PropositionType, Integer> childMap = child.countTypes();

            for (Map.Entry<PropositionType, Integer> entry : childMap.entrySet())
            {
                if (countByType.containsKey(entry.getKey())) countByType.put(entry.getKey(), countByType.get(entry.getKey()) + entry.getValue());
                else countByType.put(entry.getKey(), entry.getValue());
            }
        }

        Arrays.stream(PropositionType.values()).forEach(possibleType -> {
            if (!countByType.containsKey(possibleType)) countByType.put(possibleType, 0);
        });

        propertyCache.getTypesCounters().put(Property.COUNT_TYPES, countByType);
        return countByType;
    }

    @Override
    public Set<Proposition> search(Predicate<Proposition> searchFilter)
    {
        // TODO: chamar um módulo especializado em busca?

        Set<Proposition> propositionsFound = new HashSet<>();

        for (Proposition child : this.getChildren())
        {
            if (searchFilter.test(child)) propositionsFound.add(child);
            else propositionsFound.addAll(child.search(searchFilter));
        }

        return propositionsFound;
    }

    @Override
    public NormalFormManager getNormalFormManager()
    {
        return normalFormManager;
    }

    /**
     *  This method register Proposition's notifications and observers. For now,
     *  the current Proposition starts to listen all notifications (VALUE, UPDATED)
     *  from its children. Since its parent's boolean value depends on your own
     *  boolean value and your own boolean value depends on yours children's boolean
     *  value, when receive this notification, it should remove your cache and
     *  notify it to all your parents.
     *
     * */

    private synchronized void registerNotifications()
    {
        for (Proposition child : propositions)
        {
            child.addPropertyObserver(new PropertyEvent(Property.VALUE, State.UPDATED), data -> {
                propertyCache.getBooleanCache().getAndRemove(Property.VALUE);
                propertyNotification.notify(new PropertyEvent(Property.VALUE, State.UPDATED), null);
            });
        }
    }

    @Override
    public <T extends NotificationEvent> void addPropertyObserver(T event, Observer observer) {
        propertyNotification.addObserver(event, observer);
    }

    @Override
    public <T extends NotificationEvent> void removePropertyObserver(T event, Observer observer) {
        propertyNotification.removeObserver(event, observer);
    }

    @Override
    public String toString()
    {
        return String.format(getImpressionTemplate(), propositions.toArray());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(toString());
    }
}
