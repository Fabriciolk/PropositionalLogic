package propositionalLogic.rules.inference.onPair;

import propositionalLogic.rules.RuleOnPair;
import propositionalLogic.proposition.Proposition;
import propositionalLogic.proposition.PropositionType;

import java.util.LinkedList;
import java.util.List;

public class DisjunctiveSyllogism extends RuleOnPair
{
    @Override
    public List<Proposition> applyRule(Proposition p1, Proposition p2)
    {
        if (!p1.value() || !p2.value()) return new LinkedList<>();

        Proposition derived = null;

        if (p1.getType() == PropositionType.OR)
        {
            derived = findDerivedProp(p1, p2);
        }
        else if (p2.getType() == PropositionType.OR)
        {
            derived = findDerivedProp(p2, p1);
        }

        if (derived != null) return new LinkedList<>(List.of(derived));

        return new LinkedList<>();
    }

    private Proposition findDerivedProp(Proposition orTypeProposition, Proposition otherOne)
    {
        for (int i = 0; i < orTypeProposition.getChildren().size(); i++)
        {
            if (orTypeProposition.getChildren().get(i).equals(connectiveBuilder.not(otherOne)))
            {
                return orTypeProposition.getChildren().get(1 - i);
            }
        }

        return null;
    }
}
