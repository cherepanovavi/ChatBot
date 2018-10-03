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
	}


}
