package solver;

import solver.min2phase.Search;
import solver.min2phase.Tools;
import logic.Move;
import model.Cube;
import scrambler.Parser;
import java.util.List;

public class TwoPhaseSolver {
    private static boolean initialized = false;
    private Search search;

    public TwoPhaseSolver() {
        initializeIfNeeded();
        this.search = new Search();
    }

    private static synchronized void initializeIfNeeded() {
        if (!initialized) {
            Search.init();
            initialized = true;
        }
    }

    public SolverResult solve(Cube cube) {
        String facelets = SolverAdapter.cubeToFaceletString(cube);

        if (Tools.verify(facelets) != 0) {
            throw new IllegalStateException("cube state is invalid");
        }

        SolverOptions options = SolverOptions.defaultOptions();
        String solution = search.solution(facelets, options.maxDepth, options.probeMax, options.probeMin, options.verboseFlags);

        if (solution.startsWith("Error")) {
            throw new RuntimeException("solver failed");
        }

        String cleanSolution = solution.replaceAll("\\([0-9]+f\\)", "").replaceAll("\\.", "").trim();
        List<Move> moves = Parser.parseTxt(cleanSolution);

        return new SolverResult(moves, search.numberOfProbes());
    }
}