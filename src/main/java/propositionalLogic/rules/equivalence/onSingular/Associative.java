package propositionalLogic.rules.equivalence.onSingular;

import propositionalLogic.proposition.PropositionType;
import propositionalLogic.proposition.Proposition;
import propositionalLogic.rules.RuleOnSingular;

import java.util.LinkedList;
import java.util.List;

public class Associative extends RuleOnSingular
{
    @Override
    public List<Proposition> applyRule(Proposition p)
    {
        PropositionType type = p.getType();

        if (type == PropositionType.OR || type == PropositionType.AND || type == PropositionType.IF_ONLY_IF)
        {
            Proposition left = p.getChildren().get(0);
            Proposition right = p.getChildren().get(1);

            if (left.getType() == type)
            {
                return new LinkedList<>(List.of(
                        connectiveBuilder.byType(type,
                                left.getChildren().get(0),
                                connectiveBuilder.byType(type, left.getChildren().get(1), right))
                ));
            }
            else if (right.getType() == type)
            {
                return new LinkedList<>(List.of(
                        connectiveBuilder.byType(type,
                                connectiveBuilder.byType(type,
                                        left, right.getChildren().get(0)), right.getChildren().get(1))
                ));
            }
        }

        return null;
    }
}
