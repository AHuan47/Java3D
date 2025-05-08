package model;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import ui.CubeSelectionManager;

public class Cubie extends Group {
    private final Box body;

    public Cubie(int x, int y, int z, int size) {
        body = new Box(size * 0.95, size * 0.95, size * 0.95);
        body.setMaterial(new PhongMaterial(Color.DARKGRAY));
        getChildren().add(body);

        body.setDepthTest(DepthTest.ENABLE); // 啟用深度測試，允許點擊事件穿透進來
        body.setPickOnBounds(true);          // 允許邊界作為點擊範圍
        body.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                System.out.println("Clicked on cubie body");
                CubeSelectionManager.select(this);
            }
        });

        setTranslateX(x * size);
        setTranslateY(y * size);
        setTranslateZ(z * size);
        final double half = size / 2.0;
        // COLOR
        // FRONT
        if (z == 1) {
            Box sticker = new Box(size * 0.85, size * 0.85, 0.1);
            sticker.setMouseTransparent(true);
            sticker.setMaterial(new PhongMaterial(Color.RED));
            sticker.setTranslateZ(half);
            getChildren().add(sticker);
        }
        // BACK
        if (z == -1) {
            Box sticker = new Box(size * 0.85, size * 0.85, 0.1);
            sticker.setMouseTransparent(true);
            sticker.setMaterial(new PhongMaterial(Color.ORANGE));
            sticker.setTranslateZ(-half);// LEFT face
            getChildren().add(sticker);
        }
        // LEFT
        if (x == -1) {
            Box sticker = new Box(0.1, size * 0.85, size * 0.85);
            sticker.setMouseTransparent(true);
            sticker.setMaterial(new PhongMaterial(Color.BLUE));
            sticker.setTranslateX(-half);// RIGHT face
            getChildren().add(sticker);
        }
        // RIGHT
        if (x == 1) {
            Box sticker = new Box(0.1, size * 0.85, size * 0.85);
            sticker.setMouseTransparent(true);
            sticker.setMaterial(new PhongMaterial(Color.GREEN));
            sticker.setTranslateX(half);// UP face
            getChildren().add(sticker);
        }
        // UP
        if (y == -1) {
            Box sticker = new Box(size * 0.85, 0.1, size * 0.85);
            sticker.setMouseTransparent(true);
            sticker.setMaterial(new PhongMaterial(Color.WHITE));
            sticker.setTranslateY(-half);
            getChildren().add(sticker);
        }
        // DOWN
        if (y == 1) {
            Box sticker = new Box(size * 0.85, 0.1, size * 0.85);
            sticker.setMouseTransparent(true);
            sticker.setMaterial(new PhongMaterial(Color.YELLOW));
            sticker.setTranslateY(half);
            getChildren().add(sticker);
        }
    }

    public void setSelected(boolean selected) {
        body.setMaterial(selected ? new PhongMaterial(Color.RED)
                : new PhongMaterial(Color.DARKGRAY));
    }
}