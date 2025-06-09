package solver;

import javafx.scene.paint.Color;

public class CornerPieceInfo {
    public final Color color1;
    public final Color color2;
    public final Color color3;

    // current positions
    public final int currentX1, currentY1, currentZ1, currentFace1;
    public final int currentX2, currentY2, currentZ2, currentFace2;
    public final int currentX3, currentY3, currentZ3, currentFace3;

    // target positions
    public final int targetX1, targetY1, targetZ1, targetFace1;
    public final int targetX2, targetY2, targetZ2, targetFace2;
    public final int targetX3, targetY3, targetZ3, targetFace3;

    public CornerPieceInfo(Color color1, Color color2, Color color3,
                           int currentX1, int currentY1, int currentZ1, int currentFace1,
                           int currentX2, int currentY2, int currentZ2, int currentFace2,
                           int currentX3, int currentY3, int currentZ3, int currentFace3,
                           int targetX1, int targetY1, int targetZ1, int targetFace1,
                           int targetX2, int targetY2, int targetZ2, int targetFace2,
                           int targetX3, int targetY3, int targetZ3, int targetFace3) {
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.currentX1 = currentX1;
        this.currentY1 = currentY1;
        this.currentZ1 = currentZ1;
        this.currentFace1 = currentFace1;
        this.currentX2 = currentX2;
        this.currentY2 = currentY2;
        this.currentZ2 = currentZ2;
        this.currentFace2 = currentFace2;
        this.currentX3 = currentX3;
        this.currentY3 = currentY3;
        this.currentZ3 = currentZ3;
        this.currentFace3 = currentFace3;
        this.targetX1 = targetX1;
        this.targetY1 = targetY1;
        this.targetZ1 = targetZ1;
        this.targetFace1 = targetFace1;
        this.targetX2 = targetX2;
        this.targetY2 = targetY2;
        this.targetZ2 = targetZ2;
        this.targetFace2 = targetFace2;
        this.targetX3 = targetX3;
        this.targetY3 = targetY3;
        this.targetZ3 = targetZ3;
        this.targetFace3 = targetFace3;
    }

    public boolean isInCorrectPosition() {
        return isOrientation1() || isOrientation2() || isOrientation3();
    }

    // color1->target1, color2->target2, color3->target3
    public boolean isOrientation1() {
        return (currentX1 == targetX1 && currentY1 == targetY1 && currentZ1 == targetZ1 && currentFace1 == targetFace1 &&
                currentX2 == targetX2 && currentY2 == targetY2 && currentZ2 == targetZ2 && currentFace2 == targetFace2 &&
                currentX3 == targetX3 && currentY3 == targetY3 && currentZ3 == targetZ3 && currentFace3 == targetFace3);
    }

    // color1->target2, color2->target3, color3->target1
    public boolean isOrientation2() {
        return (currentX1 == targetX2 && currentY1 == targetY2 && currentZ1 == targetZ2 && currentFace1 == targetFace2 &&
                currentX2 == targetX3 && currentY2 == targetY3 && currentZ2 == targetZ3 && currentFace2 == targetFace3 &&
                currentX3 == targetX1 && currentY3 == targetY1 && currentZ3 == targetZ1 && currentFace3 == targetFace1);
    }

    // color1->target3, color2->target1, color3->target2
    public boolean isOrientation3() {
        return (currentX1 == targetX3 && currentY1 == targetY3 && currentZ1 == targetZ3 && currentFace1 == targetFace3 &&
                currentX2 == targetX1 && currentY2 == targetY1 && currentZ2 == targetZ1 && currentFace2 == targetFace1 &&
                currentX3 == targetX2 && currentY3 == targetY2 && currentZ3 == targetZ2 && currentFace3 == targetFace2);
    }

    public boolean isCorrectlyOriented() {
        return isOrientation1();
    }

    // returns 1, 2, 3, or -1 if not in correct position
    public int getOrientation() {
        if (isOrientation1()) return 1;
        if (isOrientation2()) return 2;
        if (isOrientation3()) return 3;
        return -1;
    }

    public boolean isOnTopLayer() {
        return currentX1 == 0 || currentX2 == 0 || currentX3 == 0;
    }

    public boolean isOnBottomLayer() {
        return currentX1 == 2 || currentX2 == 2 || currentX3 == 2;
    }

    public boolean hasWhite() {
        return Color.WHITE.equals(color1) || Color.WHITE.equals(color2) || Color.WHITE.equals(color3);
    }

    public boolean hasYellow() {
        return Color.YELLOW.equals(color1) || Color.YELLOW.equals(color2) || Color.YELLOW.equals(color3);
    }

    public int[] getWhitePosition() {
        if (Color.WHITE.equals(color1)) return new int[]{currentX1, currentY1, currentZ1, currentFace1};
        if (Color.WHITE.equals(color2)) return new int[]{currentX2, currentY2, currentZ2, currentFace2};
        if (Color.WHITE.equals(color3)) return new int[]{currentX3, currentY3, currentZ3, currentFace3};
        throw new IllegalStateException("This corner doesn't contain white");
    }

    public int[] getYellowPosition() {
        if (Color.YELLOW.equals(color1)) return new int[]{currentX1, currentY1, currentZ1, currentFace1};
        if (Color.YELLOW.equals(color2)) return new int[]{currentX2, currentY2, currentZ2, currentFace2};
        if (Color.YELLOW.equals(color3)) return new int[]{currentX3, currentY3, currentZ3, currentFace3};
        throw new IllegalStateException("This corner doesn't contain yellow");
    }

    public Color[] getNonWhiteColors() {
        if (Color.WHITE.equals(color1)) return new Color[]{color2, color3};
        if (Color.WHITE.equals(color2)) return new Color[]{color1, color3};
        if (Color.WHITE.equals(color3)) return new Color[]{color1, color2};
        throw new IllegalStateException("This corner doesn't contain white");
    }

    // UP_FACE = 0
    public boolean isWhiteFacingUp() {
        return hasWhite() && getWhitePosition()[3] == 0;
    }

    public boolean isYellowFacingUp() {
        return hasYellow() && getYellowPosition()[3] == 0;
    }

    @Override
    public String toString() {
        return String.format("Corner[%s-%s-%s] Orientation:%d Position:(%d,%d,%d)",
                colorToChar(color1), colorToChar(color2), colorToChar(color3),
                getOrientation(), currentX1, currentY1, currentZ1);
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
