package propositionalLogic.proposition.connectives;

import org.jetbrains.annotations.NotNull;
import propositionalLogic.proposition.Proposition;
import propositionalLogic.proposition.PropositionType;
import propositionalLogic.proposition.connectives.types.BinaryConnective;
import propositionalLogic.proposition.properties.Property;

import static propositionalLogic.proposition.connectives.ConnectiveBuilder.*;

class Xor extends BinaryConnective
{
    public Xor(Proposition propositionLeft, Proposition propositionRight)
    {
        super(propositionLeft, propositionRight);
    }

    @Override
    public boolean value()
    {
        if (propertyCache.getBooleanCache().containKey(Property.VALUE)) {
            return propertyCache.getBooleanCache().get(Property.VALUE);
        }

        boolean value = getChildren().get(0).value() != getChildren().get(1).value();
        propertyCache.getBooleanCache().put(Property.VALUE, value);

        return value;
    }

    @Override
    public PropositionType getType()
    {
        return PropositionType.XOR;
    }

    @Override
    public boolean isOrderImportant()
    {
        return false;
    }

    @Override
    protected @NotNull String getImpressionTemplate()
    {
        return "(%s ⊕ %s)";
    }

    @Override
    @NotNull
    public Proposition applyConjunctiveNormalFormOnSelf()
    {
        return and(
                    or(not(getChildren().get(0)), not(getChildren().get(1))),
                    or(getChildren().get(0), getChildren().get(1))
        );
    }

    @Override
    @NotNull
    public Proposition applyDisjunctiveNormalFormOnSelf()
    {
        return or(
                    and(getChildren().get(0), not(getChildren().get(1))),
                    and(not(getChildren().get(0)), getChildren().get(1))
        );
    }
}
