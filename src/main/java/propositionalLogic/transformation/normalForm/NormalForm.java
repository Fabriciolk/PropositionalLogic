package propositionalLogic.transformation.normalForm;

import propositionalLogic.cache.proposition.PropertyCache;
import propositionalLogic.cache.proposition.PropositionCache;
import propositionalLogic.proposition.Proposition;
import propositionalLogic.proposition.PropositionType;
import propositionalLogic.proposition.connectives.ConnectiveBuilder;
import propositionalLogic.proposition.connectives.types.Connective;
import propositionalLogic.proposition.properties.Property;
import propositionalLogic.searchFilter.AtomOrNotAtom;
import propositionalLogic.transformation.PropositionForm;
import propositionalLogic.transformation.QuineMcCluskey;
import propositionalLogic.transformation.TruthTable;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class NormalForm implements NormalFormManager
{
    private final ConnectiveBuilder connectiveBuilder = new ConnectiveBuilder();
    private final PropertyCache ownerPropertyCache;
    private final Proposition owner;

    public NormalForm(Proposition owner) {
        this.owner = owner;
        this.ownerPropertyCache = PropositionCache.getInstance().getCache(owner);
    }

    @Override
    public Proposition getConjunctiveNormalForm()
    {
        if (owner.isAtom()) return owner;

        Proposition negativeForm = getNegativeNormalForm(PropositionForm.CONJUNCTIVE);
        //        Proposition negativeNormalForm = getNegativeNormalForm(PropositionForm.CONJUNCTIVE);
//        System.out.println(">>> " + negativeNormalForm);
////        System.out.println("=== " + new NormalForm().removeAmbiguous(negativeNormalForm));
//        negativeNormalForm = new NormalForm().removeAmbiguous(negativeNormalForm);
//        return new NormalForm().applySpecialCasesForNormalForm(negativeNormalForm, PropositionForm.CONJUNCTIVE);
//        TruthTable truthTable = new TruthTable(this);
//        List<Proposition> disjunctiveForms = truthTable.getCombinations(connectiveBuilder::fullOr, Proposition::value);
//        Proposition canonicalConjunctive = connectiveBuilder.fullAnd(disjunctiveForms);
//
//        System.out.println(canonicalConjunctive);

//        Proposition negativeNormalForm = new QuineMcCluskey().simplify(this).getNegativeNormalForm(PropositionForm.CONJUNCTIVE);
        return negativeForm;
    }

    @Override
    public Proposition getDisjunctiveNormalForm() {
        if (owner.isAtom()) return owner;
        //        Proposition negativeNormalForm = getNegativeNormalForm(PropositionForm.DISJUNCTIVE);
//        return new NormalForm().applySpecialCasesForNormalForm(negativeNormalForm, PropositionForm.DISJUNCTIVE);
        return new QuineMcCluskey().simplify(owner);
    }

    @Override
    public Proposition getNegativeNormalForm(PropositionForm goalForm)
    {
        if (owner.isAtom()) return owner;

        Proposition currentNormalForm = goalForm == PropositionForm.CONJUNCTIVE ?
                ((Connective) owner).applyConjunctiveNormalFormOnSelf() :
                ((Connective) owner).applyDisjunctiveNormalFormOnSelf();

        Proposition[] transformedChildren = new Proposition[currentNormalForm.getChildren().size()];
        boolean anyChildWasChanged = false;

        for (int i = 0; i < currentNormalForm.getChildren().size(); i++)
        {
            transformedChildren[i] = currentNormalForm.getChildren().get(i).getNormalFormManager().getNegativeNormalForm(goalForm);
            if (!transformedChildren[i].equals(currentNormalForm.getChildren().get(i)))
            {
                anyChildWasChanged = true;
            }
        }

        if (anyChildWasChanged)
        {
            Proposition transformedNormalForm = connectiveBuilder.byType(currentNormalForm.getType(), transformedChildren);
            return transformedNormalForm.getNormalFormManager().getNegativeNormalForm(goalForm);
        }

        else return currentNormalForm;
    }

    @Override
    public Proposition getCanonicalConjunctiveNormalForm()
    {
        if (owner.isAtom()) return owner;

        if (ownerPropertyCache.getPropositionCache().containKey(Property.CANONICAL_CFN)) {
            return ownerPropertyCache.getPropositionCache().get(Property.CANONICAL_CFN);
        }

        TruthTable truthTable = new TruthTable(owner);

        Proposition canonicalCFN = connectiveBuilder.fullAnd(truthTable.getCombinations(connectiveBuilder::fullOr, Proposition::value));
        ownerPropertyCache.getPropositionCache().put(Property.CANONICAL_CFN, canonicalCFN);

        return canonicalCFN;
    }

    @Override
    public Proposition getCanonicalDisjunctiveNormalForm()
    {
        if (owner.isAtom()) return owner;

        if (ownerPropertyCache.getPropositionCache().containKey(Property.CANONICAL_DFN)) {
            return ownerPropertyCache.getPropositionCache().get(Property.CANONICAL_DFN);
        }

        TruthTable truthTable = new TruthTable(owner);

        Proposition canonicalDFN = connectiveBuilder.fullOr(truthTable.getCombinations(connectiveBuilder::fullAnd, Proposition::value));
        ownerPropertyCache.getPropositionCache().put(Property.CANONICAL_DFN, canonicalDFN);

        return canonicalDFN;
    }

    @Override
    public boolean isInConjunctiveNormalForm()
    {
        if (ownerPropertyCache.getBooleanCache().containKey(Property.IS_IN_CNF)) {
            return ownerPropertyCache.getBooleanCache().get(Property.IS_IN_CNF);
        }

        boolean isInCNF;

        switch (owner.getType())
        {
            case AND -> isInCNF = owner.getChildren().get(0).getNormalFormManager().isInConjunctiveNormalForm() &&
                                    owner.getChildren().get(1).getNormalFormManager().isInConjunctiveNormalForm();
            case OR -> {
                if (owner.getChildren().get(0).getType() == PropositionType.AND ||
                    owner.getChildren().get(1).getType() == PropositionType.AND)
                {
                    isInCNF = false;
                    break;
                }

                isInCNF = owner.getChildren().get(0).getNormalFormManager().isInConjunctiveNormalForm() &&
                            owner.getChildren().get(1).getNormalFormManager().isInConjunctiveNormalForm();
            }
            case NOT -> isInCNF = owner.getChildren().get(0).getType() == PropositionType.ATOM;
            default -> isInCNF = false;
        }

        ownerPropertyCache.getBooleanCache().put(Property.IS_IN_CNF, isInCNF);

        return isInCNF;
    }

    @Override
    public boolean isInDisjunctiveNormalForm()
    {
        if (ownerPropertyCache.getBooleanCache().containKey(Property.IS_IN_DNF)) {
            return ownerPropertyCache.getBooleanCache().get(Property.IS_IN_DNF);
        }

        boolean isInDNF;

        switch (owner.getType())
        {
            case AND -> {
                if (owner.getChildren().get(0).getType() == PropositionType.OR ||
                    owner.getChildren().get(0).getType() == PropositionType.OR)
                {
                    isInDNF = false;
                    break;
                }

                isInDNF = owner.getChildren().get(0).getNormalFormManager().isInDisjunctiveNormalForm() &&
                            owner.getChildren().get(1).getNormalFormManager().isInDisjunctiveNormalForm();
            }
            case OR -> isInDNF = owner.getChildren().get(0).getNormalFormManager().isInDisjunctiveNormalForm() &&
                                    owner.getChildren().get(1).getNormalFormManager().isInDisjunctiveNormalForm();
            case NOT -> isInDNF = owner.getChildren().get(0).getType() == PropositionType.ATOM;
            default -> isInDNF = false;
        }

        ownerPropertyCache.getBooleanCache().put(Property.IS_IN_DNF, isInDNF);

        return isInDNF;
    }

    @Override
    public boolean isInNegativeNormalForm()
    {
        if (owner.isAtom()) return true;

        return false;
    }

    @Override
    public boolean isInCanonicalConjunctiveNormalForm()
    {
        if (owner.isAtom()) return true;

        return false;
    }

    @Override
    public boolean isInCanonicalDisjunctiveNormalForm()
    {
        if (owner.isAtom()) return true;

        return false;
    }

    /**
     *      This method should be called inside getConjunctiveNormalForm method, from
     *      Connective abstract class. By this way, we ensure the given proposition
     *      is at least in Negative Normal Form.
     * */

    public Proposition applySpecialCasesForNormalForm(Proposition proposition, PropositionForm form)
    {
        List<Proposition> transformedChildren = new LinkedList<>();

        for (Proposition child : proposition.getChildren())
        {
            if ((form == PropositionForm.CONJUNCTIVE && child.getNormalFormManager().isInConjunctiveNormalForm()) ||
                (form == PropositionForm.DISJUNCTIVE && child.getNormalFormManager().isInDisjunctiveNormalForm()))
            {
                transformedChildren.add(child);
                continue;
            }

            if (shouldSubstituteChild(child, form)) {

                transformedChildren.add(new SpecialCaseTransform(child).applyAllCases());
            }
            else {
                transformedChildren.add(
                        form == PropositionForm.CONJUNCTIVE ?
                        applySpecialCasesForNormalForm(child, form).getNormalFormManager().getConjunctiveNormalForm() :
                        applySpecialCasesForNormalForm(child, form).getNormalFormManager().getDisjunctiveNormalForm()
                );
            }
        }

        return connectiveBuilder.byType(proposition.getType(), transformedChildren.toArray(new Proposition[0]));
    }

    private boolean shouldSubstituteChild(Proposition child, PropositionForm form)
    {
        for (Proposition grandchild : child.getChildren())
        {
            if ((child.getType() == PropositionType.OR && grandchild.getType() == PropositionType.AND && form == PropositionForm.CONJUNCTIVE) ||
                (child.getType() == PropositionType.AND && grandchild.getType() == PropositionType.OR && form == PropositionForm.DISJUNCTIVE)
            ) return true;
        }

        return false;
    }

    private Proposition removeTautologiesAndInconsistency(Proposition proposition)
    {
        if (proposition.isAtom() || (proposition.getType() == PropositionType.NOT && proposition.getChildren().get(0).getType() == PropositionType.ATOM))
        {
            return proposition;
        }

        List<Proposition> newChildren = new LinkedList<>();

        for (Proposition child : proposition.getChildren())
        {
            if (child.isAtom()) newChildren.add(child);
            else if (!isTautologyOrInconsistency(child)) newChildren.add(removeTautologiesAndInconsistency(child));
        }

        if (newChildren.size() == 1) {
            return connectiveBuilder.byType(newChildren.get(0).getType(), newChildren.get(0).getChildren().toArray(new Proposition[0]));
        }
        else {
            return connectiveBuilder.byType(proposition.getType(), newChildren.toArray(new Proposition[0]));
        }
    }

    private boolean isTautologyOrInconsistency(Proposition proposition)
    {
        if (proposition.getType() == PropositionType.NOT && proposition.getChildren().get(0).isAtom()) return false;
        return (proposition.getChildren().get(0).equals(connectiveBuilder.not(proposition.getChildren().get(1)))
                && (proposition.getType() == PropositionType.OR || proposition.getType() == PropositionType.AND))
                ||
                proposition.getChildren().get(0).equals(proposition.getChildren().get(1));

    }

    public Proposition removeAmbiguous(Proposition proposition)
    {
        List<Proposition> newChildren = new LinkedList<>();

        for (Proposition child : proposition.getChildren())
        {
            if (child.getType() == PropositionType.OR)
            {
                Set<Proposition> atoms = child.search(new AtomOrNotAtom());
                for (Proposition atom : Set.copyOf(atoms))
                {
                    if (atoms.contains(connectiveBuilder.not(atom))) {
                        atoms.remove(atom);
                        atoms.remove(connectiveBuilder.not(atom));
                    }
                }
                newChildren.add(connectiveBuilder.fullOr(new LinkedList<>(atoms)));
            }
            else
            {
                newChildren.add(removeAmbiguous(child));
            }
        }

        return connectiveBuilder.byType(proposition.getType(), newChildren.toArray(new Proposition[0]));
    }
}
