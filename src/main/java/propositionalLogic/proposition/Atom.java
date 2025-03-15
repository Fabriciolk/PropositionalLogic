package propositionalLogic.proposition;

import notification.NotificationEvent;
import notification.Observer;
import propositionalLogic.proposition.notification.PropertyNotification;
import propositionalLogic.proposition.properties.Property;
import propositionalLogic.proposition.properties.PropertyEvent;
import propositionalLogic.proposition.properties.State;
import propositionalLogic.transformation.normalForm.NormalForm;
import propositionalLogic.transformation.normalForm.NormalFormManager;

import java.util.*;
import java.util.function.Predicate;

public class Atom implements Proposition
{
    private final PropertyNotification propertyNotification;
    private final NormalFormManager normalFormManager;
    private boolean value;
    private String meaning;

    public Atom(boolean value, String meaning)
    {
        this.value = value;
        this.meaning = meaning;
        this.propertyNotification = new PropertyNotification(this);
        this.normalFormManager = new NormalForm(this);
    }

    @Override
    public boolean value()
    {
        return value;
    }

    @Override
    public boolean isAtom()
    {
        return true;
    }

    @Override
    public List<Proposition> getChildren()
    {
        return new LinkedList<>();
    }

    @Override
    public Set<Proposition> getAllChildren()
    {
        return new HashSet<>();
    }

    @Override
    public Set<Proposition> search(Predicate<Proposition> searchFilter) {
        Set<Proposition> propositionsFound = new HashSet<>();

        if (searchFilter.test(this)) propositionsFound.add(this);

        return propositionsFound;
    }

    @Override
    public boolean hasOnly(Set<PropositionType> types) {
        return types.contains(PropositionType.ATOM);
    }

    @Override
    public Set<Atom> getAllAtoms()
    {
        return new HashSet<>(List.of(this));
    }

    @Override
    public void setValuesToAtoms(Map<Atom, Boolean> atomsValues)
    {
        if (atomsValues.containsKey(this)) this.value = atomsValues.get(this);
    }

    @Override
    public Map<PropositionType, Integer> countTypes()
    {
        Map<PropositionType, Integer> countByType = new HashMap<>();
        countByType.put(this.getType(), 1);
        return countByType;
    }

    @Override
    public PropositionType getType()
    {
        return PropositionType.ATOM;
    }

    /**
     *
     *  This method sets the Atom's boolean value. This action will trigger a
     *  notification (VALUE, UPDATED), which is listening by all parents that
     *  have this Atom as child. Those parents then remove their cache value,
     *  since their values depends on their Atom children values. For more
     *  information about it, see {@Connective registerNotifications}
     *
     * */

    public void setValue(boolean value)
    {
        if (value != this.value) {
            this.value = value;
            propertyNotification.notify(new PropertyEvent(Property.VALUE, State.UPDATED), null);
        }
    }

    public void setMeaning(String meaning)
    {
        this.meaning = meaning;
    }

    public String getMeaning()
    {
        return meaning;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(meaning);
    }

    @Override
    public NormalFormManager getNormalFormManager() {
        return normalFormManager;
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
        return meaning;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Atom) return ((Atom) obj).meaning.equals(meaning);
        return false;
    }
}
