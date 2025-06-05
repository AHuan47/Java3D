import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import logic.*;
import model.Cube;
import model.Cubie;
import model.face.Direction;
import ui.CubeSelectionManager;
import ui.ViewDrag;
import scrambler.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static model.face.FaceUtils.*;

public class App extends Application {  //暫時用不到了，但先留著
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
        /*scene.setOnMouseClicked(e -> {
            Node target = e.getPickResult().getIntersectedNode();
            System.out.println("Clicked on: " + target);
        });*/

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-500);
        camera.setNearClip(0.1);
        camera.setFarClip(10000);
        subScene.setCamera(camera);

        // 使用者輸入
        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            switch (code) {
                case UP -> camera.setTranslateZ(camera.getTranslateZ() + 10);
                case DOWN -> camera.setTranslateZ(camera.getTranslateZ() - 10);
                case ENTER -> {
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
                case A -> {
                    cube.printAllFaces(cube.faceMap);} // check face statue, for debug
                case S -> {
                    Scrambler scrambler = new Scrambler();
                    List<Move> scrambleMoves = scrambler.genStdScramble();
                    String scrambleString = Parser.movesToString(scrambleMoves);

                    System.out.println("Applying scramble: " + scrambleString);

                    SequentialRotationAnimator.sequentialAnimator(scrambleMoves, cube);

                }
                case D -> {
                    List<Move> pendingMoves = new ArrayList<>();
                    Collections.addAll(pendingMoves,Move.F);
                    System.out.println("Applying move: " + Parser.movesToString(pendingMoves));
                    SequentialRotationAnimator.sequentialAnimator(pendingMoves, cube);
                }
                }
            }
        );

        stage.setScene(scene);
        stage.setTitle("Rubik's Cube");
        stage.show();
    }
}