package propositionalLogic.rules;

import propositionalLogic.proposition.connectives.ConnectiveBuilder;
import propositionalLogic.proposition.Proposition;

import java.util.List;

public abstract class RuleOnSingular implements RuleOnMany
{
    protected final ConnectiveBuilder connectiveBuilder = new ConnectiveBuilder();

    public abstract List<Proposition> applyRule(Proposition p);

    @Override
    public List<Proposition> applyRule(Proposition proposition, Proposition... propositions) {
        return applyRule(proposition);
    }
}
