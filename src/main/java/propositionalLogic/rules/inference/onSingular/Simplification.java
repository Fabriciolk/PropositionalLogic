package propositionalLogic.rules.inference.onSingular;

import propositionalLogic.proposition.Proposition;
import propositionalLogic.rules.RuleOnSingular;
import propositionalLogic.proposition.PropositionType;

import java.util.LinkedList;
import java.util.List;

public class Simplification extends RuleOnSingular
{
    /**
     *       Rules:
     *
     *       P ^ Q       <---->     P, Q       Obs.: P should be true
     *
     **/

    @Override
    public List<Proposition> applyRule(Proposition p)
    {
        if (!p.value()) return new LinkedList<>();

        if (p.getType() == PropositionType.AND) return new LinkedList<>(List.of(p.getChildren().get(0), p.getChildren().get(1)));

        return new LinkedList<>();
    }
}
