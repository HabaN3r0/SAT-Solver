package sat;
import immutable.EmptyImList;
import immutable.ImList;
import sat.env.Environment;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.PosLiteral;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {

    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     * 
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */

    public static Environment solve(Formula formula) {
        return solve(formula.getClauses(), new Environment());
    }

    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     *
     * @param clauses
     *            formula in conjunctive normal form
     * @param env
     *            assignment of some or all variables in clauses to true or
     *            false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     *         or null if no such environment exists.
     */
    private static Environment solve(ImList<Clause> clauses, Environment env) {
        if (clauses.isEmpty())
            return env;
        int minClauseSize = Integer.MAX_VALUE;

        Clause minClause = new Clause();
        for (Clause c : clauses) {
            // false
            if (c.isEmpty())
                return null;
            // for getting smallest clause
            if (c.size() < minClauseSize) {
                minClauseSize = c.size();
                minClause = c;
            }
        }

        Environment outEnv;
        Literal lit = minClause.chooseLiteral();

        if (minClause.size() == 1) {
            // only one branch-out is needed
            if (lit instanceof PosLiteral) outEnv = solve(substitute(clauses, lit), env.putTrue(lit.getVariable()));
            else outEnv = solve(substitute(clauses, lit), env.putFalse(lit.getVariable()));
        }
        else {
            // both 1 and 0 branch-outs required
            if (lit instanceof PosLiteral) {
                outEnv  = solve(substitute(clauses, lit), env.putTrue(lit.getVariable()));
                if (outEnv == null)
                    outEnv = solve(substitute(clauses, lit.getNegation()), env.putFalse(lit.getVariable()));

            }
            else {
                outEnv  = solve(substitute(clauses, lit), env.putFalse(lit.getVariable()));
                if (outEnv == null)
                    outEnv = solve(substitute(clauses, lit.getNegation()), env.putTrue(lit.getVariable()));
            }
        }
        return outEnv;
    }

    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     *
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses,
                                             Literal l) {
        ImList<Clause> newClauses = new EmptyImList<Clause>();

        for (Clause c : clauses) {
            Clause newClause = c.reduce(l);
            if (newClause != null)
                newClauses = newClauses.add(newClause);
        }
        return newClauses;
    }

//    private static Literal currentLiteral;
//
//    public static Environment solve(Formula formula) {
//        ImList<Clause> clauses = formula.getClauses();
//        // return env if clauses is empty
//        Environment env = new Environment();
//        if (clauses.isEmpty()) {
//            return env;
//        }
//
//        int minClauseSize = Integer.MAX_VALUE;
//        Clause smallestClause = new Clause();
//
//        for (Clause c : clauses) {
//            if (c.size() < minClauseSize) {
//                minClauseSize = c.size();
//                smallestClause = c;
//            }
//            if (c.isEmpty()) return null;
//        }
//
//        return branchOut(smallestClause, clauses, env);
//    }
//
//    /**
//     * Takes a partial assignment of variables to values, and recursively
//     * searches for a complete satisfying assignment.
//     *
//     * @param clauses
//     *            formula in conjunctive normal form
//     * @param env
//     *            assignment of some or all variables in clauses to true or
//     *            false values.
//     * @return an environment for which all the clauses evaluate to Bool.TRUE,
//     *         or null if no such environment exists.
//     */
//    private static Environment solve(ImList<Clause> clauses, Environment env) {
//        int minClauseSize = Integer.MAX_VALUE;
//        Clause smallestClause = new Clause();
//
//        ImList<Clause> newClauses = new EmptyImList<>();
//
//        // this implementation only requires one loop for reduction, finding min, and determining null
//        for (Clause c : clauses) {
//            Clause newClause = c.reduce(currentLiteral);
//
//            if (newClause == null) {
//                continue;
//            }
//            else if (newClause.isEmpty()) {
//                return null;
//            }
//            else {
//                newClauses = newClauses.add(newClause);
//            }
//
//            // determine next clause to use
//
//            if (newClause.size() < minClauseSize) {
//                minClauseSize = newClause.size();
//                smallestClause = newClause;
//            }
//        }
//
//        if (newClauses.isEmpty()) {
//            return env;
//        }
//
//        return branchOut(smallestClause, newClauses, env);
//    }
//
//    /**
//     * Branches the recursions out based on currentLiteral
//     *
//     * @param clause
//     *           the clause to get the literal to base the branching on
//     *
//     * @param clauses
//     *           the clauses that will be passed into solve
//     *
//     * @param env
//     *           the environment that will be passed into solve
//     *
//     * @return an environment for which all the clauses evaluate to Bool.TRUE,
//     *         or null if no such environment exists.
//     */
//
//    private static Environment branchOut(Clause clause, ImList<Clause> clauses, Environment env) {
//        Literal preservedLit = clause.chooseLiteral();
//        currentLiteral = preservedLit;
//
//        Environment outEnv;
//        if (clause.size() == 1) {
//            // only one branch-out is needed
//
//            if (currentLiteral instanceof NegLiteral) outEnv = solve(clauses, env.putFalse(currentLiteral.getVariable()));
//            else outEnv = solve(clauses, env.putTrue(currentLiteral.getVariable()));
//        }
//        else {
//            // both 1 and 0 branch-outs required
//            if (currentLiteral instanceof NegLiteral) {
//                outEnv  = solve(clauses, env.putFalse(currentLiteral.getVariable()));
//                if (outEnv == null) {
//                    currentLiteral = preservedLit.getNegation();
//                    outEnv = solve(clauses, env.putTrue(currentLiteral.getVariable()));
//                }
//            }
//            else {
//                outEnv  = solve(clauses, env.putTrue(currentLiteral.getVariable()));
//                if (outEnv == null) {
//                    currentLiteral = preservedLit.getNegation();
//                    outEnv = solve(clauses, env.putFalse(currentLiteral.getVariable()));
//                }
//            }
//        }
//        return outEnv;
//    }
    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     *
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
//    private static ImList<Clause> substitute(ImList<Clause> clauses,
//            Literal l) {
//        // TODO: implement this.
//        throw new RuntimeException("not yet implemented.");
//    }

}
