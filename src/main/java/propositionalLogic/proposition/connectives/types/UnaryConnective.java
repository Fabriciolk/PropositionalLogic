package propositionalLogic.proposition.connectives.types;

import org.jetbrains.annotations.NotNull;
import propositionalLogic.proposition.Proposition;

public abstract class UnaryConnective extends Connective
{
    protected UnaryConnective(@NotNull Proposition proposition)
    {
        super(proposition);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof UnaryConnective)
        {
            return obj.toString().equals(this.toString());
        }

        return false;
    }
}
