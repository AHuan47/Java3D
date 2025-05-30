package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SelectController {

    @FXML
    private ToggleGroup toggleGroup;

    @FXML
    private ToggleButton toggle1, toggle2, toggle3, toggle4, toggle5;

    @FXML
    private ImageView toggle1Image, toggle2Image, toggle3Image, toggle4Image, toggle5Image;

    @FXML
    private Button delete1, delete2, delete3, delete4, delete5;

    private final Image checkEmpty = new Image(getClass().getResourceAsStream("/assets/images/check-empty.png"));
    private final Image checkMark = new Image(getClass().getResourceAsStream("/assets/images/check-checked.png"));

    private final Image binImage = new Image(getClass().getResourceAsStream("/assets/images/bin.png"));
    private final Image deleteImage = new Image(getClass().getResourceAsStream("/assets/images/delete.png"));

    @FXML
    public void initialize() {
        // 初始化 ToggleButton 的圖示為空勾選框
        toggle1Image.setImage(checkEmpty);
        toggle2Image.setImage(checkEmpty);
        toggle3Image.setImage(checkEmpty);
        toggle4Image.setImage(checkEmpty);
        toggle5Image.setImage(checkEmpty);

        // 初始化刪除按鈕圖片為垃圾桶
        ((ImageView) delete1.getGraphic()).setImage(binImage);
        ((ImageView) delete2.getGraphic()).setImage(binImage);
        ((ImageView) delete3.getGraphic()).setImage(binImage);
        ((ImageView) delete4.getGraphic()).setImage(binImage);
        ((ImageView) delete5.getGraphic()).setImage(binImage);
    }

    @FXML
    private void onToggleClicked(ActionEvent event) {
        ToggleButton clicked = (ToggleButton) event.getSource();
        ImageView imgView = null;

        switch (clicked.getId()) {
            case "toggle1": imgView = toggle1Image; break;
            case "toggle2": imgView = toggle2Image; break;
            case "toggle3": imgView = toggle3Image; break;
            case "toggle4": imgView = toggle4Image; break;
            case "toggle5": imgView = toggle5Image; break;
        }
        if (imgView != null) {
            if (clicked.isSelected()) {
                imgView.setImage(checkMark);
            } else {
                imgView.setImage(checkEmpty);
            }
        }

        System.out.println(clicked.getId() + " toggled to " + clicked.isSelected());
        // TODO: 可在此加切換遊戲槽或其他邏輯
    }

    @FXML
    private void onDeleteClicked(ActionEvent event) {
        Button clicked = (Button) event.getSource();
        ImageView imgView = (ImageView) clicked.getGraphic();

        // 判斷目前圖示來切換垃圾桶或刪除圖示
        if (imgView.getImage() == binImage) {
            imgView.setImage(deleteImage);
            System.out.println(clicked.getId() + " 刪除按鈕切換為刪除圖示");
        } else {
            imgView.setImage(binImage);
            System.out.println(clicked.getId() + " 刪除按鈕切換為垃圾桶圖示");
        }

        // TODO: 實際刪除邏輯放這
    }

    @FXML
    private void onAddClicked(ActionEvent event) {
        System.out.println("加號按鈕被點選");
        // TODO: 進入主畫面或新增槽位邏輯
    }
}
