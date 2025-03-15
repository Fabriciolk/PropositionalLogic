package propositionalLogic.rules.inference.onSingular.distributive;

import propositionalLogic.rules.RuleOnSingular;
import propositionalLogic.proposition.Proposition;
import propositionalLogic.proposition.PropositionType;

import java.util.*;

public class Distributive extends RuleOnSingular
{
    /**
    *       Rules:
    *
    *      [reduced form]              [expanded form]
    *       P ^ (Q v R)     <---->    (P ^ Q) v (P ^ R)
    *       P v (Q ^ R)     <---->    (P v Q) ^ (P v R)
    *
    **/

    private Restriction restriction = Restriction.ANY;

    @Override
    public List<Proposition> applyRule(Proposition p)
    {
        Proposition derived = null;

        if ((p.getType() == PropositionType.OR ||
            p.getType() == PropositionType.AND)
                &&
            (p.getChildren().get(0).isAtom() ||
            p.getChildren().get(1).isAtom()))
        {
            PropositionType leftType = p.getChildren().get(0).getType();
            PropositionType rightType = p.getChildren().get(1).getType();

            if (leftType != PropositionType.AND &&
                leftType != PropositionType.OR &&
                rightType != PropositionType.AND &&
                rightType != PropositionType.OR)
            {
                return new LinkedList<>();
            }

            if (leftType == oppositeType(p.getType()))
            {
                if (rightType == oppositeType(p.getType()) &&
                    (restriction == Restriction.ANY || restriction == Restriction.ONLY_REDUCE))
                {
                    // Verify if there is proposition in common to get reduced form.
                    derived = getReducedVersion(p);

                    if (derived == null) derived = getExpandVersion(p, 0);
                }
                else if (restriction == Restriction.ANY || restriction == Restriction.ONLY_EXPAND)
                {
                    derived = getExpandVersion(p, 0);
                }
            }
            else if (rightType == oppositeType(p.getType()) &&
                    (restriction == Restriction.ANY || restriction == Restriction.ONLY_EXPAND))
            {
                derived = getExpandVersion(p, 1);
            }
        }

        if (derived == null) return new LinkedList<>();
        return new LinkedList<>(List.of(derived));
    }

    public void setRestriction(Restriction restriction) {
        this.restriction = restriction;
    }

    private Proposition getExpandVersion(Proposition proposition, int indexBinaryChild)
    {
        return connectiveBuilder.byType(Objects.requireNonNull(oppositeType(proposition.getType())),
                connectiveBuilder.byType(proposition.getType(),
                        proposition.getChildren().get(1 - indexBinaryChild),
                        proposition.getChildren().get(indexBinaryChild).getChildren().get(0)),
                connectiveBuilder.byType(proposition.getType(),
                        proposition.getChildren().get(1 - indexBinaryChild),
                        proposition.getChildren().get(indexBinaryChild).getChildren().get(1))
                );
    }

    private Proposition getReducedVersion(Proposition proposition)
    {
        System.out.println("reduced");
        for (int i = 0; i < 2; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                if (proposition.getChildren().get(0).getChildren().get(i).equals(
                        proposition.getChildren().get(1).getChildren().get(j)))
                {
                    return connectiveBuilder.byType(Objects.requireNonNull(oppositeType(proposition.getType())),
                            proposition.getChildren().get(0).getChildren().get(i),
                            connectiveBuilder.byType(proposition.getType(),
                                    proposition.getChildren().get(0).getChildren().get(1 - i),
                                    proposition.getChildren().get(1).getChildren().get(1 - j)
                            ));
                }
            }
        }

        return null;
    }

    private PropositionType oppositeType(PropositionType type)
    {
        if (type == PropositionType.AND) return PropositionType.OR;
        if (type == PropositionType.OR) return PropositionType.AND;
        return null;
    }

    public Proposition applyRecursively(Proposition proposition)
    {
        assert proposition.hasOnly(new HashSet<>(Set.of(
                PropositionType.AND,
                PropositionType.OR,
                PropositionType.NOT,
                PropositionType.ATOM
        )));

        List<Proposition> derived = applyRule(proposition);
        Proposition parent = derived.size() > 0 ? derived.get(0) : proposition;
        List<Proposition> newChildren = new LinkedList<>();

        parent.getChildren().forEach(child -> newChildren.add(applyRecursively(child)));

        if (proposition.isAtom()) return proposition;
        return connectiveBuilder.byType(parent.getType(), newChildren.toArray(new Proposition[0]));
    }
}
