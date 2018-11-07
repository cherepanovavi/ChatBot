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
		ChatBot chatBot = new ChatBot(parseQuestions());
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


	static Question[] parseQuestions(){
        ArrayList<Question> questions = new ArrayList<Question>();
        String s = "";
        Scanner in = null;
        try {
            in = new Scanner(new File("C:\\Users\\chere\\OneDrive\\Desktop\\untitled\\src\\Source\\question_base"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while(in.hasNext())
            s += in.nextLine() + "\r\n";
        in.close();
        JSONObject obj = null;
        try {
            obj = new JSONObject(s);
            JSONArray arr = obj.getJSONArray("base");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject question = arr.getJSONObject(i);

                String q = question.getString("question");
                List<String> answers = parseAnswersArray((question.getJSONArray("answers")));
                int cost = question.getInt("cost");
                String explanation = question.getString("explanation");
                String hint = question.getString("hint");
                questions.add(new Question(q, answers, cost, explanation, hint));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return questions.toArray(new Question[0]);
	}
    static List<String> parseAnswersArray(JSONArray arr){
	    ArrayList<String> answers = new ArrayList<String>();
        for (int i = 0; i < arr.length(); i++)
        {
            try {
                answers.add(arr.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return answers;
    }
}
