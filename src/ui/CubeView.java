package ui;

import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import model.Cube;
import logic.*;
import model.Cubie;
import model.face.Direction;
import model.sl.SLManager;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static model.face.FaceUtils.getRotationFace;

public class CubeView{
    public final Cube cube;
    private final SubScene subScene;
    private List<Transform> backup;

    public CubeView() {
        Group cubeGroup = new Group();
        cube = new Cube();

        cube.setScaleX(1);
        cube.setScaleY(1);
        cube.setScaleZ(1);
        cubeGroup.getChildren().add(cube);

        Rotate rotateX = new Rotate(30, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(30, Rotate.Y_AXIS);
        cube.getTransforms().addAll(rotateX, rotateY);

        subScene = new SubScene(cubeGroup, 1000, 500, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.GRAY);

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-500);
        camera.setNearClip(0.1);
        camera.setFarClip(10000);
        subScene.setCamera(camera);

        ViewDrag.enable(subScene, rotateX, rotateY);

        // 自適應
        subScene.widthProperty().addListener((obs, oldW, newW) -> {
            double scale = newW.doubleValue() / 1000.0;
            cube.setScaleX(scale);
            cube.setScaleY(scale);
            cube.setScaleZ(scale);
        });
        subScene.heightProperty().addListener((obs, oldH, newH) -> {
            double scale = newH.doubleValue() / 500.0;
            double uniform = Math.min(scale, cube.getScaleX());
            cube.setScaleX(uniform);
            cube.setScaleY(uniform);
            cube.setScaleZ(uniform);
        });

        subScene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER -> {
                    System.out.println("enter");
                    //System.out.println("Scene has focus? " + scene.isFocused());
                    System.out.println("Key pressed: " + e.getCode());
                    Cubie selected = CubeSelectionManager.getSelected();

                    if (selected == null) return;

                    Axis axis = CubeSelectionManager.getCurrentAxis();

                    Direction face = getRotationFace(selected, axis);
                    cube.applyRotation(face, true);

                    List<Cubie> layer = LayerSelector.getCubiesInSameLayer(
                            cube.allCubies, selected, axis
                    );

                    RotationAnimator.rotateLayer(layer, axis, 90, cube);
                }
                case SPACE -> {
                    Cubie selected = CubeSelectionManager.getSelected();

                    if (selected == null) return;

                    Axis axis = CubeSelectionManager.getCurrentAxis();

                    Direction face = getRotationFace(selected, axis);
                    cube.applyRotation(face, false);

                    List<Cubie> layer = LayerSelector.getCubiesInSameLayer(
                            cube.allCubies, selected, axis
                    );

                    RotationAnimator.rotateLayer(layer, axis, -90, cube);
                }
                case A -> cube.printAllFaces(cube.faceMap);
            }
        });
        subScene.requestFocus(); // 初始化時自動獲得焦點

    }

    public void updateCubeColor(int slot) throws IOException {
        cube.loadData(SLManager.load("save_slot_" + slot));
    }

    public SubScene getSubScene() {
        return subScene;
    }

    public void prettyCube(){
        cube.deselectAll();
        backup = cube.getTransforms().stream()
                .map(t -> t instanceof Rotate r ? new Rotate(r.getAngle(), r.getAxis()) : t)
                .collect(Collectors.toList());  // ← 改這裡！
        cube.getTransforms().clear();
        cube.getTransforms().addAll(
                new Rotate(30, Rotate.X_AXIS),
                new Rotate(30, Rotate.Y_AXIS)
        );
        subScene.setFill(Color.TRANSPARENT);

    }

    public void uglyCube(){
        cube.getTransforms().clear();
        cube.getTransforms().addAll(backup);
        backup.clear();
        subScene.setFill(Color.GRAY);
    }

}
