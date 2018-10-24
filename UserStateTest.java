package Source;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserStateTest {

	@Test
	void testCreateUser()
	{
		String name = "Фёдор";
		UserState user = new UserState(name);
		assertEquals(user.name, name);
		assertEquals(user.getScore(), 0);
		assertEquals(user.getQuestionNumber(), 0);
	}
	
	@Test
	void moveToNextQuestion()
	{
		UserState user = new UserState("Афанасий");
		int questNum = user.getQuestionNumber();
		user.moveToNextQuestion();
		assertEquals(user.getQuestionNumber(), questNum+1);
		assertEquals(user.getQuestionAttempts(), ChatBot.attemptsCount);
	}

	@Test
	void spendAttempt()
	{
		UserState user = new UserState("Афанасий");
		user.spendAnAttempt();
		assertEquals(user.getQuestionNumber(), 0);
		assertEquals(user.getQuestionAttempts(), ChatBot.attemptsCount -1);

	}

	@Test
	void addScores()
	{
		UserState user = new UserState("Афанасий");
		int scores1= user.getScore();
		user.addScores(2);
		assertEquals(scores1 + 2, user.getScore());

	}


}
