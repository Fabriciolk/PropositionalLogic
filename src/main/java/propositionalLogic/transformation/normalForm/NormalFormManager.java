package propositionalLogic.transformation.normalForm;

import propositionalLogic.proposition.Proposition;
import propositionalLogic.transformation.PropositionForm;

public interface NormalFormManager
{
    Proposition getConjunctiveNormalForm();

    Proposition getDisjunctiveNormalForm();

    Proposition getNegativeNormalForm(PropositionForm mainForm);

    Proposition getCanonicalConjunctiveNormalForm();

    Proposition getCanonicalDisjunctiveNormalForm();

    boolean isInConjunctiveNormalForm();

    boolean isInDisjunctiveNormalForm();

    boolean isInNegativeNormalForm();

    boolean isInCanonicalConjunctiveNormalForm();

    boolean isInCanonicalDisjunctiveNormalForm();
}
