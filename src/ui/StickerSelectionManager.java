package ui;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.Group;
import model.face.Direction;

public class StickerSelectionManager {
    private static Box selectedOutline = null;
    private static Box selectedSticker;
    private static Group parent = null;

    public static void select(Box sticker, Direction d) {
        // 移除舊的框
        if (selectedOutline != null && parent != null) {
            parent.getChildren().remove(selectedOutline);
        }

        selectedSticker = sticker;

        // 建立新的選取框（略大一點）
        Box outline = new Box(
                sticker.getWidth() + 3,
                sticker.getHeight() + 3,
                sticker.getDepth() + 3
        );
        outline.setMaterial(new PhongMaterial(Color.rgb(255, 255, 255, 0.2))); // 半透明白
        outline.setMouseTransparent(true); // 不遮擋點擊
        outline.setTranslateX(sticker.getTranslateX());
        outline.setTranslateY(sticker.getTranslateY());
        outline.setTranslateZ(sticker.getTranslateZ());

        // 加進 sticker 所屬的 Cubie
        parent = (Group) sticker.getParent();
        parent.getChildren().add(outline);

        Color color = ((PhongMaterial) sticker.getMaterial()).getDiffuseColor();
        String hex = colorToHex(color);  // 例如 "#880088"
        GameController.changeSelectColor(hex);

        // 更新狀態
        selectedOutline = outline;
    }

    private static String colorToHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255)
        );
    }

    public static Color returnSelectedColorHex() {
        if (selectedSticker == null) return null;

        PhongMaterial mat = (PhongMaterial) selectedSticker.getMaterial();
        if (mat == null) return null;
        return mat.getDiffuseColor();
    }

    public static void clearSelect(){
        selectedSticker = null;
        parent.getChildren().remove(selectedOutline);
        selectedOutline = null;
    }

}
