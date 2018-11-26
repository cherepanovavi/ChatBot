package Source.Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class AppRun extends Application {

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Chat bot");
        primaryStage.setMinWidth(1161);
        primaryStage.setMinHeight(500);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
