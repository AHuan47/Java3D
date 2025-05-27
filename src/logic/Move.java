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
    private final Direction originalDirection;
    public final Direction direction;
    private final boolean clockwise;
    private final boolean isDoubleMove;

    // constructor
    Move(Direction direction, boolean clockwise) {
        System.out.println("CONSTRUCTOR: Creating " + this.name() + " with direction=" + direction + ", clockwise=" + clockwise);
        this.originalDirection = direction;
        this.direction = switch(direction) {
            case FRONT -> {
                System.out.println("REMAPPING: FRONT -> BACK");
                yield Direction.BACK;
            }
            case BACK -> {
                System.out.println("REMAPPING: BACK -> FRONT");
                yield Direction.FRONT;
            }
            default -> {
                System.out.println("REMAPPING: " + direction + " -> " + direction + " (no change)");
                yield direction;
            }
        };
        System.out.println("STORED: this.direction is now " + this.direction);
        switch (direction){
            case BACK, RIGHT, DOWN -> this.clockwise = !clockwise;
            default -> this.clockwise = clockwise;
        }
        System.out.println("FINAL: this.clockwise is now " + this.clockwise);
        this.isDoubleMove = this.name().endsWith("2");
    }

    public Direction getDirection() {
        System.out.println("getDirection() called on " + this.name() + ", returning: " + direction);
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
        String base = originalDirection.name().charAt(0) + "";

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
