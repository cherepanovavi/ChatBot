package Source.LogicFiles;

import javafx.css.Match;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Question {
    private final String question;
    private final List<String> answers;
    private final int cost;
    private final String explanation;
    private final String hint;

    public Question(String question, List<String> answers, int cost, String explanation, String hint) {
        this.question = question;
        this.answers = answers;
        this.cost = cost;
        this.explanation = explanation;
        this.hint = hint;
    }

    public int getCost(){
        return cost;
    }

    public String getHint() {
        return hint;
    }

    public String getQuestion() {
        return question;
    }

    public String getExplanation() {
        if (explanation.contains("://"))
            return parseExplanation(explanation);
        return explanation;
    }

    private String parseExplanation (String link){
        String page = downloadExplanation(link);
        StringBuilder result = new StringBuilder();
        Pattern pattern  = Pattern.compile("[<,>,&, ;]");
        Matcher matcher = pattern.matcher(page);
        Integer index;
        Integer previous_index = 0;
        while (matcher.find()){
            index = matcher.start();
            if (page.charAt(index) == '>'|| page.charAt(index)==';')
                previous_index = index;
            else if ((page.charAt(index)== '<' || page.charAt(index)=='&') && index != 0)
                result.append(page.substring(previous_index + 1, index));
        }
        return String.valueOf("<i>" + result + "</i>" +"\n"+ "<a href= \"" + link + "\"> Читать далее</a>");

    }

    private String downloadExplanation(String link){
        try {
            URL url = new URL(explanation);
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            Boolean started = false;
            String line = null;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                if (line.contains("<p>"))
                    started = true;
                if (started)
                    result.append(line);
                if(line.contains("</p"))
                    break;
            }
            return String.valueOf(result);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return " ";
    }

    public boolean isRightAnswer(String answer) {
        return answers.contains(answer);
    }
}
