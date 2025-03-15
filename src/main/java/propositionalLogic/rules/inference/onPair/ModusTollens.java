package propositionalLogic.rules.inference.onPair;

import propositionalLogic.proposition.Proposition;
import propositionalLogic.rules.RuleOnPair;
import propositionalLogic.proposition.PropositionType;

import java.util.LinkedList;
import java.util.List;

public class ModusTollens extends RuleOnPair
{
    @Override
    public List<Proposition> applyRule(Proposition p1, Proposition p2)
    {
        if (!p1.value() || !p2.value()) return new LinkedList<>();

        if (p1.getType() == PropositionType.THEN && p1.getChildren().get(1).equals(connectiveBuilder.not(p2)))
        {
            // Here we have p1 as P --> Q and p2 as ~Q
            return new LinkedList<>(List.of(connectiveBuilder.not(p1.getChildren().get(0))));
        } else if (p2.getType() == PropositionType.THEN && p2.getChildren().get(1).equals(connectiveBuilder.not(p1)))
        {
            // Here we have p2 as P --> Q and p1 as ~Q
            return new LinkedList<>(List.of(connectiveBuilder.not(p2.getChildren().get(0))));
        }

        return new LinkedList<>();
    }
}
