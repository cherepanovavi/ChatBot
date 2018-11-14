package Source;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Program extends Application {
	public static void main(String[] args)
    {
		ChatBot chatBot = new ChatBot();
//		System.out.println(parseQuestions());
        chatBot.consoleRealisation();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Chat bot");
        primaryStage.setMinWidth(1161);
        primaryStage.setMinHeight(500);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }



}
