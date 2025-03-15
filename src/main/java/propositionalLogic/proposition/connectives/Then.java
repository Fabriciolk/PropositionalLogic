package propositionalLogic.proposition.connectives;

import org.jetbrains.annotations.NotNull;
import propositionalLogic.proposition.Proposition;
import propositionalLogic.proposition.PropositionType;
import propositionalLogic.proposition.connectives.types.BinaryConnective;
import propositionalLogic.proposition.properties.Property;

import static propositionalLogic.proposition.connectives.ConnectiveBuilder.*;

class Then extends BinaryConnective
{
    public Then(Proposition propositionLeft, Proposition propositionRight)
    {
        super(propositionLeft, propositionRight);
    }

    @Override
    public boolean value()
    {
        if (propertyCache.getBooleanCache().containKey(Property.VALUE)) {
            return propertyCache.getBooleanCache().get(Property.VALUE);
        }

        boolean value = !(getChildren().get(0).value() && !getChildren().get(1).value());
        propertyCache.getBooleanCache().put(Property.VALUE, value);

        return value;
    }

    @Override
    public PropositionType getType()
    {
        return PropositionType.THEN;
    }

    @Override
    public boolean isOrderImportant()
    {
        return true;
    }

    @Override
    protected @NotNull String getImpressionTemplate()
    {
        return "(%s --> %s)";
    }

    @Override
    @NotNull
    public Proposition applyConjunctiveNormalFormOnSelf()
    {
        return applyNormalFormOnSelf();
    }

    @Override
    @NotNull
    public Proposition applyDisjunctiveNormalFormOnSelf()
    {
        return applyNormalFormOnSelf();
    }

    private Proposition applyNormalFormOnSelf()
    {
//        if (getChildren().get(1).getType() == PropositionType.THEN)
//        {
//            return or(
//                        or(
//                            not(getChildren().get(0)),
//                            not(getChildren().get(1).getChildren().get(0))),
//                        getChildren().get(1).getChildren().get(1));
//        }

        return or(
                    not(getChildren().get(0)),
                    getChildren().get(1)
        );
    }
}
