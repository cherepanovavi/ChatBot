package Source.LogicFiles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    public static Question[] parseQuestions() {
        List<Question> questions = new ArrayList<Question>();
        String s = readFromFile(new File("src/Source/LogicFiles/question_base"));
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
        return questions.toArray(new Question[0]);
    }

    public static String readFromFile(File file) {
        String s = "";
        Scanner in = null;
        try {
            in = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (in.hasNext())
            s += in.nextLine() + "\r\n";
        in.close();
        return s;


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

    public static String parseFromHTML(String link) {
        String page = downloadPage(link);
        String paragraph = getOnlyFirstParagraph(page);
        return String.valueOf("<i>" + parseParagraph(paragraph) + "</i>" + "\n" + "<a href= \"" + link + "\"> Читать далее</a>");
    }

    public static String parseParagraph(String paragraph) {
        StringBuilder result = new StringBuilder();
        Pattern pattern = Pattern.compile("[<>&;]");
        Matcher matcher = pattern.matcher(paragraph);
        Integer index;
        Integer previous_index = 0;
        while (matcher.find()) {
            index = matcher.start();
            char symbol = paragraph.charAt(index);
            if (symbol == '<' && index!=0)
                result.append(paragraph.substring(previous_index+1, index));
//            else if(symbol == '&')
//                result.append(" "+ paragraph.substring(previous_index+1, index));
            else if(symbol == '>' || symbol ==';')
                previous_index = index;
        }
        return String.valueOf(result);
    }

    public static String getOnlyFirstParagraph(String page) {
        StringBuilder result = new StringBuilder();
        Pattern pattern = Pattern.compile("p>");
        Matcher matcher = pattern.matcher(page);
        Integer index;
        Integer previous_index = 0;
        matcher.find();
        Integer beg_index = matcher.end();
        matcher.find();
        Integer end_index = matcher.start();
        return page.substring(beg_index, end_index-2);

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

}
