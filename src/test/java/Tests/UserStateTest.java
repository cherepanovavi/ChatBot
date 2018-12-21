package Tests;

import static org.junit.jupiter.api.Assertions.*;

import LogicFiles.ChatBot;
import LogicFiles.UserState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserStateTest {

    @Test
    public void testCreateUser() {
        String name = "Фёдор";
        UserState user = new UserState(name);
        assertEquals(user.getName(), name);
        assertEquals(user.getScore(), 0);
        assertEquals(user.getQuestionNumber(), -1);
    }

    @Test
    public void moveToNextQuestion() {
        UserState user = new UserState("Афанасий");
        int questNum = user.getQuestionNumber();
        user.moveToNextQuestion();
        assertEquals(user.getQuestionNumber(), questNum + 1);
        Assertions.assertEquals(user.getQuestionAttempts(), ChatBot.attemptsCount);
    }

    @Test
    public void spendAttempt() {
        UserState user = new UserState("Афанасий");
        user.spendAnAttempt();
        assertEquals(user.getQuestionNumber(), -1);
        assertEquals(user.getQuestionAttempts(), ChatBot.attemptsCount - 1);
    }

    @Test
    public void addScores() {
        UserState user = new UserState("Афанасий");
        int scores = user.getScore();
        user.addScores(2);
        assertEquals(scores + 2, user.getScore());
    }

    @Test
    public void restart(){
        String name = "User";
        UserState user = new UserState(name);
        int scores = user.getScore();
        int questNum = user.getQuestionNumber();
        int questAtt = user.getQuestionAttempts();
        user.addScores(2);
        user.moveToNextQuestion();
        user.spendAnAttempt();
        user.restart();
        assertEquals(scores, user.getScore());
        assertEquals(questNum, user.getQuestionNumber());
        assertEquals(questAtt, user.getQuestionAttempts());
    }
}
