package propositionalLogic.proposition;

import notification.NotificationEvent;
import notification.Observer;
import propositionalLogic.transformation.normalForm.NormalFormManager;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public interface Proposition
{
    boolean value();

    boolean isAtom();

    List<Proposition> getChildren();

    Set<Proposition> getAllChildren();

    Set<Atom> getAllAtoms();

    void setValuesToAtoms(Map<Atom, Boolean> atomsValues);

    PropositionType getType();

    boolean hasOnly(Set<PropositionType> types);

    NormalFormManager getNormalFormManager();

    Map<PropositionType, Integer> countTypes();

    Set<Proposition> search(Predicate<Proposition> searchFilter);

    <T extends NotificationEvent> void addPropertyObserver(T event, Observer observer);

    <T extends NotificationEvent> void removePropertyObserver(T event, Observer observer);
}
