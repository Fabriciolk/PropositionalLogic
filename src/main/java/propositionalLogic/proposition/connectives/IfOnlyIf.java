package propositionalLogic.proposition.connectives;

import org.jetbrains.annotations.NotNull;
import propositionalLogic.annotation.Connective;
import propositionalLogic.proposition.Proposition;
import propositionalLogic.proposition.PropositionType;
import propositionalLogic.proposition.connectives.types.BinaryConnective;
import propositionalLogic.proposition.properties.Property;

import java.util.ArrayList;
import java.util.List;

import static propositionalLogic.proposition.connectives.ConnectiveBuilder.*;

@Connective
public class IfOnlyIf extends BinaryConnective
{
    public IfOnlyIf(Proposition propositionLeft, Proposition propositionRight)
    {
        super(propositionLeft, propositionRight);
    }

    @Override
    public boolean value()
    {
        if (propertyCache.getBooleanCache().containKey(Property.VALUE)) {
            return propertyCache.getBooleanCache().get(Property.VALUE);
        }

        boolean allTrue = getChildren().get(0).value() && getChildren().get(1).value();
        boolean allFalse = !getChildren().get(0).value() && !getChildren().get(1).value();

        propertyCache.getBooleanCache().put(Property.VALUE, allTrue || allFalse);

        return allTrue || allFalse;
    }

    @Override
    public PropositionType getType()
    {
        return PropositionType.IF_ONLY_IF;
    }

    @Override
    public boolean isOrderImportant()
    {
        return true;
    }

    @Override
    @NotNull
    public Proposition applyConjunctiveNormalFormOnSelf() {
        return and(
                or(
                        not(getChildren().get(0)),
                        getChildren().get(1)),
                or(
                        getChildren().get(0),
                        not(getChildren().get(1)))
        );
    }

    @Override
    @NotNull
    public Proposition applyDisjunctiveNormalFormOnSelf() {
        return or(
                and(
                        getChildren().get(0),
                        getChildren().get(1)),
                and(
                        not(getChildren().get(0)),
                        not(getChildren().get(1)))
        );
    }

    @Override
    protected @NotNull String getImpressionTemplate() {
        return "(%s <-> %s)";
    }
}
