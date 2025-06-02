package ui;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.layout.StackPane;

public class GameController {
    @FXML
    private StackPane cubeContainer;
    private CubeView cubeView;

    @FXML
    public void initialize() {
        cubeView = new CubeView();
        SubScene cubeScene = cubeView.getSubScene();
        cubeScene.setManaged(true);
        cubeContainer.getChildren().clear();
        cubeContainer.getChildren().add(cubeScene);
        Platform.runLater(() -> cubeScene.requestFocus());
    }


    public void onBack() {}
    public void onSave() {}
    public void onColor() {}
    public void onShuffle() {}
    public void onSolve() {}
    public void onHelp() {}
}

