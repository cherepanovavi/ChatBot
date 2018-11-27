package Source.LogicFiles;

import java.util.List;

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
        return explanation;
    }

    public boolean isRightAnswer(String answer) {
        return answers.contains(answer);
    }
}
