package propositionalLogic.rules.equivalence.onSingular;

import propositionalLogic.proposition.Proposition;
import propositionalLogic.proposition.PropositionType;
import propositionalLogic.rules.RuleOnSingular;

import java.util.LinkedList;
import java.util.List;

public class Equivalence extends RuleOnSingular
{
    @Override
    public List<Proposition> applyRule(Proposition p)
    {
        if (p.getType() == PropositionType.IF_ONLY_IF)
        {
            Proposition left = p.getChildren().get(0);
            Proposition right = p.getChildren().get(1);

            return new LinkedList<>(List.of(
                    connectiveBuilder.and(connectiveBuilder.then(left, right), connectiveBuilder.then(right, left))
            ));
        }

        if (p.getType() == PropositionType.AND)
        {
            Proposition leftOr = p.getChildren().get(0);
            Proposition rightOr = p.getChildren().get(1);

            if (leftOr.getType() == PropositionType.THEN && rightOr.getType() == PropositionType.THEN)
            {
                if (leftOr.getChildren().get(0).equals(rightOr.getChildren().get(1)) &&
                        leftOr.getChildren().get(1).equals(rightOr.getChildren().get(0)))
                {
                    return new LinkedList<>(List.of(
                            connectiveBuilder.ifOnlyIf(leftOr.getChildren().get(0), leftOr.getChildren().get(1))
                    ));
                }
            }
        }

        return null;
    }
}
