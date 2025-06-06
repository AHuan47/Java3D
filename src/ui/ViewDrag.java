package ui;

import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.transform.Rotate;

public class ViewDrag {
    public static void enable(Node target, Rotate rotateX, Rotate rotateY) {
        final Delta delta = new Delta();

        target.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                delta.x = e.getSceneX();
                delta.y = e.getSceneY();

                double angleX = rotateX.getAngle() % 360;
                if (angleX > 180) angleX -= 360;
                else if (angleX < -180) angleX += 360;

                delta.invertX = Math.abs(angleX) > 90;  // 上下翻轉時就 invert 左右方向
            }
        });

        target.setOnMouseDragged(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                double dx = e.getSceneX() - delta.x;
                double dy = e.getSceneY() - delta.y;

                double factor = 0.5;

                // 應用在 mousePressed 當下就決定好的方向
                rotateY.setAngle(rotateY.getAngle() - dx * factor * (delta.invertX ? -1 : 1));
                rotateX.setAngle(rotateX.getAngle() + dy * factor);

                delta.x = e.getSceneX();
                delta.y = e.getSceneY();
            }
        });
    }

    private static class Delta {
        double x, y;
        boolean invertX = false;
    }
}
