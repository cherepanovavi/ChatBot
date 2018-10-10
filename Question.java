package Source;

import java.util.List;

public class Question {
    public final String question;
    public final List<String> answers;
    public final int cost;
    public final String  explanation;
    public int attempts;
    public final String hint;
    public Question(String question, List<String> answer, int cost, String explanation, String hint)
    {
        this.question = question;
        this.answers = answer;
        this.cost = cost;
        this.explanation = explanation;
        this.hint = hint;
        attempts = 2;
    }
}
