import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.*;
import model.Cube;
import model.Cubie;
import ui.CubeSelectionManager;
import ui.ViewDrag;

import java.util.List;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        Group cubeGroup = new Group();
        Cube cube = new Cube();
        cubeGroup.getChildren().add(cube);

        // 旋轉軸
        Rotate rotateX = new Rotate(30, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(30, Rotate.Y_AXIS);
        cube.getTransforms().addAll(rotateX, rotateY);
        // 滑鼠拖曳旋轉 from ui.ViewDrag
        ViewDrag.enable(cubeGroup, rotateX, rotateY);

        SubScene subScene = new SubScene(cubeGroup, 600, 400, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.web("#202020"));

        StackPane root = new StackPane(subScene);
        subScene.widthProperty().bind(root.widthProperty());
        subScene.heightProperty().bind(root.heightProperty());
        Scene scene = new Scene(root, 600, 400);

        // 點擊偵測測試
        scene.setOnMouseClicked(e -> {
            Node target = e.getPickResult().getIntersectedNode();
            System.out.println("Clicked on: " + target);
        });

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-500);
        camera.setNearClip(0.1);
        camera.setFarClip(10000);
        subScene.setCamera(camera);

        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            switch (code) {
                case UP -> camera.setTranslateZ(camera.getTranslateZ() + 10);
                case DOWN -> camera.setTranslateZ(camera.getTranslateZ() - 10);
                case SPACE, ENTER -> {
                    Cubie selected = CubeSelectionManager.getSelected();
                    if (selected == null) return;
                    Axis axis = CubeSelectionManager.getCurrentAxis();

                    List<Cubie> layer = LayerSelector.getCubiesInSameLayer(
                            cube.getAllCubies(), selected, axis
                    );

                    RotationAnimator.rotateLayer(layer, axis, 90, cube, () -> {
                        System.out.println("動畫完成！");
                    });
                }
                case A -> CubeSelectionManager.cycleAxis();
            }
        });

        stage.setScene(scene);
        stage.setTitle("空白測試視窗");
        stage.show();
    }
}