package Source;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Program extends Application {
	public static void main(String[] args)
    {
		ChatBot chatBot = new ChatBot();
        //chatBot.consoleRealisation();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Chat bot");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
