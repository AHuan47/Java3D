package ui;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.sl.SLManager;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameController {
    @FXML
    private StackPane cubeContainer;
    private CubeView cubeView;
    private String oldFileName;
    private String oldPngName;
    @FXML private BorderPane borderPane;

    @FXML
//    public void initialize() {
//        cubeView = new CubeView();
//        SubScene cubeScene = cubeView.getSubScene();
//        cubeScene.setManaged(true);
//        cubeContainer.getChildren().clear();
//        cubeContainer.getChildren().add(cubeScene);
//        Platform.runLater(() -> cubeScene.requestFocus());
//    }

    public void initNew(){
        cubeView = new CubeView();
        SubScene cubeScene = cubeView.getSubScene();
        cubeScene.setManaged(true);
        cubeContainer.getChildren().clear();
        cubeContainer.getChildren().add(cubeScene);
        Platform.runLater(() -> cubeScene.requestFocus());
    }

    public void initOld(String jsonName, String pngName) throws IOException {
        cubeView = new CubeView();
        cubeView.cube.loadData(SLManager.load(jsonName));
        oldFileName = jsonName;
        oldPngName = pngName;
        SubScene cubeScene = cubeView.getSubScene();
        cubeScene.setManaged(true);
        cubeContainer.getChildren().clear();
        cubeContainer.getChildren().add(cubeScene);
        Platform.runLater(() -> cubeScene.requestFocus());
    }

    public void onBack() throws IOException {
        oldFileName = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Select.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.setScene(new Scene(root, 1200, 800));
        stage.show();
    }

    public void onSave() throws IOException {
        if(oldFileName == null) {
            int n = getNewId();
            SLManager.save(cubeView.cube, n); // get newId update AvailableIds
            Manifest manifest = Manifest.getManifest();
            int m = manifest.returnSlot();
            manifest.slots.get(m).data = "save_slot_" + n + ".json";
            manifest.slots.get(m).thumbnail = "save_slot_" + n + ".png";
            Manifest.updateManifest(manifest);
            cubeView.prettyCube();
            ScreenshotUtil.saveSubSceneToThumbnail(cubeView.getSubScene(),"save_slot_" + n + ".png");
            cubeView.uglyCube();
        } else {
            SLManager.save(cubeView.cube, oldFileName);
            cubeView.prettyCube();
            ScreenshotUtil.saveSubSceneToThumbnail(cubeView.getSubScene(),oldPngName);
            cubeView.uglyCube();
        }

    }

    public void onColor() {}
    public void onShuffle() {}
    public void onSolve() {}
    public void onHelp() {}
    
    private int getNewId(){
        try {
            String json = Files.readString(Path.of("resources/assets/saves/availableIds.json"));
            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();
            Map<String, Object> map = new Gson().fromJson(json, type);

            List<Double> raw = (List<Double>) map.get("availableIds");
            List<Integer> available = raw.stream()
                    .map(Double::intValue)
                    .collect(Collectors.toList());
            int newId = available.getFirst();
            Manifest.updateAvailableIds(1, newId);
            return newId;

        } catch (IOException e){
            System.out.println("availableIds.json not found when get id from pool");
            return -1;
        }
    }
}

