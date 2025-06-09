package solver;

import javafx.scene.paint.Color;

public class EdgePieceInfo {
    public final Color color1, color2;

    // current positions
    public final int curFace1, curRow1, curCol1;
    public final int curFace2, curRow2, curCol2;

    // target positions
    public final int tgFace1, tgRow1, tgCol1;
    public final int tgFace2, tgRow2, tgCol2;

    public EdgePieceInfo(Color color1, Color color2,
                         int curFace1, int curRow1, int curCol1,
                         int curFace2, int curRow2, int curCol2,
                         int tgFace1, int tgRow1, int tgCol1,
                         int tgFace2, int tgRow2, int tgCol2) {
        this.color1 = color1;
        this.color2 = color2;
        this.curFace1 = curFace1;
        this.curRow1 = curRow1;
        this.curCol1 = curCol1;
        this.curFace2 = curFace2;
        this.curRow2 = curRow2;
        this.curCol2 = curCol2;
        this.tgFace1 = tgFace1;
        this.tgRow1 = tgRow1;
        this.tgCol1 = tgCol1;
        this.tgFace2 = tgFace2;
        this.tgRow2 = tgRow2;
        this.tgCol2 = tgCol2;
    }

    public boolean isInCorrectPosition() {
        return isCorrectlyOriented() || isInCorrectPositionButFlipped();
    }

    public boolean isCorrectlyOriented() {
        return (curFace1 == tgFace1 && curRow1 == tgRow1 && curCol1 == tgCol1 &&
                curFace2 == tgFace2 && curRow2 == tgRow2 && curCol2 == tgCol2);
    }

    public boolean isInCorrectPositionButFlipped() {
        return (curFace1 == tgFace2 && curRow1 == tgRow2 && curCol1 == tgCol2 &&
                curFace2 == tgFace1 && curRow2 == tgRow1 && curCol2 == tgCol1);
    }

    @Override
    public String toString() {
        return String.format("Edge[%s-%s] cur:(%d,%d,%d)-(%d,%d,%d) tg:(%d,%d,%d)-(%d,%d,%d)",
                colorToChar(color1), colorToChar(color2),
                curFace1, curRow1, curCol1, curFace2, curRow2, curCol2,
                tgFace1, tgRow1, tgCol1, tgFace2, tgRow2, tgCol2);
    }

    private String colorToChar(Color color) {
        if (Color.WHITE.equals(color)) return "W";
        if (Color.YELLOW.equals(color)) return "Y";
        if (Color.RED.equals(color)) return "R";
        if (Color.ORANGE.equals(color)) return "O";
        if (Color.BLUE.equals(color)) return "B";
        if (Color.GREEN.equals(color)) return "G";
        return "?";
    }
}