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
		assertEquals(chatBot.analyzeAnswer(userState, "Мне нужна помощь").get(0), chatBot.getHelp());
		assertEquals(chatBot.analyzeAnswer(userState, "Справка").get(0), chatBot.getHelp());
		assertEquals(chatBot.analyzeAnswer(userState, " -h").get(0), chatBot.getHelp());
		assertEquals(chatBot.analyzeAnswer(userState, "Что делать?").get(0), chatBot.getHelp());
		assertEquals(userState.getQuestionNumber(), -1);
	}

	@Test
	void testAnalyzeAnswerSkip()
	{
		String name = "Фёдор";
		UserState userState = new UserState(name);
		ChatBot chatBot = new ChatBot();
		assertEquals(chatBot.analyzeAnswer(userState, " ").get(0), chatBot.getSkipMessage());
		assertEquals(userState.getQuestionNumber(), 0);
		assertEquals(userState.getScore(), 0);
		assertEquals(chatBot.analyzeAnswer(userState, "Хочу пропустить").get(0), chatBot.getSkipMessage());
		assertEquals(userState.getQuestionNumber(), 1);
		assertEquals(userState.getScore(), 0);
		assertEquals(chatBot.analyzeAnswer(userState, "Следующий вопрос").get(0), chatBot.getSkipMessage());
		assertEquals(userState.getQuestionNumber(), 2);
		assertEquals(userState.getScore(), 0);
	}

	//чатбот должен принимать базу вопросов извне, тогда понятно как тестить
//	@Test
//	void testAnalyzeAnswerRightAnswer()
//	{
//		String name = "Фёдор";
//		UserState userState = new UserState(name);
//		int scores = 0;
//		ChatBot chatBot = new ChatBot();
//		chatBot.analyzeAnswer(userState, "готов начать");
//		for (int i = 0; i < chatBot.questions.length; i ++)
//		{
//			String answer = chatBot.questions[i].answers.get(0);
//			assertEquals(chatBot.analyzeAnswer(userState, answer).get(0), "Правильный ответ!");
//			scores = scores + chatBot.questions[i].cost;
//			assertEquals(userState.getScore(), scores);
//			assertEquals(userState.getQuestionNumber(), i + 1);
//
//		}
//	}
//
//	@Test
//	void testAnalyzeAnswerRightAnswerOneCase()
//		{
//			String name = "Фёдор";
//			UserState userState = new UserState(name);
//			int scores = 0;
//			ChatBot chatBot = new ChatBot();
//			chatBot.analyzeAnswer(userState, "готов начать");
//			String answer = chatBot.questions[0].answers.get(0);
//			assertEquals(chatBot.analyzeAnswer(userState, answer).get(0), "Правильный ответ!");
//			scores = scores + chatBot.questions[0].cost;
//			assertEquals(userState.getScore(), scores);
//			assertEquals(userState.getQuestionNumber(), 1);
//
//	}
	
	@Test	
	void testAnalyzeAnswerWrongAnswerOneCase()
		{
			String name = "Фёдор";
			UserState userState = new UserState(name);
			ChatBot chatBot = new ChatBot();
			chatBot.analyzeAnswer(userState, "готов начать");
			String answer = "этонеответ";
			assertEquals(chatBot.analyzeAnswer(userState, answer).get(0), "Неправильный ответ, у тебя осталось 2 попытки");
			assertEquals(userState.getScore(), 0);
			assertEquals(userState.getQuestionNumber(), 0);
			assertEquals(userState.getQuestionAttempts(), 1);
	}
}
