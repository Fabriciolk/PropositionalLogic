package propositionalLogic.rules.inference.onTrio;

import propositionalLogic.proposition.Proposition;
import propositionalLogic.rules.RuleOnTrio;

import java.util.LinkedList;
import java.util.List;

public class Inconsistency extends RuleOnTrio
{
    @Override
    public List<Proposition> applyRule(Proposition p1, Proposition p2, Proposition p3)
    {
        if (p1.value() && p2.value() && p1.equals(connectiveBuilder.not(p2)))
        {
            return new LinkedList<>(List.of(p3));
        }
        else if (p1.value() && p3.value() && p1.equals(connectiveBuilder.not(p3)))
        {
            return new LinkedList<>(List.of(p2));
        }
        else if (p2.value() && p3.value() && p2.equals(connectiveBuilder.not(p3)))
        {
            return new LinkedList<>(List.of(p1));
        }

        return new LinkedList<>();
    }
}
