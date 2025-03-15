package propositionalLogic.rules.inference.onPair;

import propositionalLogic.proposition.Proposition;
import propositionalLogic.rules.RuleOnPair;

import java.util.LinkedList;
import java.util.List;

public class Conjunction extends RuleOnPair
{
    @Override
    public List<Proposition> applyRule(Proposition p1, Proposition p2)
    {
        if (!p1.value() || !p2.value()) return new LinkedList<>();

        return new LinkedList<>(List.of(connectiveBuilder.and(p1, p2)));
    }
}
