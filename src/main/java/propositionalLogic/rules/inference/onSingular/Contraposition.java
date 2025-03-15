package propositionalLogic.rules.inference.onSingular;

import propositionalLogic.proposition.Proposition;
import propositionalLogic.rules.RuleOnSingular;
import propositionalLogic.proposition.PropositionType;

import java.util.LinkedList;
import java.util.List;

public class Contraposition extends RuleOnSingular
{
    /**
     *       Rules:
     *
     *       P -> Q          <---->    ~(Q) -> ~(P)
     *       ~(Q) -> ~(P)    <---->    P -> Q
     *
     **/

    @Override
    public List<Proposition> applyRule(Proposition p)
    {
        if (p.getType() == PropositionType.THEN)
        {
            return new LinkedList<>(List.of(
                    connectiveBuilder.then(
                            connectiveBuilder.not(
                                    p.getChildren().get(1)
                            ),
                            connectiveBuilder.not(
                                    p.getChildren().get(0))
                    )
                    ));
        }

        return new LinkedList<>();
    }
}
