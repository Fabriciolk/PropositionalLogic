package propositionalLogic.proposition.connectives;

import org.jetbrains.annotations.NotNull;
import propositionalLogic.proposition.Proposition;
import propositionalLogic.proposition.PropositionType;
import propositionalLogic.proposition.connectives.types.UnaryConnective;
import propositionalLogic.proposition.properties.Property;
import propositionalLogic.rules.equivalence.onSingular.DeMorganLaw;

import java.util.List;

class Not extends UnaryConnective
{
    public Not(Proposition proposition)
    {
        super(proposition);
    }

    @Override
    public boolean value()
    {
        if (propertyCache.getBooleanCache().containKey(Property.VALUE)) {
            return propertyCache.getBooleanCache().get(Property.VALUE);
        }

        boolean value = !getChildren().get(0).value();
        propertyCache.getBooleanCache().put(Property.VALUE, value);

        return value;
    }

    @Override
    public boolean isAtom()
    {
        return false;
    }

    @Override
    public PropositionType getType()
    {
        return PropositionType.NOT;
    }

    @Override
    protected @NotNull String getImpressionTemplate() {
        return "~(%s)";
    }

    @Override
    @NotNull
    public Proposition applyConjunctiveNormalFormOnSelf() {
        return applyNormalFormOnSelf();
    }

    @Override
    @NotNull
    public Proposition applyDisjunctiveNormalFormOnSelf() {
        return applyNormalFormOnSelf();
    }

    private Proposition applyNormalFormOnSelf()
    {
        if (getChildren().get(0).isAtom()) return this;

        List<Proposition> derivedFromMorganLow = new DeMorganLaw().applyRule(this);

        return derivedFromMorganLow.size() > 0 ? derivedFromMorganLow.get(0) : this;
    }
}
