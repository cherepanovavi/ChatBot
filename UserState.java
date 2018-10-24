package Source;

class UserState {
    private int score;
    final String name;
    private int questionNumber;
    private int questionAttempts;

    UserState(String username)
    {
        score = 0;
        name = username;
        questionNumber = -1;
        questionAttempts = ChatBot.attemptsCount;
    }
    void addScores(int newScores)
    {
        score += newScores;
    }
    int getQuestionNumber()
    {
        return questionNumber;
    }
    int getScore()
    {
        return score;
    }
    int getQuestionAttempts() {return questionAttempts;}
    void spendAnAttempt() {questionAttempts -= 1;}
    void moveToNextQuestion()
    {
        questionNumber += 1;
        questionAttempts = ChatBot.attemptsCount;
    }
}

