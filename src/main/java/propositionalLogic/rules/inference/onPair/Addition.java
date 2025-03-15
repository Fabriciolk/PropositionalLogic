package propositionalLogic.rules.inference.onPair;

import propositionalLogic.rules.RuleOnPair;
import propositionalLogic.proposition.Proposition;

import java.util.LinkedList;
import java.util.List;

public class Addition extends RuleOnPair
{
    @Override
    public List<Proposition> applyRule(Proposition p1, Proposition p2)
    {
        if (p1.value() || p2.value())
        {
            return new LinkedList<>(List.of(connectiveBuilder.or(p1, p2)));
        }

        return new LinkedList<>();
    }
}
