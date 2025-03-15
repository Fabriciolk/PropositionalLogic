package propositionalLogic;

import propositionalLogic.rules.equivalence.EquivalenceMaker;
import propositionalLogic.rules.inference.Inferencer;
import propositionalLogic.proposition.connectives.*;
import propositionalLogic.proposition.Atom;
import propositionalLogic.proposition.Proposition;

import java.util.*;

public class PropositionalLogic
{
    private final Set<Proposition> propositions;
    private final ConnectiveBuilder connectiveBuilder;
    private final Inferencer inferencer;
    private final EquivalenceMaker equivalenceMaker;
//    private final TrueTable trueTable;

    public PropositionalLogic(Set<Proposition> propositions)
    {
        this.propositions = propositions;
        this.connectiveBuilder = new ConnectiveBuilder();
        this.inferencer = new Inferencer(propositions);
        this.equivalenceMaker = new EquivalenceMaker(propositions);
//        this.trueTable = new TrueTable(propositions.);
    }

    public boolean makeInferenceByTrueTable(Proposition propositionToProve)
    {
        if (propositions.contains(propositionToProve)) return true;
        else
        {

        }

        return propositions.contains(propositionToProve);
    }

    public boolean makeInferenceByTheorem(Proposition propositionToProve)
    {
        if (propositions.contains(propositionToProve)) return true;
        else
        {
            inferencer.make();
            equivalenceMaker.make();
        }

        return propositions.contains(propositionToProve);
    }

    public Proposition getRandomProposition(int numberOfOperators)
    {
        return getRandomProposition(numberOfOperators, new HashSet<>());
    }

    public Proposition getRandomProposition(int numberOfOperators, Set<Atom> atomsToUse)
    {
        if (atomsToUse == null) atomsToUse = new HashSet<>();

        List<Atom> atomsToUseInList = atomsToUse.stream().toList();
        Random random = new Random();

        try {
            Proposition proposition = null;

            for (int i = 0; i < numberOfOperators; i++)
            {
                Proposition atom = atomsToUse.size() > 0 ?
                        atomsToUseInList.get(random.nextInt(atomsToUseInList.size())) :
                        new Atom(true, "P");

                Proposition anotherAtom = atomsToUse.size() > 0 ?
                        atomsToUseInList.get(random.nextInt(atomsToUseInList.size())) :
                        new Atom(true, "P");

                Proposition[] extraParams = new Proposition[1];

                if (proposition == null)
                {
                    if (random.nextInt(2) == 0)
                    {
                        extraParams[0] = anotherAtom;
                        proposition = connectiveBuilder.newRandomConnective(atom, extraParams);
                    }
                    else {
                        proposition = connectiveBuilder.newRandomConnective(atom);
                    }
                }
                else
                {
                    Proposition[] propositionsToUse = new Proposition[] {proposition, anotherAtom};
                    int randomIndex = random.nextInt(propositionsToUse.length);
                    Proposition randomProposition = propositionsToUse[randomIndex];

                    if (random.nextInt(2) == 0) {
                        extraParams[0] = propositionsToUse[1 - randomIndex];
                        proposition = connectiveBuilder.newRandomConnective(randomProposition, extraParams);
                    }
                    else {
                        proposition = connectiveBuilder.newRandomConnective(proposition);
                    }

                }
            }

            return proposition;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
