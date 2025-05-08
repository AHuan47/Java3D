import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import model.Cube;
import ui.ViewDrag;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        Cube cube = new Cube();

        cube.translateXProperty().bind(root.widthProperty().divide(2));
        cube.translateYProperty().bind(root.heightProperty().divide(2));

        // 建立旋轉軸並加入cube.transform
        Rotate rotateX = new Rotate(30, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(30, Rotate.Y_AXIS);
        cube.getTransforms().addAll(rotateX, rotateY);

        ViewDrag.enable(root, rotateX, rotateY);
        root.getChildren().add(cube);
        Scene scene = new Scene(root, 600, 400, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.web("#202020"));
        stage.setScene(scene);
        stage.setTitle("Rubik's Cube");
        stage.show();
    }
}