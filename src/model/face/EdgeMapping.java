package model.face;

public class EdgeMapping {
    public final Direction face;      // 目標面，例如 FRONT、UP 等
    public final int index;          // 第幾 row / col
    public final boolean isRow;      // 是否為 row（true = row，false = col）
    public final boolean reversed;   // 是否反轉順序

    public EdgeMapping(Direction face, int index, boolean isRow, boolean reversed) {
        this.face = face;
        this.index = index;
        this.isRow = isRow;
        this.reversed = reversed;
    }
}