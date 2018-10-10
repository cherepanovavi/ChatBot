package Source;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class ChatBotTest {

	@Test
	void testGetEnding()
	{
		ChatBot chatBot = new ChatBot();
		assertEquals(chatBot.getEnding(0), "ов");
		assertEquals(chatBot.getEnding(5), "ов");
		assertEquals(chatBot.getEnding(46), "ов");
		assertEquals(chatBot.getEnding(21), "о");
		assertEquals(chatBot.getEnding(1), "о");
		assertEquals(chatBot.getEnding(22), "а");
		assertEquals(chatBot.getEnding(3), "а");
		assertEquals(chatBot.getEnding(104), "а");		
	}
	
	@Test
	void testAnalyzeAnswerHelp()
	{
		String name = "Фёдор";
		UserState userState = new UserState(name);
		ChatBot chatBot = new ChatBot();
		assertEquals(chatBot.analyzeAnswer(userState, "Мне нужна помощь"), chatBot.getHelp());
		assertEquals(chatBot.analyzeAnswer(userState, "Справка"), chatBot.getHelp());
		assertEquals(chatBot.analyzeAnswer(userState, " -h"), chatBot.getHelp());
		assertEquals(chatBot.analyzeAnswer(userState, "Что делать?"), chatBot.getHelp());
		assertEquals(userState.getQuestionNumber(), 0);
	}

	@Test
	void testAnalyzeAnswerSkip()
	{
		String name = "Фёдор";
		UserState userState = new UserState(name);
		ChatBot chatBot = new ChatBot();
		assertEquals(chatBot.analyzeAnswer(userState, " "), chatBot.skipMessage);
		assertEquals(userState.getQuestionNumber(), 1);
		assertEquals(userState.getScore(), 0);
		assertEquals(chatBot.analyzeAnswer(userState, "Хочу пропустить"), chatBot.skipMessage);
		assertEquals(userState.getQuestionNumber(), 2);
		assertEquals(userState.getScore(), 0);
		assertEquals(chatBot.analyzeAnswer(userState, "Следующий вопрос"), chatBot.skipMessage);
		assertEquals(userState.getQuestionNumber(), 3);
		assertEquals(userState.getScore(), 0);
	}
	
	@Test
	void testAnalyzeAnswerRightAnswer()
	{
		String name = "Фёдор";
		UserState userState = new UserState(name);
		int scores = 0;
		ChatBot chatBot = new ChatBot();
		for (int i = 0; i < chatBot.questions.length; i ++)
		{
			String answer = chatBot.questions[i].answers.get(0);
			assertEquals(chatBot.analyzeAnswer(userState, answer), "Правильный ответ!");
			scores = scores + chatBot.questions[i].cost;
			assertEquals(userState.getScore(), scores);
			assertEquals(userState.getQuestionNumber(), i + 1);
			
		}
	}
	
	@Test	
	void testAnalyzeAnswerRightAnswerOneCase()
		{
			String name = "Фёдор";
			UserState userState = new UserState(name);
			int scores = 0;
			ChatBot chatBot = new ChatBot();
			String answer = chatBot.questions[0].answers.get(0);
			assertEquals(chatBot.analyzeAnswer(userState, answer), "Правильный ответ!");
			scores = scores + chatBot.questions[0].cost;
			assertEquals(userState.getScore(), scores);
			assertEquals(userState.getQuestionNumber(), 1);

	}
	
	@Test	
	void testAnalyzeAnswerWrongAnswerOneCase()
		{
			String name = "Фёдор";
			UserState userState = new UserState(name);
			ChatBot chatBot = new ChatBot();
			String answer = "этонеответ";
			assertEquals(chatBot.analyzeAnswer(userState, answer), "Неправильный ответ");
			assertEquals(userState.getScore(), 0);
			assertEquals(userState.getQuestionNumber(), 1);
	}
}
