package solver;

import logic.Move;
import scrambler.Parser;
import java.util.List;

public class SolverResult {
    public final List<Move> moves;
    public final long probes;

    public SolverResult(List<Move> moves, long probes) {
        this.moves = moves;
        this.probes = probes;
    }

    public String getMovesString() {
        return Parser.movesToString(moves);
    }
}