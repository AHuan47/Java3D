package solver;

import solver.min2phase.Search;

public class SolverOptions {
    public final int maxDepth;
    public final long probeMax;
    public final long probeMin;
    public final int verboseFlags;

    private SolverOptions(int maxDepth, long probeMax, long probeMin, int verboseFlags) {
        this.maxDepth = maxDepth;
        this.probeMax = probeMax;
        this.probeMin = probeMin;
        this.verboseFlags = verboseFlags;
    }

    public static SolverOptions defaultOptions() {
        return new SolverOptions(21, 100_000, 0, 0);
    }
}