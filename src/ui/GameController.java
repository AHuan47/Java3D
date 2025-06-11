package ui;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.animation.PauseTransition;
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
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import logic.Move;
import logic.SequentialRotationAnimator;
import model.sl.SLManager;
import scrambler.Parser;
import scrambler.Scrambler;
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
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import solver.SolverOptions;
import solver.SolverResult;
import solver.TwoPhaseSolver;

public class GameController {
    @FXML
    private StackPane cubeContainer;
    private CubeView cubeView;
    private String oldFileName;
    private String oldPngName;
    private static Region selectedColor;
    private boolean lockSave = false;
    private TwoPhaseSolver solver;
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
    private Button autoShuffleButton;
    @FXML
    private Button solveButton;
    @FXML
    private TextField scrambleLengthInput;

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
        this.solver = new TwoPhaseSolver();
        applyInsetEffect(scrambleLengthInput);
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
        startEndButton.setOnAction(e -> toggleStartEnd());
        pauseResumeButton.setOnAction(e -> togglePauseResume());
        this.solver = new TwoPhaseSolver();
        applyInsetEffect(scrambleLengthInput);
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
        if(lockSave) return;
        cubeView.getSubScene().setFill(Color.valueOf("#3d3d3d"));
        cubeView.cube.deselectAll();
        cubeView.cube.setStickerTouchable(false);
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
            cubeView.getSubScene().setFill(Color.TRANSPARENT);

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

