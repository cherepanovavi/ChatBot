import java.util.List;

public class Question {
    public final String question;
    public final List<String> answers;
    public final int cost;
    public Question(String question, List<String> answer, int cost)
    {
        this.question = question;
        this.answers = answer;
        this.cost = cost;
    }
}
