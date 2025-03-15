package propositionalLogic.transformation.normalForm;

import propositionalLogic.proposition.Proposition;
import propositionalLogic.proposition.PropositionType;
import propositionalLogic.proposition.connectives.ConnectiveBuilder;

public class SpecialCaseTransform
{
    private final ConnectiveBuilder connectiveBuilder = new ConnectiveBuilder();
    private final Proposition proposition;

    public SpecialCaseTransform(Proposition proposition)
    {
        this.proposition = proposition;
    }

    public Proposition applyForOperatorAtomAndOpposite()
    {
        if (!(proposition.getType() == PropositionType.AND || proposition.getType() == PropositionType.OR)) return proposition;
        if (!(proposition.getChildren().get(0).isAtom() || proposition.getChildren().get(1).isAtom())) return proposition;

        int atomChildIndex = proposition.getChildren().get(0).isAtom() ? 0 : 1;

        if (!(proposition.getChildren().get(1 - atomChildIndex).getType() == getOppositeType(proposition.getType()))) return proposition;

        return connectiveBuilder.byType(getOppositeType(proposition.getType()),
                connectiveBuilder.byType(proposition.getType(),
                        proposition.getChildren().get(1 - atomChildIndex).getChildren().get(0),
                        proposition.getChildren().get(atomChildIndex)),
                connectiveBuilder.byType(proposition.getType(),
                        proposition.getChildren().get(1 - atomChildIndex).getChildren().get(1),
                        proposition.getChildren().get(atomChildIndex)));
    }

    public Proposition applyForOperatorOppositeAndOperator()
    {
        if (!(proposition.getType() == PropositionType.AND || proposition.getType() == PropositionType.OR)) return proposition;

        int oppositeChildIndex = proposition.getChildren().get(0).getType() == getOppositeType(proposition.getType()) ? 0 : 1;

        if (!(proposition.getChildren().get(oppositeChildIndex).getType() == proposition.getType())) return proposition;
        if (!(proposition.getChildren().get(1 - oppositeChildIndex).getType() == getOppositeType(proposition.getType()))) return proposition;

        return connectiveBuilder.byType(getOppositeType(proposition.getType()),
                connectiveBuilder.byType(proposition.getType(),
                        proposition.getChildren().get(1 - oppositeChildIndex).getChildren().get(0),
                        connectiveBuilder.byType(proposition.getType(),
                                proposition.getChildren().get(1 - oppositeChildIndex).getChildren().get(1),
                                proposition.getChildren().get(oppositeChildIndex).getChildren().get(0))),
                connectiveBuilder.byType(proposition.getType(),
                        proposition.getChildren().get(1 - oppositeChildIndex).getChildren().get(0),
                        connectiveBuilder.byType(proposition.getType(),
                                proposition.getChildren().get(1 - oppositeChildIndex).getChildren().get(1),
                                proposition.getChildren().get(oppositeChildIndex).getChildren().get(1)))
        );
    }

    public Proposition applyForOperatorOppositeAndOpposite()
    {
        if (!(proposition.getType() == PropositionType.AND || proposition.getType() == PropositionType.OR)) return proposition;
        if (!(proposition.getChildren().get(0).getType() == getOppositeType(proposition.getType()) &&
                proposition.getChildren().get(1).getType() == getOppositeType(proposition.getType()))) return proposition;

        return connectiveBuilder.byType(getOppositeType(proposition.getType()),
                connectiveBuilder.byType(getOppositeType(proposition.getType()),
                        connectiveBuilder.byType(proposition.getType(),
                                proposition.getChildren().get(0).getChildren().get(0),
                                proposition.getChildren().get(1).getChildren().get(0)),
                        connectiveBuilder.byType(proposition.getType(),
                                proposition.getChildren().get(0).getChildren().get(0),
                                proposition.getChildren().get(1).getChildren().get(1))),
                connectiveBuilder.byType(getOppositeType(proposition.getType()),
                        connectiveBuilder.byType(proposition.getType(),
                                proposition.getChildren().get(0).getChildren().get(1),
                                proposition.getChildren().get(1).getChildren().get(0)),
                        connectiveBuilder.byType(proposition.getType(),
                                proposition.getChildren().get(0).getChildren().get(1),
                                proposition.getChildren().get(1).getChildren().get(1)))
        );
    }

    public Proposition applyAllCases()
    {
        Proposition transformedProp = applyForOperatorAtomAndOpposite();

        if (!transformedProp.equals(proposition)) return transformedProp;
        else transformedProp = applyForOperatorOppositeAndOperator();

        if (!transformedProp.equals(proposition)) return transformedProp;
        else return applyForOperatorOppositeAndOpposite();
    }

    public PropositionType getOppositeType(PropositionType type)
    {
        assert type == PropositionType.AND || type == PropositionType.OR;

        if (type == PropositionType.AND) return PropositionType.OR;

        return PropositionType.AND;
    }
}
