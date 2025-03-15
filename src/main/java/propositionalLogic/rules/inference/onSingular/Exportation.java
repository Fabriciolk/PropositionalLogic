package propositionalLogic.rules.inference.onSingular;

import propositionalLogic.proposition.Proposition;
import propositionalLogic.rules.RuleOnSingular;
import propositionalLogic.proposition.PropositionType;

import java.util.LinkedList;
import java.util.List;

public class Exportation extends RuleOnSingular
{
    /**
     *       Rules:
     *
     *       (P ^ Q) -> R     <---->    P -> (Q -> R)
     *
     **/

    @Override
    public List<Proposition> applyRule(Proposition p)
    {
        if (p.getType() == PropositionType.THEN && p.getChildren().get(0).getType() == PropositionType.AND)
        {
            return new LinkedList<>(List.of
            (
                    connectiveBuilder.then(
                            p.getChildren().get(0).getChildren().get(0),
                            connectiveBuilder.then(
                                    p.getChildren().get(0).getChildren().get(1),
                                    p.getChildren().get(1)
                            )
                    )
            ));
        }

        return new LinkedList<>();
    }
}
