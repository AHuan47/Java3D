package solver.cases;

public enum LastLayerCase {
    ALREADY_SOLVED,
    CORNER_SWAP_ADJACENT,    // swap two adjacent corners
    CORNER_SWAP_OPPOSITE,    // swap two opposite corners
    EDGE_CYCLE_CLOCKWISE,    // cycle 3 edges clockwise
    EDGE_CYCLE_COUNTER_CLOCKWISE, // cycle 3 edges counter-clockwise
    EDGE_SWAP_OPPOSITE,      // 4-edge cycle
    EDGES_ONLY,              // only edges need permutation
    CORNERS_ONLY             // only corners need permutation
}
