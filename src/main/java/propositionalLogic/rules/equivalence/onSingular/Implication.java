package propositionalLogic.rules.equivalence.onSingular;

import propositionalLogic.proposition.Proposition;
import propositionalLogic.proposition.PropositionType;
import propositionalLogic.rules.RuleOnSingular;

import java.util.LinkedList;
import java.util.List;

public class Implication extends RuleOnSingular
{
    @Override
    public List<Proposition> applyRule(Proposition p)
    {
        if (p.getType() == PropositionType.THEN)
        {
            return new LinkedList<>(List.of(
                    connectiveBuilder.or(connectiveBuilder.not(p.getChildren().get(0)), p.getChildren().get(1))
            ));
        }

        if (p.getType() == PropositionType.OR)
        {
            return new LinkedList<>(List.of(
                    connectiveBuilder.then(connectiveBuilder.not(p.getChildren().get(0)), p.getChildren().get(1))
            ));
        }

        return null;
    }
}
