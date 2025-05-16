package logic;

import javafx.animation.RotateTransition;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import model.Cubie;

import java.util.List;

public class RotationAnimator {
    public static void rotateLayer(List<Cubie> cubies, Axis axis, double angle, Group cubeRoot, Runnable onFinished) {
        // 建立臨時 group
        Group rotationGroup = new Group();
        rotationGroup.getChildren().addAll(cubies);
        cubeRoot.getChildren().add(rotationGroup);

        // 設定旋轉中心為這一層的平均位置
        double centerX = cubies.stream().mapToDouble(c -> c.getTranslateX()).average().orElse(0);
        double centerY = cubies.stream().mapToDouble(c -> c.getTranslateY()).average().orElse(0);
        double centerZ = cubies.stream().mapToDouble(c -> c.getTranslateZ()).average().orElse(0);

        rotationGroup.setTranslateX(centerX);
        rotationGroup.setTranslateY(centerY);
        rotationGroup.setTranslateZ(centerZ);

        for (Cubie c : cubies) {
            c.setTranslateX(c.getTranslateX() - centerX);
            c.setTranslateY(c.getTranslateY() - centerY);
            c.setTranslateZ(c.getTranslateZ() - centerZ);
        }

        // 選軸
        RotateTransition rt = new RotateTransition(Duration.seconds(0.1), rotationGroup);
        rt.setByAngle(angle);
        switch (axis) {
            case X -> rotationGroup.setRotationAxis(Rotate.X_AXIS);
            case Y -> rotationGroup.setRotationAxis(Rotate.Y_AXIS);
            case Z -> rotationGroup.setRotationAxis(Rotate.Z_AXIS);
        }

        rt.setOnFinished(e -> {
            for (Cubie c : cubies) {
                Point3D scenePos = c.localToScene(Point3D.ZERO);
                Point3D parentPos = cubeRoot.sceneToLocal(scenePos);

                c.setTranslateX(c.snapToGrid(parentPos.getX(), 50));
                c.setTranslateY(c.snapToGrid(parentPos.getY(), 50));
                c.setTranslateZ(c.snapToGrid(parentPos.getZ(), 50));

                c.rotateAxis(axis, 90);
                c.snapOrientation(1e-8);

                System.out.printf("X: %.3f  Y: %.3f  Z: %.3f%n",
                        c.getTranslateX(), c.getTranslateY(), c.getTranslateZ());
            }
            rotationGroup.getChildren().clear();
            cubeRoot.getChildren().remove(rotationGroup);
            cubeRoot.getChildren().addAll(cubies);

            if (onFinished != null) onFinished.run(); // 音效可以在這執行
        });

        rt.play();
    }
}
