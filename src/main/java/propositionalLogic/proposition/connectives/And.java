package propositionalLogic.proposition.connectives;

import org.jetbrains.annotations.NotNull;
import propositionalLogic.annotation.Connective;
import propositionalLogic.proposition.Proposition;
import propositionalLogic.proposition.PropositionType;
import propositionalLogic.proposition.connectives.types.BinaryConnective;
import propositionalLogic.proposition.properties.Property;
import propositionalLogic.rules.inference.onSingular.distributive.Distributive;

import java.util.List;

@Connective
class And extends BinaryConnective
{
    public And(Proposition propositionLeft, Proposition propositionRight)
    {
        super(propositionLeft, propositionRight);
    }

    @Override
    public boolean value()
    {
        if (propertyCache.getBooleanCache().containKey(Property.VALUE)) {
            return propertyCache.getBooleanCache().get(Property.VALUE);
        }

        boolean value = getChildren().get(0).value() && getChildren().get(1).value();
        propertyCache.getBooleanCache().put(Property.VALUE, value);

        return value;
    }

    @Override
    public boolean isOrderImportant()
    {
        return false;
    }

    @Override
    @NotNull
    public Proposition applyConjunctiveNormalFormOnSelf() {
        Proposition leftChild = getChildren().get(0);
        Proposition rightChild = getChildren().get(1);

        if (leftChild.equals(rightChild)) return leftChild;
        else return this;
    }

    @Override
    @NotNull
    public Proposition applyDisjunctiveNormalFormOnSelf() {
        List<Proposition> derivedFromDistributive = new Distributive().applyRule(this);
        return derivedFromDistributive.size() > 0 ? derivedFromDistributive.get(0) : this;
    }

    @Override
    public PropositionType getType()
    {
        return PropositionType.AND;
    }

    @Override
    protected @NotNull String getImpressionTemplate()
    {
        return "(%s ^ %s)";
    }
}
