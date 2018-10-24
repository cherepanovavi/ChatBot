package Source;

public class UserState {
    private int score;
    public final String name;
    private int questionNumber;
    private int questionAttempts;

    public UserState(String username)
    {
        score = 0;
        name = username;
        questionNumber = -1;
        questionAttempts = ChatBot.attemptsCount;
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
    public int getQuestionAttempts() {return questionAttempts;}
    public void spendAnAttempt() {questionAttempts -= 1;}
    public void moveToNextQuestion()
    {
        questionNumber += 1;
        questionAttempts = ChatBot.attemptsCount;
    }
}

