package propositionalLogic.rules.equivalence.onSingular;

import propositionalLogic.proposition.Proposition;
import propositionalLogic.rules.RuleOnSingular;

import java.util.LinkedList;
import java.util.List;

public class DoubleNegation extends RuleOnSingular
{
    @Override
    public List<Proposition> applyRule(Proposition p)
    {
        return new LinkedList<>(List.of(
                connectiveBuilder.not(connectiveBuilder.not(p))
        ));
    }
}
