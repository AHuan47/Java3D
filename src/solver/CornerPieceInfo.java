package solver;

import javafx.scene.paint.Color;

public class CornerPieceInfo {
    public final Color color1, color2, color3;

    // current positions
    public final int curFace1, curRow1, curCol1;
    public final int curFace2, curRow2, curCol2;
    public final int curFace3, curRow3, curCol3;

    // target positions
    public final int tgFace1, tgRow1, tgCol1;
    public final int tgFace2, tgRow2, tgCol2;
    public final int tgFace3, tgRow3, tgCol3;

    public CornerPieceInfo(Color color1, Color color2, Color color3,
                           int curFace1, int curRow1, int curCol1,
                           int curFace2, int curRow2, int curCol2,
                           int curFace3, int curRow3, int curCol3,
                           int tgFace1, int tgRow1, int tgCol1,
                           int tgFace2, int tgRow2, int tgCol2,
                           int tgFace3, int tgRow3, int tgCol3) {
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.curFace1 = curFace1;
        this.curRow1 = curRow1;
        this.curCol1 = curCol1;
        this.curFace2 = curFace2;
        this.curRow2 = curRow2;
        this.curCol2 = curCol2;
        this.curFace3 = curFace3;
        this.curRow3 = curRow3;
        this.curCol3 = curCol3;
        this.tgFace1 = tgFace1;
        this.tgRow1 = tgRow1;
        this.tgCol1 = tgCol1;
        this.tgFace2 = tgFace2;
        this.tgRow2 = tgRow2;
        this.tgCol2 = tgCol2;
        this.tgFace3 = tgFace3;
        this.tgRow3 = tgRow3;
        this.tgCol3 = tgCol3;
    }

    public boolean isInCorrectPosition() {
        return isOrientation1() || isOrientation2() || isOrientation3();
    }
    // correct orientation
    public boolean isOrientation1() {
        return (curFace1 == tgFace1 && curRow1 == tgRow1 && curCol1 == tgCol1 &&
                curFace2 == tgFace2 && curRow2 == tgRow2 && curCol2 == tgCol2 &&
                curFace3 == tgFace3 && curRow3 == tgRow3 && curCol3 == tgCol3);
    }

    public boolean isOrientation2() {
        return (curFace1 == tgFace2 && curRow1 == tgRow2 && curCol1 == tgCol2 &&
                curFace2 == tgFace3 && curRow2 == tgRow3 && curCol2 == tgCol3 &&
                curFace3 == tgFace1 && curRow3 == tgRow1 && curCol3 == tgCol1);
    }

    public boolean isOrientation3() {
        return (curFace1 == tgFace3 && curRow1 == tgRow3 && curCol1 == tgCol3 &&
                curFace2 == tgFace1 && curRow2 == tgRow1 && curCol2 == tgCol1 &&
                curFace3 == tgFace2 && curRow3 == tgRow2 && curCol3 == tgCol2);
    }

    public boolean isCorrectlyOriented() {
        return isOrientation1();
    }

    public int getOrientation() {
        if (isOrientation1()) return 1;
        if (isOrientation2()) return 2;
        if (isOrientation3()) return 3;
        return -1;
    }

    public boolean hasWhite() {
        return Color.WHITE.equals(color1) || Color.WHITE.equals(color2) || Color.WHITE.equals(color3);
    }

    public boolean hasYellow() {
        return Color.YELLOW.equals(color1) || Color.YELLOW.equals(color2) || Color.YELLOW.equals(color3);
    }

    @Override
    public String toString() {
        return String.format("Corner[%s-%s-%s] Orientation:%d",
                colorToChar(color1), colorToChar(color2), colorToChar(color3), getOrientation());
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
