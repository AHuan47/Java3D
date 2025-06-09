package solver.cases;

public enum YellowCornerCase {
    ALL_CORRECT,           // all 4 corners oriented
    ONE_CORRECT,           // exactly 1 corner oriented
    TWO_CORRECT,           // exactly 2 corners oriented
    NONE_CORRECT,          // no corners oriented
    THREE_CORRECT_INVALID  // invalid state
}
