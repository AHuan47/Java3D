package solver;

import javafx.scene.paint.Color;

public class EdgePieceInfo {
    public final Color color1;
    public final Color color2;

    // current positions
    public final int currentX1, currentY1, currentZ1, currentFace1;
    public final int currentX2, currentY2, currentZ2, currentFace2;

    // target positions
    public final int targetX1, targetY1, targetZ1, targetFace1;
    public final int targetX2, targetY2, targetZ2, targetFace2;

    public EdgePieceInfo(Color color1, Color color2,
                         int currentX1, int currentY1, int currentZ1, int currentFace1,
                         int currentX2, int currentY2, int currentZ2, int currentFace2,
                         int targetX1, int targetY1, int targetZ1, int targetFace1,
                         int targetX2, int targetY2, int targetZ2, int targetFace2) {
        this.color1 = color1;
        this.color2 = color2;
        this.currentX1 = currentX1;
        this.currentY1 = currentY1;
        this.currentZ1 = currentZ1;
        this.currentFace1 = currentFace1;
        this.currentX2 = currentX2;
        this.currentY2 = currentY2;
        this.currentZ2 = currentZ2;
        this.currentFace2 = currentFace2;
        this.targetX1 = targetX1;
        this.targetY1 = targetY1;
        this.targetZ1 = targetZ1;
        this.targetFace1 = targetFace1;
        this.targetX2 = targetX2;
        this.targetY2 = targetY2;
        this.targetZ2 = targetZ2;
        this.targetFace2 = targetFace2;
    }

    public boolean isInCorrectPosition() {
        return isCorrectlyOriented() || isInCorrectPositionButFlipped();
    }

    public boolean isCorrectlyOriented() {
        return (currentX1 == targetX1 && currentY1 == targetY1 && currentZ1 == targetZ1 && currentFace1 == targetFace1 &&
                currentX2 == targetX2 && currentY2 == targetY2 && currentZ2 == targetZ2 && currentFace2 == targetFace2);
    }

    public boolean isInCorrectPositionButFlipped() {
        return (currentX1 == targetX2 && currentY1 == targetY2 && currentZ1 == targetZ2 && currentFace1 == targetFace2 &&
                currentX2 == targetX1 && currentY2 == targetY1 && currentZ2 == targetZ1 && currentFace2 == targetFace1);
    }

    public boolean isOnTopLayer() {
        return currentX1 == 0 || currentX2 == 0;
    }

    public boolean isOnBottomLayer() {
        return currentX1 == 2 || currentX2 == 2;
    }

    public boolean isOnMiddleLayer() {
        return currentX1 == 1 || currentX2 == 1;
    }

    public boolean hasWhite() {
        return Color.WHITE.equals(color1) || Color.WHITE.equals(color2);
    }

    public boolean hasYellow() {
        return Color.YELLOW.equals(color1) || Color.YELLOW.equals(color2);
    }

    public Color getNonWhiteColor() {
        if (Color.WHITE.equals(color1)) return color2;
        if (Color.WHITE.equals(color2)) return color1;
        throw new IllegalStateException("This edge doesn't contain white");
    }

    public Color getNonYellowColor() {
        if (Color.YELLOW.equals(color1)) return color2;
        if (Color.YELLOW.equals(color2)) return color1;
        throw new IllegalStateException("This edge doesn't contain yellow");
    }

    @Override
    public String toString() {
        return String.format("Edge[%s-%s] Current:(%d,%d,%d,%d)-(%d,%d,%d,%d) Target:(%d,%d,%d,%d)-(%d,%d,%d,%d)",
                colorToChar(color1), colorToChar(color2),
                currentX1, currentY1, currentZ1, currentFace1, currentX2, currentY2, currentZ2, currentFace2,
                targetX1, targetY1, targetZ1, targetFace1, targetX2, targetY2, targetZ2, targetFace2);
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
