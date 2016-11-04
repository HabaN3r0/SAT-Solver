package sat;

import sat.formula.Literal;

/**
 * Created by ongteckwu on 1/11/16.
 */
public class Edge {
    private final Literal x;
    private final Literal y;

    public Edge(Literal x, Literal y) {
        this.x = x;
        this.y = y;
    }
}
