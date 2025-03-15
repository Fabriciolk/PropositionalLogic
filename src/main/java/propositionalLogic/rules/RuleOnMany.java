package propositionalLogic.rules;

import propositionalLogic.proposition.Proposition;

import java.util.List;

public interface RuleOnMany
{
    List<Proposition> applyRule(Proposition proposition, Proposition... propositions);
}
