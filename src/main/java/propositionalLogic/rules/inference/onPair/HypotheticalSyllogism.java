package propositionalLogic.rules.inference.onPair;

import propositionalLogic.proposition.Proposition;
import propositionalLogic.rules.RuleOnPair;
import propositionalLogic.proposition.PropositionType;

import java.util.LinkedList;
import java.util.List;

public class HypotheticalSyllogism extends RuleOnPair
{
    @Override
    public List<Proposition> applyRule(Proposition p1, Proposition p2)
    {
        if (!p1.value() || !p2.value()) return new LinkedList<>();

        if (p1.getType() == PropositionType.THEN && p2.getType() == PropositionType.THEN)
        {
            if (p1.getChildren().get(1).equals(p2.getChildren().get(0)))
            {
                return new LinkedList<>(List.of(connectiveBuilder.then(
                        p1.getChildren().get(1),
                        p2.getChildren().get(0))));
            }
            else if (p2.getChildren().get(1).equals(p1.getChildren().get(0)))
            {
                return new LinkedList<>(List.of(connectiveBuilder.then(
                        p2.getChildren().get(1),
                        p1.getChildren().get(0))));
            }
        }

        return new LinkedList<>();
    }
}
