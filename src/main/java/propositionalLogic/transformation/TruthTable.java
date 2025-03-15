package propositionalLogic.transformation;

import org.jetbrains.annotations.NotNull;
import propositionalLogic.proposition.Atom;
import propositionalLogic.proposition.Proposition;
import propositionalLogic.proposition.connectives.ConnectiveBuilder;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TruthTable
{
    private final ConnectiveBuilder cb = new ConnectiveBuilder();
    private final List<Atom> atoms;
    private final Proposition proposition;

    // TODO: Corrigir esta classe. Fazê-la notificar?
    public TruthTable(@NotNull Proposition proposition)
    {
        this.proposition = proposition;
        atoms = this.proposition.getAllAtoms().stream().toList();
    }

    public List<Proposition> getCombinations(Function<List<Proposition>, Proposition> transformerForAtomsCombination, Predicate<Proposition> builtPropositionFilter)
    {
        List<List<Atom>> combinations = new LinkedList<>();

        addAllCombinations(0, combinations);

        // Here we access the same object reference on all iterations: 'this.proposition'. In this way, we can't parallelize the stream
        // because we need to change reference's atoms boolean values to check its boolean value entirely, causing concurrency problems.
        return combinations.stream()
                .filter(combination -> {
                    setAtomsValues(this.proposition, combination);
                    return builtPropositionFilter.test(this.proposition);
                })
                .map(atoms -> transformerForAtomsCombination.apply(getRepresentationForm(atoms)))
                .collect(Collectors.toList());
    }

    public List<List<Proposition>> getAllCombinations()
    {
        List<List<Atom>> combinations = new LinkedList<>();

        addAllCombinations(0, combinations);

        return combinations.parallelStream().map(this::getRepresentationForm).collect(Collectors.toList());
    }

    private List<Proposition> getRepresentationForm (List<Atom> atoms) {
        List<Proposition> representationForm = new LinkedList<>();
        for (Atom atom : atoms) representationForm.add(atom.value() ? atom : cb.not(atom));
        return representationForm;
    }

    /**
     *      We can understand the algorithm below thinking on a binary tree so that each level
     *      is an atom and the two children correspond to when that atom is true and when is false.
     *      For example, for atoms P, Q and R:
     *
     *                                           P
     *                             T┌------------┴------------┐F
     *                              Q                         Q
     *                       T┌-----┴-----┐F           T┌-----┴------┐F
     *                        R           R             R            R
     *                    T┌--┴--┐F   T┌--┴--┐F     T┌--┴--┐F    T┌--┴--┐F
     *                    TTT   TTF   TFT   TFF     FTT   FTF    FFT   FFF
     *
     *      The algorithm then reads all single branch and store each combination of atom's values.
     *      In the end, we'll have a list of branches.
     *
     *      In this way, given N atoms, the space complexity is O(N) and time complexity is O(N*2^N).
     *
     * */

    private void addAllCombinations(int indexReading, List<List<Atom>> combinations)
    {
        if (indexReading == atoms.size()) {
            combinations.add(getDeepCopyOf(atoms));
            return;
        }

        Atom atom = atoms.get(indexReading);

        atom.setValue(true);
        addAllCombinations(indexReading + 1, combinations);
        atom.setValue(false);
        addAllCombinations(indexReading + 1, combinations);
    }

    private synchronized void setAtomsValues(Proposition proposition, List<Atom> atoms)
    {
        Map<Atom, Boolean> atomsValues = new HashMap<>();
        for (Atom atom : atoms) atomsValues.put(atom, atom.value());
        proposition.setValuesToAtoms(atomsValues);
    }

    // TODO: Substituir por um mapa<Meaning do atom, valor do atom> pra economizar memoria heap
    private List<Atom> getDeepCopyOf(List<Atom> atoms)
    {
        List<Atom> copyList = new LinkedList<>();
        for (Atom atom : atoms) copyList.add(new Atom(atom.value(), atom.getMeaning()));
        return copyList;
    }
}
