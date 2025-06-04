package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class SelectController {

    private final String JSON_DIR = "resources/assets/saves/Data/";
    private final String IMAGE_DIR = "resources/assets/saves/Thumbnail/";
    private final Image checkEmpty = new Image(getClass().getResourceAsStream("/assets/images/check-empty.png"));
    private final Image checkMark = new Image(getClass().getResourceAsStream("/assets/images/check-checked.png"));
    private final Image binImage = new Image(getClass().getResourceAsStream("/assets/images/bin.png"));
    private final Image deleteImage = new Image(getClass().getResourceAsStream("/assets/images/delete.png"));

    // 用來記錄所有已讀取的 JSON 檔案
    private List<File> jsonFileList = new ArrayList<>();

    // 當前選中的 ToggleButton 與對應的 JSON
    private ToggleButton selectedToggle = null;
    private File selectedJson = null;

    @FXML private GridPane gridPane;

    // 初始化畫面
    public void initialize() {
        refreshSlots();
    }

    //讀取資料夾內的JSON渲染slot
    private void refreshSlots() {
        gridPane.getChildren().clear();
        jsonFileList.clear();

        File dir = new File(JSON_DIR);
        if (!dir.exists()) return;

        File[] jsonFiles = dir.listFiles((d, name) -> name.endsWith(".json"));
        if (jsonFiles == null) return;

        List<File> allJsonFiles = Arrays.stream(jsonFiles)
                .sorted()
                .limit(6)
                .collect(Collectors.toList());

        jsonFileList.addAll(allJsonFiles);

        // 每一個 JSON 建立一個 slot
        for (int i = 0; i < jsonFileList.size(); i++) {
            File json = jsonFileList.get(i);
            File image = new File(IMAGE_DIR + json.getName().replace(".json", ".png"));
            addSlot(i, image, json);
        }

        // 少於六個，補上新增按鈕
        if (jsonFileList.size() < 6) {
            addAddButton(jsonFileList.size());
        }
    }

    // 建立slot 卡片
    private void addSlot(int index, File imageFile, File jsonFile) {
        StackPane stack = new StackPane();
        stack.getStyleClass().add("grid-cell");

        // 勾選按鈕與圖片
        ToggleButton toggle = new ToggleButton();
        ImageView toggleImage = new ImageView(checkEmpty);
        toggleImage.setFitWidth(30);
        toggleImage.setFitHeight(30);
        toggle.setGraphic(toggleImage);
        StackPane.setAlignment(toggle, javafx.geometry.Pos.TOP_LEFT);

        // 縮圖
        ImageView imageView = new ImageView();
        if (imageFile.exists()) {
            imageView.setImage(new Image(imageFile.toURI().toString(), 250, 250, true, true));
        }
        StackPane.setAlignment(imageView, javafx.geometry.Pos.CENTER);

        // 刪除按鈕
        Button delete = new Button();
        delete.getStyleClass().add("clean-button"); // 設定透明樣式
        ImageView deleteImageView = new ImageView(binImage);
        deleteImageView.setFitWidth(30);
        deleteImageView.setFitHeight(30);
        delete.setGraphic(deleteImageView);
        StackPane.setAlignment(delete, javafx.geometry.Pos.BOTTOM_RIGHT);

        // 確認選擇按鈕（一開始隱藏）
        Button confirmSelect = new Button("確認選擇");
        confirmSelect.getStyleClass().add("confirm-button");
        confirmSelect.setVisible(false);
        StackPane.setAlignment(confirmSelect, javafx.geometry.Pos.BOTTOM_CENTER);

        // 確認刪除按鈕（一開始隱藏）
        Button confirmDelete = new Button("確認刪除");
        confirmDelete.getStyleClass().add("delete-confirm-button");
        confirmDelete.setVisible(false);
        StackPane.setAlignment(confirmDelete, javafx.geometry.Pos.BOTTOM_CENTER);

        // 切換 toggle 狀態時更新圖示與顯示確認選擇
        toggle.setOnAction(e -> {
            boolean isSelected = toggle.isSelected();
            toggleImage.setImage(isSelected ? checkMark : checkEmpty);
            confirmSelect.setVisible(isSelected); // 跳出確認選擇
            confirmDelete.setVisible(false);
            deleteImageView.setImage(binImage);
            selectedToggle = isSelected ? toggle : null;
            selectedJson = isSelected ? jsonFile : null; // 取得選擇圖片的對應檔名
        });

        // 刪除按鈕切換圖示與確認刪除狀態
        delete.setOnAction(e -> {
            if (deleteImageView.getImage() == binImage) {
                deleteImageView.setImage(deleteImage);
                confirmDelete.setVisible(true); // 跳出確認刪除
                confirmSelect.setVisible(false);
                toggle.setSelected(false);
                toggleImage.setImage(checkEmpty);
            } else {
                deleteImageView.setImage(binImage);
                confirmDelete.setVisible(false);
            }
        });

        // 點擊確認選擇時印出檔案名稱
        confirmSelect.setOnAction(e -> goToNextPage(jsonFile.getName()));

        // 點擊確認刪除時刪除檔案與縮圖並刷新畫面
        confirmDelete.setOnAction(e -> {
            if (jsonFile.exists()) jsonFile.delete();
            File imgFile = new File(IMAGE_DIR + jsonFile.getName().replace(".json", ".png"));
            if (imgFile.exists()) imgFile.delete();
            refreshSlots();
        });

        // 加入所有 UI 元件到 StackPane
        stack.getChildren().addAll(imageView, toggle, delete, confirmSelect, confirmDelete);
        GridPane.setColumnIndex(stack, index % 3);
        GridPane.setRowIndex(stack, index / 3);
        gridPane.getChildren().add(stack);
    }

    // 當 slot 未滿六個，加入新增按鈕的卡片
    private void addAddButton(int index) {
        VBox box = new VBox();
        box.setAlignment(javafx.geometry.Pos.CENTER);
        box.getStyleClass().add("slot-cell");

        Button addButton = new Button();
        addButton.getStyleClass().add("clean-button");
        ImageView addImg = new ImageView(new Image(getClass().getResourceAsStream("/assets/images/add.png"), 140, 140, true, true));
        addButton.setGraphic(addImg);
        addButton.setOnAction(e -> goToNextPage(null));

        box.getChildren().add(addButton);
        GridPane.setColumnIndex(box, index % 3);
        GridPane.setRowIndex(box, index / 3);
        gridPane.getChildren().add(box);
    }

    // 切換場景：目前僅印出檔案名稱或新增狀態（未跳轉）
    private void goToNextPage(String jsonFileName) {
        if (jsonFileName != null) {
            System.out.println("使用者選擇檔案：" + jsonFileName);
        } else {
            System.out.println("使用者新增新遊戲中...");
        }
    }
}
