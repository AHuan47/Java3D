import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Cube;
import model.face.Direction;
import model.face.Face;
import model.sl.SLManager;

public class SaveLoadTest extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // 1. 建立新方塊並修改其中一面顏色
            Cube cube = new Cube();
            Face front = cube.faceMap.get(Direction.FRONT);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    front.getTiles()[i][j] = Color.PURPLE; // 改成紫色
                }
            }

            cube.customColors.put(Direction.FRONT, Color.PURPLE); // 客製顏色也改

            // 2. 儲存到 slot 1
            SLManager.save(cube, 1);
            System.out.println("儲存成功！");

            // 3. 載入 slot 1
            SLManager.LoadResult result = SLManager.load("0");
            System.out.println("載入成功！");

            // 4. 印出 FRONT 面顏色驗證
            Color[][] restored = result.faceColors.get(Direction.FRONT);
            System.out.println("FRONT 面顏色：");
            for (Color[] row : restored) {
                for (Color c : row) {
                    System.out.print(c.toString() + " ");
                }
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.exit(0); // 關閉 JavaFX 執行緒
    }

    public static void main(String[] args) {
        launch();
    }
}