package propositionalLogic.proposition.connectives;

import jakarta.json.JsonObject;
import org.reflections.Reflections;
import propositionalLogic.cache.proposition.PropositionCache;
import propositionalLogic.proposition.Atom;
import propositionalLogic.proposition.Proposition;
import propositionalLogic.proposition.PropositionType;
import propositionalLogic.proposition.connectives.types.Connective;

import java.lang.reflect.Constructor;
import java.util.*;

public class ConnectiveBuilder
{
    private static final Map<Integer, List<Class<? extends Connective>>> connectivesListByType = new HashMap<>();
    private static final Map<PropositionType, Constructor<?>> constructorByType = new HashMap<>();
    private final Random random = new Random();

    public ConnectiveBuilder()
    {
        if (connectivesListByType.isEmpty()) setupConnectiveTypesClasses();
    }

    /*******************************************************
     *
     *
     *         All types of connectives supported.
     *
     *
     *******************************************************/

    public static Proposition or(Proposition p1, Proposition p2)
    {
        return new Or(p1, p2);
    }

    public static Proposition xor(Proposition p1, Proposition p2)
    {
        return new Xor(p1, p2);
    }

    public static Proposition and(Proposition p1, Proposition p2)
    {
        return new And(p1, p2);
    }

    public static Proposition then(Proposition leftProposition, Proposition rightProposition)
    {
        return new Then(leftProposition, rightProposition);
    }

    public static Proposition ifOnlyIf(Proposition leftProposition, Proposition rightProposition)
    {
        return new IfOnlyIf(leftProposition, rightProposition);
    }

    public static Proposition not(Proposition proposition)
    {
        if (proposition instanceof Not) return proposition.getChildren().get(0);
        return new Not(proposition);
    }

    /*******************************************************
     *
     *
     *       Additional methods to build a Proposition
     *
     *
     *******************************************************/

    public Proposition fullAnd(List<Proposition> propositions)
    {
        if (propositions.size() < 2) {
            if (propositions.size() == 1) return propositions.get(0);
            else return null;
        }
        return fullConnective(PropositionType.AND, propositions, propositions.get(0), propositions.get(1));
    }

    public Proposition fullOr(List<Proposition> propositions)
    {
        if (propositions.size() < 2) {
            if (propositions.size() == 1) return propositions.get(0);
            else return null;
        }
        return fullConnective(PropositionType.OR, propositions, propositions.get(0), propositions.get(1));
    }

    private Proposition fullConnective(PropositionType type, List<Proposition> propositionsToInclude, Proposition... initialPropositions)
    {
        Proposition fullProposition = byType(type, initialPropositions);

        for (int i = initialPropositions.length; i < propositionsToInclude.size(); i++)
        {
            fullProposition = byType(type, fullProposition, propositionsToInclude.get(i));
        }

        return fullProposition;
    }

    public Proposition getUniqueDeepCopy(Proposition proposition, boolean enableCache)
    {
        List<Proposition> copy = new LinkedList<>();
        deepCopyRecursive(proposition, copy);

        for (Atom atom : copy.get(0).getAllAtoms()) atom.setMeaning(atom.getMeaning().split("___")[0]);
        if (enableCache) PropositionCache.registerProposition(copy.get(0));

        return copy.get(0);
    }

    public Proposition fromString(JsonObject proposition)
    {
        return null;
    }

    public Proposition byType(PropositionType propositionType, Proposition... propositions)
    {
        if (propositionType == PropositionType.ATOM) {
            if (propositions[0].getType() == PropositionType.ATOM) return propositions[0];
            else return null;
        }

        if (constructorByType.containsKey(propositionType)) {
            try {
                return (Proposition) constructorByType.get(propositionType).newInstance(
                        Arrays.stream(propositions).toList().subList(0, constructorByType.get(propositionType).getParameterCount()).toArray()
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (Map.Entry<Integer, List<Class<? extends Connective>>> entry : connectivesListByType.entrySet())
        {
            for (Class<? extends Connective> cls : entry.getValue())
            {
                // If we have propositions enough as arguments to create a new instance of class 'cls'
                if (propositions.length >= cls.getConstructors()[0].getParameterCount())
                {
                    // Get only arguments enough to create the new instance
                    List<Proposition> argumentsToUse = Arrays.stream(propositions).toList().subList(0, cls.getConstructors()[0].getParameterCount());

                    try {
                        Proposition newProp = (Proposition) cls.getConstructors()[0].newInstance(argumentsToUse.toArray());

                        // Verify if we created an instance that is a type of required type.
                        if (newProp.getType() == propositionType) {
                            constructorByType.put(propositionType, cls.getConstructors()[0]);
                            return newProp;
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return null;
    }

    public Proposition newRandomConnective(Proposition proposition, Proposition... propositions)
    {
        List<Class<? extends Connective>> connectiveClasses = connectivesListByType.get(
                1 + Math.min(propositions.length, getMaximumParametersFromConnectiveTypes() - 1)
        );
        Class<? extends Connective> clsToUse = connectiveClasses.get(random.nextInt(connectiveClasses.size()));

        try {
            List<Proposition> allArgumentsReceived = new LinkedList<>();
            allArgumentsReceived.add(proposition);
            allArgumentsReceived.addAll(List.of(propositions));

            List<Proposition> allArgumentsToUse = allArgumentsReceived.subList(0, clsToUse.getConstructors()[0].getParameterCount());
            return (Proposition) clsToUse.getConstructors()[0].newInstance(allArgumentsToUse.toArray());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /*******************************************************
     *
     *
     *                  Private methods
     *
     *
     *******************************************************/

    private void setupConnectiveTypesClasses()
    {
        String connectivePackageName = Connective.class.getPackageName();
        String connectiveParentPackageName = connectivePackageName.substring(0, connectivePackageName.lastIndexOf('.'));

        Reflections reflections = new Reflections(connectiveParentPackageName);

        for (Class<? extends Connective> connectiveType : reflections.getSubTypesOf(Connective.class))
        {
            if (connectiveType.getPackageName().equals(connectiveParentPackageName))
            {
                int numberOfParameters = connectiveType.getConstructors()[0].getParameterCount();

                if (connectivesListByType.containsKey(numberOfParameters)) connectivesListByType.get(numberOfParameters).add(connectiveType);
                else connectivesListByType.put(numberOfParameters, new LinkedList<>(List.of(connectiveType)));
            }
        }
    }

    private int getMaximumParametersFromConnectiveTypes()
    {
        return connectivesListByType.keySet().stream().max(Comparator.comparingInt(o -> o)).orElse(0);
    }

    private void deepCopyRecursive(Proposition originalProp, List<Proposition> buildingProp)
    {
        List<Proposition> childrenCopy = new LinkedList<>();

        for (Proposition child : originalProp.getChildren())
        {
            if (child.isAtom()) childrenCopy.add(new Atom(child.value(), ((Atom) child).getMeaning() + "___"));
            else {
                deepCopyRecursive(child, buildingProp);
                childrenCopy.add(buildingProp.get(0));
            }
        }

        if (buildingProp.size() == 0) {
            buildingProp.add(byType(originalProp.getType(), childrenCopy.toArray(new Proposition[0])));
        }
        else {
            Proposition current = buildingProp.remove(0);
            buildingProp.add(byType(current.getType(), childrenCopy.toArray(new Proposition[0])));
        }
        PropositionCache.unregisterProposition(buildingProp.get(0));
    }
}
