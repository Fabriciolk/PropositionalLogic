package propositionalLogic.rules;

import propositionalLogic.proposition.connectives.ConnectiveBuilder;
import propositionalLogic.proposition.Proposition;

import java.util.List;

public abstract class RuleOnTrio implements RuleOnMany
{
    protected final ConnectiveBuilder connectiveBuilder = new ConnectiveBuilder();

    public abstract List<Proposition> applyRule(Proposition p1, Proposition p2, Proposition p3);

    @Override
    public List<Proposition> applyRule(Proposition proposition, Proposition... propositions) {
        return applyRule(proposition, propositions[0], propositions[1]);
    }
}
