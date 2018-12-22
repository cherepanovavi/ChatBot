package LogicFiles;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Parser {

    public static Question[] parseQuestionsFromFile() {
        String s = readFromFile(new File("src/main/java/LogicFiles/question_base"));
        return parseQuestions(s).toArray(new Question[0]);
    }

    public static ObservableList<Question> parseQuestions(String s){
        ObservableList<Question> questions = FXCollections.observableArrayList();
        if (s!= null){
            JSONObject obj = null;
            try {
                obj = new JSONObject(s);
                JSONArray arr = obj.getJSONArray("base");
                for (int i = 0; i < arr.length(); i++) {
                    questions.add(parseQuestion(arr.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return questions;
    }

    public static String createJSON(List<Question> questions){
        JSONObject obj = new JSONObject();
        JSONArray json_questions = new JSONArray();
        for (int i = 0; i<questions.toArray().length; i++){
            json_questions.put(getJSONforQuestion(questions.get(i)));
        }
        try {
            obj.put("base", json_questions);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    public static JSONObject getJSONforQuestion(Question question){
        JSONObject obj = new JSONObject();
        try {
            obj.put("question", question.getQuestion());
            obj.put("cost", question.getCost());
            obj.put("explanation", question.getExplanation());
            obj.put("hint", question.getHint());
            JSONArray json_answers = new JSONArray();
            List<String> answers = question.getAnswers();
            for (int i = 0; i < answers.toArray().length; i++)
                json_answers.put(answers.get(i));
            obj.put("answers", json_answers);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static String readFromFile(File file) {
        StringBuilder s = new StringBuilder();
        Scanner in = null;
        try {
            in = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert in != null;
        while (in.hasNext())
            s.append(in.nextLine()).append("\r\n");
        in.close();
        return s.substring(0, s.length() - 2);
    }

    public static String createJSON(List<Question> questions){
        JSONObject obj = new JSONObject();
        JSONArray json_questions = new JSONArray();
        for (int i = 0; i<questions.toArray().length; i++){
            json_questions.put(getJSONforQuestion(questions.get(i)));
        }
        try {
            obj.put("base", json_questions);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            return obj.toString(4);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject getJSONforQuestion(Question question){
        JSONObject obj = new JSONObject();
        try {
            obj.put("question", question.getQuestion());
            obj.put("cost", question.getCost());
            obj.put("explanation", question.getExplanation());
            obj.put("hint", question.getHint());
            JSONArray json_answers = new JSONArray();
            List<String> answers = question.getAnswers();
            for (int i = 0; i < answers.toArray().length; i++)
                json_answers.put(answers.get(i));
            obj.put("answers", json_answers);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
    private static List<String> parseAnswersArray(JSONArray arr) {
        ArrayList<String> answers = new ArrayList<String>();
        for (int i = 0; i < arr.length(); i++) {
            try {
                answers.add(arr.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return answers;
    }

    private static Question parseQuestion(JSONObject question) {
        try {
            String q = question.getString("question");
            List<String> answers = parseAnswersArray((question.getJSONArray("answers")));
            int cost = question.getInt("cost");
            String explanation = question.getString("explanation");
            String hint = question.getString("hint");
            return new Question(q, answers, cost, explanation, hint);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getPageID(String s) {
        String pageId = null;
        JSONObject obj = null;
        try {
            obj = new JSONObject(s);
            JSONObject query = obj.getJSONObject("query");
            JSONArray search = query.getJSONArray("search");
            JSONObject r = search.getJSONObject(0);
            pageId = String.valueOf(r.getInt("pageid"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pageId;
    }

    private static String getFullURL(String id) {
        String fullURL = null;
        String URL = String.format("https://ru.wikipedia.org/w/api.php?action=query&prop=info&inprop=url&format=json&pageids=%s", id);
        String s = downloadPage(URL);
        try {
            JSONObject obj = new JSONObject(s);
            JSONObject query = obj.getJSONObject("query");
            JSONObject pages = query.getJSONObject("pages");
            JSONObject r = pages.getJSONObject(id);
            fullURL = r.getString("fullurl");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fullURL;
    }

    public static String getWikiSearchResult(String searchRequest) {
        String page = getPage(searchRequest);
        return parseFromHTML(page);
    }


    private static String getPage(String searchRequest) {
        String URL = String.format("https://ru.wikipedia.org/w/api.php?action=query&list=search&format=json&srsearch=%s", searchRequest);
        String searchResult = downloadPage(URL);
        String pageID = getPageID(searchResult);
        return getFullURL(pageID);
    }

    public static String decorate(String text) {
        int a = text.indexOf('.');
        int i = text.indexOf("https://");
        int j = text.indexOf("Переходим");
        if (i == -1)
            return text;
        String result = String.format("%s<i>%s</i>\n<a href= \"%s\">Читать далее</a>\n%s", text.substring(0, a+1), text.substring(a+1, i), text.substring(i, j), text.substring(j));
        return result;
    }

    private static String parseFromHTML(String link) {
        String result = null;
        try {
            Document doc = Jsoup.connect(link).get();
            Elements paragraphs = doc.select("p");
            result = "";
            for (Element p : paragraphs)
                if (result.equals(""))
                    result += p.text();
                else
                    break;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result + " " + link;
    }

    private static String downloadPage(String link) {
        try {
            URL url = new URL(link);
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            return String.valueOf(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return " ";
    }

    public static String getFileFromGit(){
        try
        {
            URL url = new URL("https://raw.githubusercontent.com/cherepanovavi/ChatBot/telegram/src/main/java/LogicFiles/question_base");
            String userName = "DeniskinBeast";
            String password = "DeniskinBeast2018";
            URLConnection con = url.openConnection();

            con.setRequestProperty("X-Requested-With", "Curl");
            String userpass = userName + ":" + password;
            String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
            con.setRequestProperty("Authorization", basicAuth);

            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line = reader.readLine();

            while(line != null){
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator());
                line = reader.readLine();
            }
            return stringBuilder.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
