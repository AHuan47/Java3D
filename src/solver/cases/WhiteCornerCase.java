package solver.cases;

public enum WhiteCornerCase {
    WHITE_ON_TOP,      // white sticker facing up
    WHITE_ON_RIGHT,    // white sticker on right face
    WHITE_ON_FRONT,    // white sticker on front face
    NEEDS_EXTRACTION,  // corner in bottom layer, wrong position
    ALREADY_SOLVED     // corner already correct
}
