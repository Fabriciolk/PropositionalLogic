package propositionalLogic.rules.inference.onPair;

import propositionalLogic.rules.RuleOnPair;
import propositionalLogic.proposition.Proposition;
import propositionalLogic.proposition.PropositionType;

import java.util.LinkedList;
import java.util.List;

public class ModusPonens extends RuleOnPair
{
    @Override
    public List<Proposition> applyRule(Proposition p1, Proposition p2)
    {
        if (!p1.value() || !p2.value()) return new LinkedList<>();

        if (p1.getType() == PropositionType.THEN && p1.getChildren().get(0).equals(p2))
        {
            // Here we have p1 as P --> Q and p2 as P
            return new LinkedList<>(List.of(p1.getChildren().get(1)));
        }
        else if (p2.getType() == PropositionType.THEN && p2.getChildren().get(0).equals(p1))
        {
            // Here we have p2 as P --> Q and p1 as P
            return new LinkedList<>(List.of(p2.getChildren().get(1)));
        }

        return new LinkedList<>();
    }
}
