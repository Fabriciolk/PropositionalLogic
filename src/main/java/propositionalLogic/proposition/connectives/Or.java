package propositionalLogic.proposition.connectives;

import org.jetbrains.annotations.NotNull;
import propositionalLogic.proposition.Proposition;
import propositionalLogic.proposition.PropositionType;
import propositionalLogic.proposition.connectives.types.BinaryConnective;
import propositionalLogic.proposition.properties.Property;
import propositionalLogic.rules.inference.onSingular.distributive.Distributive;
import propositionalLogic.rules.inference.onSingular.distributive.Restriction;

import java.util.List;

class Or extends BinaryConnective
{
    public Or(Proposition propositionLeft, Proposition propositionRight)
    {
        super(propositionLeft, propositionRight);
    }

    @Override
    public boolean value()
    {
        if (propertyCache.getBooleanCache().containKey(Property.VALUE)) {
            return propertyCache.getBooleanCache().get(Property.VALUE);
        }

        boolean value = getChildren().get(0).value() || getChildren().get(1).value();
        propertyCache.getBooleanCache().put(Property.VALUE, value);

        return value;
    }

    @Override
    public boolean isOrderImportant()
    {
        return false;
    }

    @Override
    public PropositionType getType()
    {
        return PropositionType.OR;
    }

    @Override
    protected @NotNull String getImpressionTemplate()
    {
        return "(%s v %s)";
    }

    @Override
    @NotNull
    public Proposition applyConjunctiveNormalFormOnSelf()
    {
        List<Proposition> derivedFromDistributive = new Distributive().applyRule(this);

        return derivedFromDistributive.size() > 0 ? derivedFromDistributive.get(0) : this;
    }

    @Override
    @NotNull
    public Proposition applyDisjunctiveNormalFormOnSelf()
    {
        return this;
    }
}