    @FXML
    public void onShuffle() {
        // 切換 UI 狀態
        autoShuffleButton.setVisible(false);
        scrambleLengthInput.setVisible(true);
        scrambleLengthInput.clear();
        scrambleLengthInput.setPromptText("輸入打亂步數 (例如: 25)");
        scrambleLengthInput.requestFocus();
        scrambleLengthInput.setOnKeyPressed(null);

        scrambleLengthInput.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                // 隱藏輸入框 + 顯示並禁用按鈕
                autoShuffleButton.setVisible(true);
                autoShuffleButton.setDisable(true);
                scrambleLengthInput.setVisible(false);

                cubeView.cube.deselectAll();

                String inputText = scrambleLengthInput.getText().trim();
                List<Move> moves;
                String scrambleInfo;

                if (inputText.isEmpty()) {
                    moves = new Scrambler().genStdScramble();
                    scrambleInfo = "標準打亂 (30 步)";
                } else {
                    try {
                        int length = Integer.parseInt(inputText);
                        if (length <= 0) {
                            throw new NumberFormatException("Length must be positive");
                        }
                        moves = new Scrambler().genScrambleMoves(length);
                        scrambleInfo = "自訂打亂 (" + length + " 步)";
                    } catch (NumberFormatException ex) {
                        System.err.println("無效的打亂長度: " + inputText + "，使用標準打亂");
                        moves = new Scrambler().genStdScramble();
                        scrambleInfo = "標準打亂 (30 步) - 輸入無效";
                    }
                }

                System.out.println(scrambleInfo + ": " + Parser.movesToString(moves));

                SequentialRotationAnimator.sequentialAnimator(moves, cubeView.cube);
                cubeView.getSubScene().requestFocus();

                // 創建一個 PauseTransition 來延遲執行 UI 重置和按鈕啟用(延遲時間 = 步數 * 400 毫秒)
                Duration delay = Duration.millis(moves.size() * 400.0);
                PauseTransition pause = new PauseTransition(delay);
                pause.setOnFinished(event -> {
                    resetShuffleUI();
                    autoShuffleButton.setDisable(false);
                });
                pause.play(); // 開始延遲
            }
        });
    }
    private void resetShuffleUI() {
        scrambleLengthInput.clear();
        scrambleLengthInput.setVisible(false);
        autoShuffleButton.setVisible(true);
        scrambleLengthInput.setOnKeyPressed(null);
        autoShuffleButton.setDisable(false);
    }

    public void onSolve() {
        try {
            if (solveButton != null) {
                solveButton.setDisable(true);
            }

            System.out.println("Solving the cube...");
            cubeView.cube.deselectAll();

            long startTime = System.nanoTime();
            SolverResult result = solver.solve(cubeView.cube);
            long solveTime = (System.nanoTime() - startTime) / 1_000_000;

            List<Move> solutionMoves = result.moves;

            System.out.println("Solution found in " + solutionMoves.size() + "steps : " + result.getMovesString());
            System.out.println("Time spent: " + solveTime + "ms, Times probed: " + result.probes);

            SequentialRotationAnimator.sequentialAnimator(solutionMoves, cubeView.cube);
            cubeView.getSubScene().requestFocus();

            Duration delay = Duration.millis(solutionMoves.size() * 400.0);
            PauseTransition pause = new PauseTransition(delay);
            pause.setOnFinished(event -> {
                System.out.println("Cube Solved.");
                if (solveButton != null) {
                    solveButton.setDisable(false);
                }
            });
            pause.play();

        } catch (Exception e) {
            if (solveButton != null) {
                solveButton.setDisable(false);
            }

            System.err.println("Error solving: " + e.getMessage());
            e.printStackTrace();
        }
    }
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

    private void applyInsetEffect(TextField field) {
        InnerShadow dark = new InnerShadow();
        dark.setColor(Color.rgb(0, 0, 0, 0.5));
        dark.setRadius(8);
        dark.setOffsetX(3);
        dark.setOffsetY(3);

        InnerShadow light = new InnerShadow();
        light.setColor(Color.rgb(255, 255, 255, 0.5));
        light.setRadius(8);
        light.setOffsetX(-2);
        light.setOffsetY(-2);

        light.setInput(dark);
        field.setEffect(light);
    }
    public void onHelp() {
        Font.loadFont(getClass().getResource("/fonts/SourceHanSerifTW-Heavy.otf").toExternalForm(), 20);

        // 功能介紹區塊
        Label featureTitle = new Label("功能介紹");
        featureTitle.setFont(Font.font("Source Han Serif TW Heavy", FontWeight.NORMAL, 20));

        Label featureLabel = new Label(
                "• 計時功能：點選「開始」啟動計時\n" +
                        "   ⏸ 可暫停、▶ 可繼續\n" +
                        "• 存檔功能：會自動儲存當前狀態與魔方顏色\n" +
                        "• 客製顏色：可更換魔方的顏色配置\n" +
                        "• 自動打亂：點選自動打亂按鈕可輸入欲打亂步數\n，     按下ENTER鍵開始打亂\n" +
                        "• 自動解：從當前狀態開始解直到回復原狀\n"
        );
        featureLabel.setWrapText(true);
        featureLabel.setStyle(
                "-fx-font-family: 'Source Han Serif TW Heavy';" +
                        "-fx-font-size: 14px;" +
                        "-fx-line-spacing: 6px;" +
                        "-fx-padding: 12px;" +
                        "-fx-background-color:rgba(229, 225, 218, 0.3);" +
                        "-fx-background-radius: 12px;" +
                        "-fx-border-radius: 12px;" +
                        "-fx-border-color: BLACK;" +
                        "-fx-border-width: 1px;"
        );
        featureLabel.setPrefWidth(600);

        //  操作說明區塊
        Label controlTitle = new Label("操作說明");
        controlTitle.setFont(Font.font("Source Han Serif TW Heavy", FontWeight.NORMAL, 20));

        Label controlLabel = new Label(
                "• ▲ 鍵：拉近 / ▼ 鍵：拉遠\n" + "• Space 鍵：逆時針旋轉 /  Enter 鍵：順時針旋轉\n" + "• 滑鼠左鍵：控制方塊 / 滑鼠右鍵：選擇方塊\n"
        );
        controlLabel.setWrapText(true);
        controlLabel.setStyle(
                "-fx-font-family: 'Source Han Serif TW Heavy';" +
                        "-fx-font-size: 14px;" +
                        "-fx-line-spacing: 6px;" +
                        "-fx-padding: 12px;" +
                        "-fx-background-color:rgba(229, 225, 218, 0.3);" +
                        "-fx-background-radius: 12px;" +
                        "-fx-border-radius: 12px;" +
                        "-fx-border-color: BLACK;" +
                        "-fx-border-width: 1px;"
        );
        controlLabel.setPrefWidth(600);

        VBox contentBox = new VBox(20,
                featureTitle, featureLabel,
                controlTitle, controlLabel
        );
        contentBox.setPadding(new Insets(10));
        contentBox.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("遊戲說明");
        titleLabel.setStyle(
                "-fx-font-family: 'Source Han Serif TW Heavy';" +
                        "-fx-font-size: 30px;" +
                        "-fx-font-weight: bold;"
        );
        BorderPane.setAlignment(titleLabel, Pos.CENTER);

        Button closeButton = new Button("關閉");
        closeButton.setStyle("-fx-font-family: 'Source Han Serif TW Heavy'; -fx-font-size: 14px;");
        closeButton.getStyleClass().add("icon-button");
        closeButton.setOnAction(e -> ((Stage) closeButton.getScene().getWindow()).close());

        HBox buttonBox = new HBox(closeButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(20));
        pane.setTop(titleLabel);
        pane.setCenter(contentBox);
        pane.setBottom(buttonBox);


        Stage helpStage = new Stage();
        helpStage.setTitle("說明");
        Scene scene = new Scene(pane, 500, 700);
        helpStage.setScene(scene);
        helpStage.setResizable(false);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                helpStage.close();
            }
        });
        cubeView.getSubScene().requestFocus();
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
        }
        cubeView.getSubScene().requestFocus();
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
        cubeView.getSubScene().requestFocus();
    }
    public static void changeSelectColor(String color){
        if(selectedColor == null) return;
        selectedColor.setStyle("-fx-background-color: " + color + "; -fx-border-color: black;");
    }
}
