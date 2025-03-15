package propositionalLogic.transformation;

import propositionalLogic.proposition.Atom;
import propositionalLogic.proposition.Proposition;
import propositionalLogic.proposition.connectives.ConnectiveBuilder;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PetrickMethod
{
    private ConnectiveBuilder connectiveBuilder;
    private Map<Proposition, Set<Proposition>> primeImplicantChart;
    private Map<Proposition, Proposition> labeledIrreducibleTermsMap;
    private List<Set<Set<Proposition>>> minimumSumOfProducts;

    public PetrickMethod (Map<Proposition, Set<Proposition>> primeImplicantChart)
    {
        this.primeImplicantChart = primeImplicantChart;
        minimumSumOfProducts = new LinkedList<>();
        connectiveBuilder = new ConnectiveBuilder();
    }

    public Proposition getMinimumSumOfProducts()
    {
        String error = "Unable to get minimum sum of products using Petrick's method";
        String reason = "there are one or more essential terms to remove yet.";

        if (hasEssentialTerms())
        {
            System.out.println(error + " because " + reason);
            return null;
        }

        if (primeImplicantChart.size() == 1) return getFirstIrreducibleTermFound(primeImplicantChart);

        labeledIrreducibleTermsMap = getLabeledIrreducibleTermsMap(primeImplicantChart);
        minimumSumOfProducts = getInitialConjunctives(labeledIrreducibleTermsMap);
        applyDistributiveRule(minimumSumOfProducts);
        Set<Proposition> smallerSet = getSmallerLabeledConjunctive(minimumSumOfProducts);
        Set<Proposition> convertedSmallerSet = convertToOriginalTerms(smallerSet, labeledIrreducibleTermsMap);

        return connectiveBuilder.fullOr(new LinkedList<>(convertedSmallerSet));
    }

    private boolean hasEssentialTerms()
    {
        for (Map.Entry<Proposition, Set<Proposition>> entry : primeImplicantChart.entrySet())
        {
            if (entry.getValue().size() == 1) return true;
        }

        return false;
    }

    private Proposition getFirstIrreducibleTermFound(Map<Proposition, Set<Proposition>> chart)
    {
        for (Map.Entry<Proposition, Set<Proposition>> entry : chart.entrySet()) {
            for (Proposition term : entry.getValue()) return term;
        }
        return null;
    }

    private Map<Proposition, Proposition> getLabeledIrreducibleTermsMap(Map<Proposition, Set<Proposition>> chart)
    {
        Map<Proposition, Proposition> map = new HashMap<>();

        for (Map.Entry<Proposition, Set<Proposition>> entry : chart.entrySet())
        {
            for (Proposition originalIrreducibleTerm : entry.getValue())
            {
                if (!map.containsKey(originalIrreducibleTerm))
                {
                    Proposition labeledTerm = new Atom(true, String.valueOf(originalIrreducibleTerm.hashCode()));
                    map.put(originalIrreducibleTerm, labeledTerm);
                }
            }
        }

        return map;
    }

    private List<Set<Set<Proposition>>> getInitialConjunctives(Map<Proposition, Proposition> labelMap)
    {
        List<Set<Set<Proposition>>> initialConjunctive = new LinkedList<>();

        for (Map.Entry<Proposition, Set<Proposition>> entry : primeImplicantChart.entrySet())
        {
            initialConjunctive.add(
                    entry.getValue().parallelStream()
                    .map((Function<Proposition, Set<Proposition>>) proposition -> new HashSet<>(Set.of(labelMap.get(proposition))))
                    .collect(Collectors.toSet())
            );
        }

        return initialConjunctive;
    }

    private void applyDistributiveRule(List<Set<Set<Proposition>>> proposition)
    {
        while (proposition.size() > 1)
        {
            Set<Set<Proposition>> firstSet = proposition.get(0);
            Set<Set<Proposition>> secondSet = proposition.get(1);
            Set<Set<Proposition>> newSet = new HashSet<>();

            for (Set<Proposition> first : firstSet)
            {
                for (Set<Proposition> second : secondSet)
                {
                    Set<Proposition> distributive = new HashSet<>();
                    distributive.addAll(first);
                    distributive.addAll(second);
                    newSet.add(distributive);
                }
            }

            proposition.remove(firstSet);
            proposition.remove(secondSet);
            proposition.add(newSet);
        }
    }

    private Set<Proposition> getSmallerLabeledConjunctive(List<Set<Set<Proposition>>> proposition)
    {
        Set<Proposition> smaller = new HashSet<>();
        if (proposition.size() > 0) smaller = proposition.get(0).stream().min(Comparator.comparingInt(Set::size)).orElse(new HashSet<>());
        return smaller;
    }

    private Set<Proposition> convertToOriginalTerms(Set<Proposition> termsSet, Map<Proposition, Proposition> labelMap)
    {
        Map<Proposition, Proposition> convertorMap = new HashMap<>();
        labelMap.forEach((key, value) -> convertorMap.put(value, key));

        return termsSet.parallelStream().map(convertorMap::get).collect(Collectors.toSet());
    }
}
