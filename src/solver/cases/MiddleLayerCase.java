package solver.cases;

public enum MiddleLayerCase {
    INSERT_RIGHT,      // edge goes right: U R U' R' U' F' U F
    INSERT_LEFT,       // edge goes left: U' F' U F U R U' R'
    NEEDS_EXTRACTION,  // edge in middle layer, wrong place
    ALREADY_SOLVED,    // edge already correct
    NEEDS_POSITIONING  // edge on top, needs U moves to position
}
