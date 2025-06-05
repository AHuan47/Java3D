package model.face;
import javafx.scene.paint.Color;
import logic.Axis;
import model.Cubie;
import java.util.Map;

public class FaceUtils {

    public static Color[] getEdge(Face face, int index, boolean isRow) {
        Color[][] tiles = face.getTiles();
        Color[] edge = new Color[3];

        for (int i = 0; i < 3; i++) {
            edge[i] = isRow ? tiles[index][i] : tiles[i][index];
        }
        return edge;
    }

    public static void setEdge(Face face, int index, boolean isRow, Color[] values) {
        Color[][] tiles = face.getTiles();

        for (int i = 0; i < 3; i++) {
            if (isRow) {
                tiles[index][i] = values[i];
            } else {
                tiles[i][index] = values[i];
            }
        }
    }

    public static void reverse(Color[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            Color temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
    }

    public static Face getFace(Direction direction, Map<Direction, Face> faceMap) {
        return faceMap.get(direction);
    }

    public static Direction getRotationFace(Cubie cubie, Axis axis) {

        int x = (int)cubie.getTranslateX();
        int y = (int)cubie.getTranslateY();;
        int z = (int)cubie.getTranslateZ();;

        return switch (axis) {
            case X -> {
                if (x == -50) yield Direction.LEFT;
                else if (x == 50) yield Direction.RIGHT;
                else yield Direction.MIDDLEY;
            }
            case Y -> {
                if (y == -50) yield Direction.UP;
                else if (y == 50) yield Direction.DOWN;
                else yield Direction.MIDDLEX;
            }
            case Z -> {
                if (z == -50) yield Direction.FRONT;
                else if (z == 50) yield Direction.BACK;
                else yield Direction.MIDDLEZ;
            }
        };
    }

    public static Axis getAxis(Direction face) {
        return switch (face) {
            case LEFT, RIGHT ->   Axis.X;
            case UP, DOWN ->      Axis.Y;
            case FRONT, BACK ->   Axis.Z;
            default -> throw new IllegalArgumentException("Invalid face for Axis return");
        };
    }

    public static Integer getCord(Direction face){
        return switch (face) {
            case RIGHT, DOWN, BACK ->   50;
            case LEFT, UP, FRONT ->      -50;
            default -> throw new IllegalArgumentException("Invalid face for coordinate return");
        };
    }
    public static void changeCustomColor(Direction direction, Map<Direction, Color> customColors, Color color) {
        customColors.put(direction, color);
    }
}

