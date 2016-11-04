package sat;

/*
import static org.junit.Assert.*;

import org.junit.Test;
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import sat.env.*;
import sat.formula.*;

public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();

    static int p; static int cnf;
    static String s;
    static long start = System.currentTimeMillis();



	
	// TODO: add the main method that reads the .cnf file and calls SATSolver.solve to determine the satisfiability
    public static void main(String[] args) throws IOException,  NullPointerException {
        Formula f = readFile("C:/Users/user/Desktop/largeUnsat.cnf");
        long time1 = System.nanoTime();
        System.out.println(SATSolver.solve(f));

        long time2 = System.nanoTime();
        long timeTaken = time2 - time1;
        System.out.println("Time taken " + timeTaken / Math.pow(10, 9) + " s");

    }



    // TODO: add the main method that reads the .cnf file and calls SATSolver.solve to determine the satisfiability
    public static Formula readFile(String fileName) throws IOException,  NullPointerException{
        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);
        Formula formula = new Formula();

        while ((s = br.readLine()) != null) {

            if(s.indexOf('c')==0)
                continue;
            else if (s.indexOf('p')==0) {
                String[] sa = s.split(" ");

                p = Integer.parseInt(sa[sa.length - 2]);
                cnf = Integer.parseInt(sa[sa.length - 1]);}
            else {

                Clause c = new Clause();
                String[] sa = s.trim().split(" ");
                for (String str : sa) {
                    if (str.equals("0")) break;
                    int v = Integer.parseInt(str);
                    Literal m = PosLiteral.make(Integer.toString(Math.abs(v)));
                    if (v < 0) c = c.add(m.getNegation());
                    else c = c.add(m);
                }
                formula = formula.addClause(c);
            }
        }

        System.out.println(System.currentTimeMillis() - start + " read time");
        return formula;
    }


    public void testSolve() {
        Clause c1 = new Clause(a).add(nb);
        Clause c2 = new Clause(a).add(b);
        Clause c3 = new Clause(a);
        Clause c4 = new Clause(b);
        Clause c5 = new Clause(nb);
        Clause c6 = new Clause(c);
        Formula f = new Formula(c3).addClause(c4).and(
                new Formula(c5).or(new Formula(c6)));

        Environment e = SATSolver.solve(f);
        System.out.println(e);


    }
    
    
    public void testSATSolver2(){
    	// (~a)
    	Environment e = SATSolver.solve(makeFm(makeCl(na)));
        System.out.println(e.get(na.getVariable()));
/*
    	assertEquals( Bool.FALSE, e.get(na.getVariable()));
*/    	
    }
    
    private static Formula makeFm(Clause... e) {
        Formula f = new Formula();
        for (Clause c : e) {
            f = f.addClause(c);
        }
        return f;
    }
    
    private static Clause makeCl(Literal... e) {
        Clause c = new Clause();
        for (Literal l : e) {
            c = c.add(l);
        }
        return c;
    }
    
    
    
}