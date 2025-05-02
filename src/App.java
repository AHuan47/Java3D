import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        Pane root = new Pane(); // 空白容器
        Scene scene = new Scene(root, 600, 400); // 設定視窗大小
        stage.setScene(scene);
        stage.setTitle("空白測試視窗");
        stage.show();
    }
}