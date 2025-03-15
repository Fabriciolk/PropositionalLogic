package propositionalLogic.searchFilter;

import propositionalLogic.proposition.Proposition;
import propositionalLogic.proposition.PropositionType;

import java.util.function.Predicate;

public class AtomOrNotAtom implements Predicate<Proposition>
{
    @Override
    public boolean test(Proposition proposition) {
        return proposition.isAtom() ||
                (proposition.getType() == PropositionType.NOT && proposition.getChildren().get(0).isAtom());
    }
}
