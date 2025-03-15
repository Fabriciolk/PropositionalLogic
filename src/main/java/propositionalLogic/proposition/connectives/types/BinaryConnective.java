package propositionalLogic.proposition.connectives.types;

import org.jetbrains.annotations.NotNull;
import propositionalLogic.proposition.Proposition;

public abstract class BinaryConnective extends Connective
{
    protected BinaryConnective(@NotNull Proposition p1, @NotNull Proposition p2)
    {
        super(p1, p2);
    }

    protected abstract boolean isOrderImportant();

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof BinaryConnective)
        {
            if (isOrderImportant())
            {
                return obj.toString().equals(this.toString());
            }

            String firstPossibleFormat = String.format(getImpressionTemplate(), getChildren().get(0), getChildren().get(1));
            String secondPossibleFormat = String.format(getImpressionTemplate(), getChildren().get(1), getChildren().get(0));
            return obj.toString().equals(firstPossibleFormat) || obj.toString().equals(secondPossibleFormat);
        }

        return false;
    }
}
