package ui;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.TextArea;
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
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class GameController {
    @FXML
    private StackPane cubeContainer;
    private CubeView cubeView;
    private String oldFileName;
    private String oldPngName;
    @FXML private BorderPane borderPane;
    @FXML private javafx.scene.control.Label timeLabel;
    @FXML private javafx.scene.control.Button startEndButton;
    @FXML private javafx.scene.control.Button pauseResumeButton;
    @FXML private javafx.scene.control.Button resetButton;
    private Timeline timeline;
    private int seconds = 0;
    private boolean isRunning = false;
    private boolean isPaused = false;
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
        startEndButton.setOnAction(e -> toggleStartEnd());
        pauseResumeButton.setOnAction(e -> togglePauseResume());
        resetButton.setOnAction(e -> resetTimer());
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
        startEndButton.setOnAction(e -> toggleStartEnd());
        pauseResumeButton.setOnAction(e -> togglePauseResume());
        resetButton.setOnAction(e -> resetTimer());
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
            oldFileName = "save_slot_" + n + ".json";
            oldPngName = "save_slot_" + n + ".png";
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

    public void onHelp() {
        // 用 Label 取代 TextArea
        Label infoLabel = new Label(
                " • 計時功能：點選「開始」啟動計時\n" +
                "• 計時功能：點選「開始」啟動計時\n" +
                        "   ⏸ 可暫停、▶ 再繼續\n" +
                        " • 存檔會自動儲存當前狀態與魔方顏色\n" +
                        " • 客製顏色：可更換魔方顏色\n" +
                        " • 自動打亂 & 自動解：待補上\n"
        );
        infoLabel.setWrapText(true);
        infoLabel.setStyle("-fx-font-size: 14px; -fx-line-spacing: 4px;");

        // 說明圖片
        ImageView imageView = new ImageView(new Image(
                getClass().getResource("/assets/images/help_keys.png").toExternalForm()
        ));
        imageView.setFitWidth(300); // 可調整寬度
        imageView.setPreserveRatio(true);

        // 垂直排列說明文字 + 圖片
        VBox contentBox = new VBox(20, infoLabel, imageView);
        contentBox.setPadding(new Insets(10));
        contentBox.setAlignment(Pos.TOP_CENTER);

        // 標題
        Label titleLabel = new Label(" 遊戲說明");
        titleLabel.setStyle("-fx-font-size: 30 px; -fx-font-weight: bold;");
        BorderPane.setAlignment(titleLabel, Pos.CENTER);

        // 關閉按鈕
        Button closeButton = new Button("關閉");
        closeButton.getStyleClass().add("icon-button");
        closeButton.setOnAction(e -> ((Stage) closeButton.getScene().getWindow()).close());
        BorderPane.setAlignment(closeButton, Pos.CENTER);

        // 視窗主體
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(20));
        pane.setTop(titleLabel);
        pane.setCenter(contentBox);
        pane.setBottom(closeButton);

        // 彈出視窗設定
        Stage helpStage = new Stage();
        helpStage.setTitle("說明");
        helpStage.setScene(new Scene(pane, 500, 700));
        helpStage.setResizable(false);
        helpStage.show();
    }


    //  計時控制邏輯
    private void toggleStartEnd() {
        if (!isRunning) {
            // 開始計時
            timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                seconds++;
                int min = seconds / 60;
                int sec = seconds % 60;
                timeLabel.setText(String.format("%02d : %02d", min, sec));
            }));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();

            isRunning = true;
            isPaused = false;

            startEndButton.setText("結束");
            pauseResumeButton.setText("⏸");
            pauseResumeButton.setDisable(false);
            resetButton.setDisable(false);
        } else {
            // 結束：停下並歸零
            if (timeline != null) timeline.stop();

            seconds = 0;
            timeLabel.setText("00 : 00");
            isRunning = false;
            isPaused = false;

            startEndButton.setText("開始");
            pauseResumeButton.setText("⏸");
            pauseResumeButton.setDisable(true);
            resetButton.setDisable(true);
        }
    }
    private void togglePauseResume() {
        if (!isPaused) {
            // 暫停
            if (timeline != null) timeline.pause();
            isPaused = true;
            pauseResumeButton.setText(" ▶ ");
        } else {
            // 繼續
            if (timeline != null) timeline.play();
            isPaused = false;
            pauseResumeButton.setText("⏸");
        }
    }
    private void resetTimer() {
        if (timeline != null) {
            timeline.stop();
        }
        seconds = 0;
        timeLabel.setText("00 : 00");

        isRunning = false;
        isPaused = false;

        startEndButton.setText("開始");
        pauseResumeButton.setText("⏸");
        pauseResumeButton.setDisable(true);
        resetButton.setDisable(true);
    }
}

