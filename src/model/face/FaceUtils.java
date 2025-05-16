package model.face;

import javafx.scene.paint.Color;

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
}
