import propositionalLogic.proposition.Atom;
import propositionalLogic.proposition.Proposition;
import propositionalLogic.proposition.PropositionType;
import propositionalLogic.proposition.connectives.ConnectiveBuilder;
import propositionalLogic.rules.inference.onSingular.distributive.Distributive;
import propositionalLogic.transformation.QuineMcCluskey;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static propositionalLogic.proposition.connectives.ConnectiveBuilder.*;

public class Main
{
    public static void main(String[] args)
    {
        Atom A = new Atom(true, "Hoje vai chover");
        Atom B = new Atom(false, "Ontem choveu");
        Atom C = new Atom(true, "A temperatura agora é de 20 graus Celsius");
        Atom D = new Atom(false, "A umidade agora é maior que 30%");

        /* *************************************************************************** */

//        Inferencer inferencer = new Inferencer(propositions);
//        EquivalenceMaker equivalenceMaker = new EquivalenceMaker();
//        PropositionalLogic propositionalLogic = new PropositionalLogic(propositions);

        /* *************************************************************************** */

        Proposition myProposition = ifOnlyIf(
                or(A, D),
                ifOnlyIf(B, C)
        );
    }
}
