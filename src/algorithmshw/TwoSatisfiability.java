package algorithmshw;

import edu.princeton.cs.algs4.KosarajuSharirSCC;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author Levi Muriuki
 * Problem 4.2.26
 */
public class TwoSatisfiability {
    private SeparateChainingHashST<Character, Boolean> solve(String formula)
    {
       //find no of variables in the formula
        HashSet<Character> variables = new HashSet<>();
        char[] charsInFormula = formula.toCharArray();
        for (char aCharsInFormula : charsInFormula) {
            if (aCharsInFormula != '('
                    && aCharsInFormula != ')'
                    && aCharsInFormula != 'V'
                    && aCharsInFormula != '^'
                    && aCharsInFormula != ' '
                    && aCharsInFormula != '!') {
                variables.add(aCharsInFormula);
            }
        }

        Digraph digraph = new Digraph(variables.size() * 2);

        String[] values = formula.split(" ");

        SeparateChainingHashST<String, Integer> variableToId = new SeparateChainingHashST<>();
        SeparateChainingHashST<Integer, String> idToVariable = new SeparateChainingHashST<>();

        //Get Vertices
        for(int i = 0; i < values.length; i += 2)
        {
            boolean isVar1Negation;
            boolean isVar2Negation;

            //read variables
            String var1;
            String var2;
            String var1Negation;
            String var2Negation;

            if(values[i].charAt(1) == '!')
            {
                var1 = values[i].substring(2, 3);
                isVar1Negation = true;
            } else
            {
                var1 = String.valueOf(values[i].charAt(1));
                isVar1Negation = false;
            }

            var1Negation = "!" + var1;

            i += 2;

            if(values[i].charAt(0) == '!')
            {
                var2 = values[i].substring(1, 2);
                isVar2Negation = true;
            } else
            {
                var2 = String.valueOf(values[i].charAt(0));
                isVar2Negation = false;
            }

            var2Negation = "!" + var1;
            
            //add vars to map 
            if(!variableToId.contains(var1))
            {
                addVarToMap(var1, variableToId, idToVariable);
                addVarToMap(var1Negation, variableToId, idToVariable);
            }
            if(!variableToId.contains(var2))
            {
                addVarToMap(var2, variableToId, idToVariable);
                addVarToMap(var2Negation, variableToId, idToVariable);
            }

            //add edges to implication digraph
            //Map (A v B) to (A -> !B) and (B -> !A)
            //Discrete math stuff ugh...
            int var1Id = variableToId.get(var1);
            int var1NegationId = variableToId.get(var1Negation);
            int var2Id = variableToId.get(var2);
            int var2NegationId = variableToId.get(var2Negation);

            if(!isVar1Negation)
            {
                if(!isVar2Negation)
                {
                    digraph.addEdge(var1Id, var2NegationId);
                    digraph.addEdge(var2Id, var1NegationId);
                } else
                {
                    digraph.addEdge(var1Id, var2Id);
                    digraph.addEdge(var2NegationId, var1NegationId);
                }
            } else
            {
                if(!isVar2Negation)
                {
                    digraph.addEdge(var1NegationId, var2NegationId);
                    digraph.addEdge(var2Id, var1NegationId);
                } else
                {
                    digraph.addEdge(var1NegationId, var2Id);
                    digraph.addEdge(var2NegationId, var1Id);
                }
            }
        }

        //Compute strongly connected components
        KosarajuSharirSCC kscc = new KosarajuSharirSCC(digraph);

        //Check if formula is satisfiable
        if(!isFormulaSatisfiable(digraph, kscc))
            return null;

        //assign vars to true using strongly connected components
        List<Integer>[] scc = (List<Integer>[])new ArrayList[kscc.count()];

        for(int s = 0; s < scc.length; s++)
            scc[s] = new ArrayList<>();

        for(int v = 0; v < digraph.V(); v++)
        {
            int stronglyCCId = kscc.id(v);
            scc[stronglyCCId].add(v);
        }

        SeparateChainingHashST<Character, Boolean> solution = new SeparateChainingHashST<>();

        for(int c = scc.length - 1; c >= 0; c--)
        {
            for(int vId : scc[c])
            {
                String vertexVar = idToVariable.get(vId);
                char var;

                boolean isNegation = vertexVar.charAt(0) == '!';
                if(!isNegation)
                    var = vertexVar.charAt(0);
                else
                    var = vertexVar.charAt(1);

                if(!solution.contains(var))
                {
                    if(!isNegation)
                        solution.put(var, true);
                    else
                        solution.put(var, false);
                }
            }
        }
        return solution;
    }

    private boolean isFormulaSatisfiable(Digraph digraph, KosarajuSharirSCC kscc)
    {
        for(int v = 0; v < digraph.V(); v += 2)
        {
            if(kscc.stronglyConnected(v, v+1))
                return false;
        }
        return true;
    }

    private void addVarToMap(String var, SeparateChainingHashST<String, Integer> variableToId, SeparateChainingHashST<Integer, String> idToVariable)
    {
        int varId = variableToId.size();

        variableToId.put(var, varId);
        idToVariable.put(varId, var);
    }

    public static void main(String[] args)
    {
        TwoSatisfiability ts = new TwoSatisfiability();

        String formula1 = "(A V B) ^ (!A V B)";
        System.out.println("Formula 1: " + formula1);

        SeparateChainingHashST<Character, Boolean> solution1 = ts.solve(formula1);

        if(solution1 == null)
            StdOut.println("Formula not satisfiable");
        else
        {
            for(char var : solution1.keys())
                System.out.println(var + ": " + solution1.get(var));
        }

    }
}
