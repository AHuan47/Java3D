package model.face;

public class RotationMapping {
    public final Direction center;        // 要旋轉的中心面（或中層）
    public final boolean clockwise;       // 是否為順時針
    public final EdgeMapping[] edges;     // 順時針方向的四個邊（或逆時針依方向決定）

    public RotationMapping(Direction center, boolean clockwise, EdgeMapping[] edges) {
        this.center = center;
        this.clockwise = clockwise;
        this.edges = edges;
    }
}