package ui;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.transform.Rotate;

public class ViewDrag {
    public static void enable(Node target, Rotate rotateX, Rotate rotateY) {
        final Delta delta = new Delta();

        target.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                System.out.println("Here in drag.");
                delta.x = e.getSceneX();
                delta.y = e.getSceneY();
            }
        });

        target.setOnMouseDragged(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                double dx = e.getSceneX() - delta.x;
                double dy = e.getSceneY() - delta.y;
                rotateY.setAngle(rotateY.getAngle() - dx * 0.5);
                rotateX.setAngle(rotateX.getAngle() + dy * 0.5);
                delta.x = e.getSceneX();
                delta.y = e.getSceneY();
            }
        });
    }

    private static class Delta {
        double x, y;
    }
}