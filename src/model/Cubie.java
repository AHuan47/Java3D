package model;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class Cubie extends Group {
    private final Box body;

    public Cubie(int x, int y, int z, int size) {
        body = new Box(size * 0.95, size * 0.95, size * 0.95);
        body.setMaterial(new PhongMaterial(Color.DARKGRAY));
        getChildren().add(body);

        setTranslateX(x * size);
        setTranslateY(y * size);
        setTranslateZ(z * size);
        final double half = size / 2.0;

        // COLOR
        // FRONT face
        if (z == 1) {
            Box sticker = new Box(size * 0.85, size * 0.85, 0.1);
            sticker.setMaterial(new PhongMaterial(Color.RED));
            sticker.setTranslateZ(half);
            getChildren().add(sticker);
        }
        // BACK face
        if (z == -1) {
            Box sticker = new Box(size * 0.85, size * 0.85, 0.1);
            sticker.setMaterial(new PhongMaterial(Color.ORANGE));
            sticker.setTranslateZ(-half);
            getChildren().add(sticker);
        }
        // LEFT face
        if (x == -1) {
            Box sticker = new Box(0.1, size * 0.85, size * 0.85);
            sticker.setMaterial(new PhongMaterial(Color.BLUE));
            sticker.setTranslateX(-half);
            getChildren().add(sticker);
        }
        // RIGHT face
        if (x == 1) {
            Box sticker = new Box(0.1, size * 0.85, size * 0.85);
            sticker.setMaterial(new PhongMaterial(Color.GREEN));
            sticker.setTranslateX(half);
            getChildren().add(sticker);
        }
        // UP face
        if (y == -1) {
            Box sticker = new Box(size * 0.85, 0.1, size * 0.85);
            sticker.setMaterial(new PhongMaterial(Color.WHITE));
            sticker.setTranslateY(-half);
            getChildren().add(sticker);
        }
        // DOWN face
        if (y == 1) {
            Box sticker = new Box(size * 0.85, 0.1, size * 0.85);
            sticker.setMaterial(new PhongMaterial(Color.YELLOW));
            sticker.setTranslateY(half);
            getChildren().add(sticker);
        }
    }
}