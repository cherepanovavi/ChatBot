package Source.LogicFiles;

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
        String result = String.format("%s<i>%s</i>\n<a href= \"%s\"> Читать далее</a>\n%s", text.substring(0, a), text.substring(a+1, i - 1), text.substring(i, j-1), text.substring(j));
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
}
