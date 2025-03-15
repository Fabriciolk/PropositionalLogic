package propositionalLogic.rules.inference.onSingular;

import propositionalLogic.proposition.Proposition;
import propositionalLogic.rules.RuleOnSingular;
import propositionalLogic.proposition.PropositionType;

import java.util.LinkedList;
import java.util.List;

public class SelfReference extends RuleOnSingular
{
    /**
     *       Rules:
     *
     *       P        <---->    P ^ P       Obs.: P should be true
     *       P v P    <---->    P           Obs.: P should be true
     *
     **/

    @Override
    public List<Proposition> applyRule(Proposition p)
    {
        if (!p.value()) return new LinkedList<>();

        if (p.getType() == PropositionType.OR && p.getChildren().get(0).equals(p.getChildren().get(1)))
        {
            return new LinkedList<>(List.of(p.getChildren().get(0)));
        }

        return new LinkedList<>(List.of(connectiveBuilder.and(p, p)));
    }
}
