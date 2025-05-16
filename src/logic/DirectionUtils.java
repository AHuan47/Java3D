package logic;

import model.face.Direction;

public class DirectionUtils {

    public static Direction getRotatedDirection(Direction dir, Axis axis, boolean clockwise) {
        return switch (axis) {
            case X -> rotateAroundX(dir, clockwise);
            case Y -> rotateAroundY(dir, clockwise);
            case Z -> rotateAroundZ(dir, clockwise);
        };
    }

    private static Direction rotateAroundX(Direction dir, boolean clockwise) {
        return switch (dir) {
            case UP -> clockwise ? Direction.BACK : Direction.FRONT;
            case DOWN -> clockwise ? Direction.FRONT : Direction.BACK;
            case FRONT -> clockwise ? Direction.UP : Direction.DOWN;
            case BACK -> clockwise ? Direction.DOWN : Direction.UP;
            default -> dir; // LEFT, RIGHT 不變
        };
    }

    private static Direction rotateAroundY(Direction dir, boolean clockwise) {
        return switch (dir) {
            case FRONT -> clockwise ? Direction.RIGHT : Direction.LEFT;
            case BACK -> clockwise ? Direction.LEFT : Direction.RIGHT;
            case LEFT -> clockwise ? Direction.FRONT : Direction.BACK;
            case RIGHT -> clockwise ? Direction.BACK : Direction.FRONT;
            default -> dir; // UP, DOWN 不變
        };
    }

    private static Direction rotateAroundZ(Direction dir, boolean clockwise) {
        return switch (dir) {
            case UP -> clockwise ? Direction.LEFT : Direction.RIGHT;
            case DOWN -> clockwise ? Direction.RIGHT : Direction.LEFT;
            case LEFT -> clockwise ? Direction.DOWN : Direction.UP;
            case RIGHT -> clockwise ? Direction.UP : Direction.DOWN;
            default -> dir; // FRONT, BACK 不變
        };
    }
}