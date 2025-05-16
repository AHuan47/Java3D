package model.face;
import javafx.scene.paint.Color;

import java.util.*;
import static model.face.FaceUtils.*;
import model.Cube;

public class Face {
    private final Color[][] tiles = new Color[3][3];

    public Face(Color defaultColor) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                tiles[i][j] = defaultColor;
    }

    public void rotate(boolean clockwise) {
        Color[][] newTiles = new Color[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                newTiles[i][j] = clockwise ? tiles[2 - j][i] : tiles[j][2 - i];
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tiles[i][j] = newTiles[i][j];
            }
        }
    }

    public Color[][] getTiles() {
        return tiles;
    }
}
