package propositionalLogic.rules.equivalence;

import propositionalLogic.rules.RuleOnSingular;
import propositionalLogic.rules.equivalence.onSingular.*;
import propositionalLogic.proposition.Proposition;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class EquivalenceMaker
{
    private Set<Proposition> propositions;
    private final List<RuleOnSingular> ruleOnSingulars = new LinkedList<>();

    public EquivalenceMaker(Set<Proposition> initialPropositions)
    {
        this();
        this.propositions = initialPropositions;
    }

    public EquivalenceMaker()
    {
        setupRules();
    }

    public void setPropositions(Set<Proposition> propositions) {
        this.propositions = propositions;
    }

    public void make()
    {
        Set<Proposition> propositionsToAdd = new HashSet<>();

        for (RuleOnSingular ruleOnSingular : ruleOnSingulars)
        {
            for (Proposition proposition : propositions)
            {
                Proposition equivalentProposition = ruleOnSingular.applyRule(proposition).get(0);
                if (equivalentProposition != null) propositionsToAdd.add(equivalentProposition);
            }
        }

        propositions.addAll(propositionsToAdd);
    }

    public Set<Proposition> getPropositions() {
        return propositions;
    }

    private void setupRules()
    {
        ruleOnSingulars.addAll(List.of(
                new Associative(),
                new Commutative(),
                new DeMorganLaw(),
                new DoubleNegation(),
                new Equivalence(),
                new Implication()
        ));
    }
}
