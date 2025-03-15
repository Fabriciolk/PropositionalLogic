package propositionalLogic.rules.inference.onPair;

import propositionalLogic.rules.RuleOnPair;
import propositionalLogic.proposition.Proposition;
import propositionalLogic.proposition.PropositionType;

import java.util.LinkedList;
import java.util.List;

public class ResolutionPrinciple extends RuleOnPair
{
    @Override
    public List<Proposition> applyRule(Proposition p1, Proposition p2)
    {
        if (p1.value() && p2.value())
        {
            if (p1.getType() == PropositionType.OR && p2.getType() == PropositionType.OR)
            {
                int[] indexesWhereIsOpposite = findIndexesWhereIsOpposite(p1, p2);

                if (indexesWhereIsOpposite != null)
                {
                    return new LinkedList<>(List.of(
                            connectiveBuilder.or(
                                    p1.getChildren().get(1 - indexesWhereIsOpposite[0]),
                                    p2.getChildren().get(1 - indexesWhereIsOpposite[1]))
                    ));
                }
            }
        }

        return new LinkedList<>();
    }

    public int[] findIndexesWhereIsOpposite(Proposition p1, Proposition p2)
    {
        for (int i = 0; i < 2; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                if (p1.getChildren().get(i).equals(connectiveBuilder.not(p2.getChildren().get(j))))
                {
                    return new int[] {i, j};
                }
            }
        }

        return null;
    }
}
