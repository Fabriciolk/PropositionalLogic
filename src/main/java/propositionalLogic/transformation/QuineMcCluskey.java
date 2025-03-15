package propositionalLogic.transformation;

import propositionalLogic.proposition.Proposition;
import propositionalLogic.proposition.PropositionType;
import propositionalLogic.proposition.connectives.ConnectiveBuilder;
import propositionalLogic.searchFilter.AtomOrNotAtom;

import java.util.*;

public class QuineMcCluskey
{
    private final ConnectiveBuilder cb = new ConnectiveBuilder();

    public QuineMcCluskey () {}

    public Proposition simplify(Proposition proposition)
    {
        TruthTable truthTable = new TruthTable(proposition);
        List<Proposition> minterms = truthTable.getCombinations(cb::fullAnd, Proposition::value);
        Map<Integer, Set<Proposition>> mintermsByNumberOfNotTypes = getMapByNumberOfNot(minterms);

        Set<Proposition> irreducibleTerms = new HashSet<>();
        findIrreducibleTermsRecursively(mintermsByNumberOfNotTypes, irreducibleTerms);
        PrimeImplicantChart primeImplicantChart = new PrimeImplicantChart(new HashSet<>(minterms), irreducibleTerms);

        return primeImplicantChart.getMinimizedProposition();
    }

    private Map<Integer, Set<Proposition>> getMapByNumberOfNot(List<Proposition> minterms)
    {
        Map<Integer, Set<Proposition>> map = new HashMap<>();

        for (Proposition minterm : minterms)
        {
            int numberOfNotTypes = minterm.countTypes().get(PropositionType.NOT);

            if (!map.containsKey(numberOfNotTypes)) {
                map.put(numberOfNotTypes, new HashSet<>(Set.of(minterm)));
            }
            else {
                map.get(numberOfNotTypes).add(minterm);
            }
        }

        return map;
    }

    private void findIrreducibleTermsRecursively(Map<Integer, Set<Proposition>> currentNotTypesMap, Set<Proposition> irreducibleTermsSetToStore)
    {
        boolean wasAddedAnyPropositionToNewGroup = false;
        addEmptySets(currentNotTypesMap);
        Map<Integer, Set<Proposition>> newGroups = new HashMap<>();

        Set<Proposition> usedPropositions = new HashSet<>();

        for (int i = 0; i < currentNotTypesMap.size() - 1; i++)
        {
            Set<Proposition> notTypeSet1 = currentNotTypesMap.get(i);
            Set<Proposition> notTypeSet2 = currentNotTypesMap.get(i + 1);

            for (Proposition p1 : notTypeSet1)
            {
                for (Proposition p2 : notTypeSet2)
                {
                    Proposition common = extractPropositionInCommon(p1, p2);

                    if (common != null)
                    {
                        usedPropositions.add(p1);
                        usedPropositions.add(p2);

                        if (newGroups.containsKey(common.countTypes().get(PropositionType.NOT)))
                        {
                            newGroups.get(common.countTypes().get(PropositionType.NOT)).add(common);
                        }
                        else {
                            Set<Proposition> newSet = new HashSet<>();
                            newSet.add(common);
                            newGroups.put(common.countTypes().get(PropositionType.NOT), newSet);
                        }

                        wasAddedAnyPropositionToNewGroup = true;
                    }
                }
            }
        }

        for (Map.Entry<Integer, Set<Proposition>> group : currentNotTypesMap.entrySet())
        {
            for (Proposition propInSubgroup : group.getValue())
            {
                if (!usedPropositions.contains(propInSubgroup)) irreducibleTermsSetToStore.add(propInSubgroup);
            }
        }

        if (wasAddedAnyPropositionToNewGroup)
        {
            findIrreducibleTermsRecursively(newGroups, irreducibleTermsSetToStore);
        }
    }

    private void addEmptySets(Map<Integer, Set<Proposition>> map)
    {
        for (int i = 0; i < map.size(); i++) if (!map.containsKey(i)) map.put(i, new HashSet<>());
    }

    // We admit here two propositions are full-ands.
    public Proposition extractPropositionInCommon(Proposition p1, Proposition p2)
    {
        List<Proposition> elementsList1 = new LinkedList<>(p1.search(new AtomOrNotAtom()));
        List<Proposition> elementsList2 = new LinkedList<>(p2.search(new AtomOrNotAtom()));

        Proposition common = null;

        for (int i = 0; i < elementsList1.size(); i++)
        {
            for (int j = 0; j < elementsList2.size(); j++)
            {
                if (elementsList1.get(i).equals(elementsList2.get(j)))
                {
                    if (common == null) common = elementsList1.get(i);
                    else common = cb.and(common, elementsList1.get(i));

                    elementsList1.remove(i);
                    elementsList2.remove(j);

                    i--;

                    break;
                }
            }
        }

        if (
                elementsList1.size() == 1 &&
                elementsList2.size() == 1 &&
                elementsList1.get(0).equals(cb.not(elementsList2.get(0)))
        ) {
            return common;
        }

        return null;
    }
}
