package propositionalLogic.rules.equivalence.onSingular;

import propositionalLogic.proposition.Proposition;
import propositionalLogic.proposition.PropositionType;
import propositionalLogic.rules.RuleOnSingular;

import java.util.LinkedList;
import java.util.List;

import static propositionalLogic.proposition.connectives.ConnectiveBuilder.*;

public class DeMorganLaw extends RuleOnSingular
{
    @Override
    public List<Proposition> applyRule(Proposition p)
    {
        boolean wasNotType = false;

        if (p.getType() == PropositionType.NOT)
        {
            p = p.getChildren().get(0);
            wasNotType = true;
        }

        if (p.getType() == PropositionType.AND || p.getType() == PropositionType.OR)
        {
            Proposition internalProp = null;

            if (p.getType() == PropositionType.AND)
            {
                internalProp = or(
                                    not(p.getChildren().get(0)),
                                    not(p.getChildren().get(1)));
            }
            else if (p.getType() == PropositionType.OR)
            {
                internalProp = and(
                                    not(p.getChildren().get(0)),
                                    not(p.getChildren().get(1)));
            }

            if (!wasNotType) return new LinkedList<>(List.of(not(internalProp)));

            assert internalProp != null;
            return new LinkedList<>(List.of(internalProp));
        }

        return new LinkedList<>();
    }
}
