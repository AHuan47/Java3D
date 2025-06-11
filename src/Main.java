import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.GameController;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Select.fxml"));
        scene.getStylesheets().add(getClass().getResource("/css/Select.css").toExternalForm());
        Parent root = loader.load();
        
        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.setTitle("Rubik's Cube");
        
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
