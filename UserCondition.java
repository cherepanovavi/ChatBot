public class UserCondition {
    private int score;
    public final String name;
    public int questionNumber;

    public UserCondition(String username)
    {
        score = 0;
        name = username;
        questionNumber = 0;
    }
    public void addScores(int newScores)
    {
        score += newScores;
    }
    public int getQuestionNumber()
    {
        return questionNumber;
    }
    public int getScore()
    {
        return score;
    }
    public void moveToNextQuestion()
    {
        questionNumber += 1;
    }
}

