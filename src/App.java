import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import model.Cube;
import ui.ViewDrag;

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

        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(0);
        light.setTranslateY(0);
        light.setTranslateZ(-200); // 從前方打過來

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-500);
        camera.setNearClip(0.1);
        camera.setFarClip(10000);
        subScene.setCamera(camera);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.UP) {
                camera.setTranslateZ(camera.getTranslateZ() + 10);
            } else if (e.getCode() == KeyCode.DOWN) {
                camera.setTranslateZ(camera.getTranslateZ() - 10);
            }
        });

        stage.setScene(scene);
        stage.setTitle("Rubik's Cube");
        stage.show();
    }
}