package propositionalLogic.rules.inference;

import propositionalLogic.rules.RuleOnMany;
import propositionalLogic.rules.RuleOnPair;
import propositionalLogic.rules.RuleOnSingular;
import propositionalLogic.rules.RuleOnTrio;
import propositionalLogic.rules.inference.onPair.*;
import propositionalLogic.rules.inference.onSingular.*;
import propositionalLogic.proposition.Proposition;
import propositionalLogic.rules.inference.onSingular.distributive.Distributive;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Inferencer
{
    private final Set<Proposition> propositions;
    private final List<RuleOnSingular> ruleOnSingulars = new LinkedList<>();
    private final List<RuleOnPair> ruleOnPairs = new LinkedList<>();
    private final List<RuleOnTrio> ruleOnTrios = new LinkedList<>();

    public Inferencer(Set<Proposition> initialPropositions)
    {
        this.propositions = initialPropositions;
        setupRules();
    }

    public Set<Proposition> getPropositions() {
        return propositions;
    }

    public void make()
    {
        Set<Proposition> propositionsToAdd = new HashSet<>();
        boolean discoveredOnSingular = false;
        boolean discoveredOnPair = false;
        boolean discoveredOnTrio = false;
        int numberOfPropositions = propositions.size();

        for (RuleOnSingular ruleOnSingular : ruleOnSingulars)
        {
            for (Proposition proposition : propositions)
            {
                propositionsToAdd.addAll(ruleOnSingular.applyRule(proposition));
            }
        }

        propositions.addAll(propositionsToAdd);
        propositionsToAdd.clear();

        if (propositions.size() > numberOfPropositions) discoveredOnSingular = true;
        numberOfPropositions = propositions.size();

        for (RuleOnPair ruleOnPair : ruleOnPairs)
        {
            for (Proposition firstProposition : propositions)
            {
                for (Proposition rightProposition : propositions)
                {
                    if (firstProposition.equals(rightProposition)) continue;
                    propositionsToAdd.addAll(ruleOnPair.applyRule(firstProposition, rightProposition));
                }
            }
        }

        propositions.addAll(propositionsToAdd);
        propositionsToAdd.clear();

        if (propositions.size() > numberOfPropositions) discoveredOnPair = true;
        numberOfPropositions = propositions.size();

        for (RuleOnTrio ruleOnTrio : ruleOnTrios)
        {
            for (Proposition firstProp : propositions)
            {
                for (Proposition secondProp : propositions)
                {
                    for (Proposition thirdProp : propositions)
                    {
                        if (firstProp.equals(secondProp) || firstProp.equals(thirdProp) || secondProp.equals(thirdProp))
                        {
                            continue;
                        }
                        propositionsToAdd.addAll(ruleOnTrio.applyRule(firstProp, secondProp, thirdProp));
                    }
                }
            }
        }

        propositions.addAll(propositionsToAdd);
        propositionsToAdd.clear();

        if (propositions.size() > numberOfPropositions) discoveredOnTrio = true;
        numberOfPropositions = propositions.size();

    }

    public boolean addPropositionToInference(Proposition proposition)
    {
        return propositions.add(proposition);
    }

    public boolean removeProposition(Proposition proposition)
    {
        return propositions.remove(proposition);
    }

    private void deriveOnType(RuleOnMany ruleOnGroup)
    {

    }

    private void setupRules()
    {
        ruleOnSingulars.addAll(List.of(
                new Contraposition(),
                new Distributive(),
                new Exportation(),
                new SelfReference(),
                new Simplification()
        ));
        ruleOnPairs.addAll(List.of(
                new Addition(),
                new Conjunction(),
                new DisjunctiveSyllogism(),
                new HypotheticalSyllogism(),
                new ModusPonens(),
                new ModusTollens(),
                new ResolutionPrinciple()
        ));
        ruleOnTrios.addAll(List.of(
//                new Inconsistency()
        ));
    }


}
