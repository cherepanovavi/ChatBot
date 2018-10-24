package Source;

import java.util.List;

class Question {
    private final String question;
    private final List<String> answers;
    final int cost;
    private final String  explanation;
    private final String hint;
    Question(String question, List<String> answers, int cost, String explanation, String hint)
    {
        this.question = question;
        this.answers = answers;
        this.cost = cost;
        this.explanation = explanation;
        this.hint = hint;
    }
    String getHint() {return hint;}
    String getQuestion() {return  question;}
    String getExplanation() {return  explanation;}
    boolean isRightAnswer(String answer)
    {
        return answers.contains(answer);
    }

}
