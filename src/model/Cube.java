package model;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import model.face.Face;

public class Cube extends Group {
    public Cube() {
        Face front = new Face(Color.RED);
        Face back = new Face(Color.ORANGE);
        Face left = new Face(Color.BLUE);
        Face right = new Face(Color.GREEN);
        Face up = new Face(Color.WHITE);
        Face down = new Face(Color.YELLOW);

        final int SIZE = 50;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Cubie cubie = new Cubie(x, y, z, SIZE);
                    this.getChildren().add(cubie);
                }
            }
        }
    }
}