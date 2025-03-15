package propositionalLogic.transformation;

import propositionalLogic.proposition.Proposition;
import propositionalLogic.proposition.connectives.ConnectiveBuilder;
import propositionalLogic.searchFilter.AtomOrNotAtom;

import java.util.*;

public class PrimeImplicantChart
{
    private final ConnectiveBuilder connectiveBuilder = new ConnectiveBuilder();
    private final Map<Proposition, Set<Proposition>> coverageMap = new HashMap<>();
    private Set<Proposition> essentialPrimeImplicants = new HashSet<>();
    private final Set<Proposition> minterms;
    private final Set<Proposition> primeImplicants;

    public PrimeImplicantChart (Set<Proposition> minterms, Set<Proposition> primeImplicants)
    {
        this.minterms = minterms;
        this.primeImplicants = primeImplicants;
        createCoverageMap();
    }

    public Proposition getMinimizedProposition()
    {
        essentialPrimeImplicants = extractEssentialPrimeImplicants(coverageMap);
        removeMintermsCoveredByEssentialsPrimeImplicants(coverageMap, essentialPrimeImplicants);
        Proposition minimumSumOfProducts = getMinimumSumOfProductsFromRemaining(coverageMap);

        if (minimumSumOfProducts != null)
        {
            return connectiveBuilder.or(connectiveBuilder.fullOr(List.copyOf(essentialPrimeImplicants)), minimumSumOfProducts);
        }
        else {
            return connectiveBuilder.fullOr(List.copyOf(essentialPrimeImplicants));
        }
    }

    private void createCoverageMap()
    {
        for (Proposition minterm : minterms)
        {
            Set<Proposition> coverPrimeImplicants = new HashSet<>();

            for (Proposition primeImplicant : primeImplicants)
            {
                if (isMintermCoveredByPrimeImplicant(minterm, primeImplicant)) coverPrimeImplicants.add(primeImplicant);
            }

            coverageMap.put(minterm, coverPrimeImplicants);
        }
    }

    private boolean isMintermCoveredByPrimeImplicant(Proposition minterm, Proposition primeImplicant)
    {
        Set<Proposition> mintermUnits = minterm.search(new AtomOrNotAtom());
        Set<Proposition> primeImplicantUnits = primeImplicant.search(new AtomOrNotAtom());

        boolean isMintermCovered = true;

        for (Proposition primeImplicantUnit : primeImplicantUnits)
        {
            boolean isUnityCovered = false;

            for (Proposition mintermUnit : mintermUnits)
            {
                if (primeImplicantUnit.equals(mintermUnit)) {
                    isUnityCovered = true;
                    break;
                }
            }

            if (!isUnityCovered) {
                isMintermCovered = false;
                break;
            }
        }

        return isMintermCovered;
    }

    private Set<Proposition> extractEssentialPrimeImplicants(Map<Proposition, Set<Proposition>> coverageMap)
    {
        Set<Proposition> essentialPrimeImplicants = new HashSet<>();

        for (Proposition minterm : minterms)
        {
            if (coverageMap.get(minterm).size() == 1)
            {
                essentialPrimeImplicants.add((Proposition) coverageMap.get(minterm).toArray()[0]);
                coverageMap.remove(minterm);
            }
        }

        return essentialPrimeImplicants;
    }

    private void removeMintermsCoveredByEssentialsPrimeImplicants(Map<Proposition, Set<Proposition>> coverageMap, Set<Proposition> essentialPrimeImplicants)
    {
        for (Proposition essentialIrreducibleTerm : essentialPrimeImplicants)
        {
            for (Proposition minterm : minterms)
            {
                if (coverageMap.containsKey(minterm) && coverageMap.get(minterm).contains(essentialIrreducibleTerm)) coverageMap.remove(minterm);
            }
        }
    }

    private Proposition getMinimumSumOfProductsFromRemaining(Map<Proposition, Set<Proposition>> coverageMap)
    {
        return new PetrickMethod(coverageMap).getMinimumSumOfProducts();
    }
}
