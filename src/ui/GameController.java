package ui;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
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
    private static Region selectedColor;
    private boolean lockSave = false;
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
        cubeView.cube.applyAllCustomColor();
        Platform.runLater(() -> cubeScene.requestFocus());
    }

    public void onBack() throws IOException {
        System.out.println("onBack");
        oldFileName = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Select.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.setScene(new Scene(root, 1200, 800));
        stage.show();
    }

    public void onSave() throws IOException {
        if(lockSave) return;
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

    public void onColor() {
        cubeView.getSubScene().setFill(Color.valueOf("#3d3d3d"));
        cubeView.cube.deselectAll();
        cubeView.cube.setStickerTouchable(false);
        cubeContainer.setBackground(new Background(new BackgroundFill(
                Color.valueOf("#3d3d3d"), CornerRadii.EMPTY, Insets.EMPTY
        )));
        lockSave = true;

        // -- 左下角 UI --
        VBox inputPanel = new VBox(5); // 垂直排列：提示+輸入框
        inputPanel.setPickOnBounds(false); // 不擋後面 3D 滑鼠事件

        // 提示區域
        HBox promptBox = new HBox(10); // 水平排列：框 + 文字
        promptBox.setAlignment(Pos.CENTER_LEFT);
        selectedColor = new Region(); // 當作 2D 色塊
        selectedColor.setPrefSize(30, 30);
        selectedColor.setStyle("-fx-background-color: #888888; -fx-border-color: black;");
        Label label = new Label("當前選取顏色");
        promptBox.getChildren().addAll(selectedColor, label);

        HBox promptBox2 = new HBox(10); // 水平排列：框 + 文字
        promptBox2.setAlignment(Pos.CENTER_LEFT);
        Region newColor = new Region();
        newColor.setPrefSize(30, 30);
        newColor.setMaxWidth(30);
        newColor.setStyle("-fx-background-color: #880088; -fx-border-color: black;");
        Label label2 = new Label("輸入 hex 色碼");
        promptBox2.getChildren().addAll(newColor, label2);


        // 輸入框
        TextField inputField = new TextField();
        inputField.setPromptText("#FF8800");
        inputField.setMaxWidth(120);
        // 輸入框持續監聽
        inputField.textProperty().addListener((obs, oldText, newText) -> {
            // 判斷是不是合法 HEX
            if (newText.matches("#?[0-9a-fA-F]{6}")) {
                // 合法 hex
                Color c = Color.web(newText.startsWith("#") ? newText : "#" + newText);
                label2.setText("點擊確認以更換");
                newColor.setStyle("-fx-background-color: " + newText + "; -fx-border-color: black;");
            } else {
                // 不合法hex
                label2.setText("hex格式錯誤");
                newColor.setStyle("-fx-background-color: transparent; -fx-border-color: red;");
            }
        });

        // 按鈕：套用顏色
        Button applyButton = new Button("套用顏色");
        applyButton.setOnAction(e -> {
            String input = inputField.getText();
            if (input.matches("#?[0-9a-fA-F]{6}")) {
                if (StickerSelectionManager.returnSelectedColorHex() == null) {
                    label2.setText("未選擇欲更換面！");
                } else {
                    boolean hasHash = input.startsWith("#");
                    String hex = hasHash ? input : "#" + input;

                    if (!hasHash) {
                        label2.setText("開頭加入 # ");
                    } else {
                        Color c = Color.web(hex);
                        if (cubeView.cube.checkColor(c)) {
                            label2.setText("更換成功");
                            cubeView.cube.applyCustomColor(StickerSelectionManager.returnSelectedColorHex(), c);
                            cubeView.cube.applyAllCustomColor();
                        } else {
                            label2.setText("無法使用重複顏色");
                        }
                    }
                }
            } else {
                label2.setText("hex格式錯誤！！！");
            }

        });

        // exit button
        Button exitButton = new Button("保存並離開");
        exitButton.setOnAction(e -> {
            StickerSelectionManager.clearSelect();
            cubeContainer.getChildren().remove(inputPanel);
            cubeView.cube.setStickerTouchable(true);
            cubeContainer.setBackground(new Background(new BackgroundFill(
                    Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY
            )));
            cubeView.getSubScene().setFill(Color.GRAY);
            lockSave = false;
            cubeView.getSubScene().requestFocus();
        });

        // 組合進 VBox
        inputPanel.getChildren().addAll(promptBox, promptBox2, inputField, applyButton, exitButton);
        inputPanel.setPadding(new Insets(4));
        inputPanel.setMaxWidth(180);
        inputPanel.setBackground(new Background(new BackgroundFill(
                Color.color(1, 1, 1, 0.8), CornerRadii.EMPTY, Insets.EMPTY
        )));

        // 設定位置與邊距
        StackPane.setAlignment(inputPanel, Pos.BOTTOM_LEFT);
        StackPane.setMargin(inputPanel, new Insets(0, 0, 10, 10));

        // 加進畫面
        cubeContainer.getChildren().add(inputPanel);
    }

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

    public static void changeSelectColor(String color){
        selectedColor.setStyle("-fx-background-color: " + color + "; -fx-border-color: black;");
    }
}