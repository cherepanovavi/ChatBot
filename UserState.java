public class UserState {
    private int score;
    public final String name;
    private int questionNumber;

    public UserState(String username)
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

