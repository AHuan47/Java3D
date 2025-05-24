package logic;

import model.face.Direction;

public enum Move {
    F(Direction.FRONT, true),
    B(Direction.BACK, true),
    L(Direction.LEFT, true),
    R(Direction.RIGHT, true),
    U(Direction.UP, true),
    D(Direction.DOWN, true),

    F_PRIME(Direction.FRONT, false),
    B_PRIME(Direction.BACK, false),
    L_PRIME(Direction.LEFT, false),
    R_PRIME(Direction.RIGHT, false),
    U_PRIME(Direction.UP, false),
    D_PRIME(Direction.DOWN, false),

    F_2(Direction.FRONT, true),
    B_2(Direction.BACK, true),
    L_2(Direction.LEFT, true),
    R_2(Direction.RIGHT, true),
    U_2(Direction.UP, true),
    D_2(Direction.DOWN, true);


    // storage
    private final Direction direction;
    private final boolean clockwise;
    private final boolean isDoubleMove;

    // constructor
    Move(Direction direction, boolean clockwise) {
        this.direction = direction;
        this.clockwise = clockwise;
        this.isDoubleMove = this.name().endsWith("2");
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isClockwise() {
        return clockwise;
    }

    public boolean isDoubleMove() {
        return isDoubleMove;
    }

    public boolean isSameFace(Move other) {
        return this.direction == other.direction;
    }

    @Override
    public String toString() {
        String base = direction.name().charAt(0) + "";

        if(isDoubleMove) {
            return base + "2";
        }
        else if(!clockwise) {
            return base + "'";
        }
        else {
            return base;
        }
    }

    public static Move[] getMoves() {
        return Move.values();
    }
}
