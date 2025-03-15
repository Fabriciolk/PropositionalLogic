package propositionalLogic.rules.equivalence.onSingular;

import propositionalLogic.proposition.Proposition;
import propositionalLogic.proposition.PropositionType;
import propositionalLogic.rules.RuleOnSingular;

import java.util.LinkedList;
import java.util.List;

public class Commutative extends RuleOnSingular
{
    @Override
    public List<Proposition> applyRule(Proposition p)
    {
        if (p.getType() == PropositionType.OR)
        {
            return new LinkedList<>(List.of(
                    connectiveBuilder.or(p.getChildren().get(1), p.getChildren().get(0))
            ));
        }
        else if (p.getType() == PropositionType.AND)
        {
            return new LinkedList<>(List.of(
                    connectiveBuilder.and(p.getChildren().get(1), p.getChildren().get(0))
            ));
        }

        return null;
    }
}
