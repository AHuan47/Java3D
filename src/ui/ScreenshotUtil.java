package ui;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ScreenshotUtil {
    public static void saveSubSceneToThumbnail(SubScene subScene, String filename) {
        // 實際的儲存路徑
        String folderPath = "resources/assets/saves/Thumbnail";
        File folder = new File(folderPath);
        if (!folder.exists()) folder.mkdirs();  // 若資料夾不存在，則自動建立

        WritableImage image = new WritableImage((int) subScene.getWidth(), (int) subScene.getHeight());

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        subScene.snapshot(null, image);
        WritableImage cropped = cropImage(image, 250, 0, 500, 500);

        File file = new File(folder, filename);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(cropped, null), "png", file);
            System.out.println("已儲存：" + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static WritableImage cropImage(WritableImage source, int x, int y, int w, int h) {
        PixelReader reader = source.getPixelReader();
        return new WritableImage(reader, x, y, w, h);
    }
}
