import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class ChatBotTest {

	@Test
	void testGetEnding()
	{
		assertEquals(ChatBot.getEnding(0), "ов");
		assertEquals(ChatBot.getEnding(5), "ов");
		assertEquals(ChatBot.getEnding(46), "ов");
		assertEquals(ChatBot.getEnding(21), "о");
		assertEquals(ChatBot.getEnding(1), "о");
		assertEquals(ChatBot.getEnding(22), "а");
		assertEquals(ChatBot.getEnding(3), "а");
		assertEquals(ChatBot.getEnding(104), "а");		
	}
	
	@Test
	void testAnalyzeAnswerHelp()
	{
		String name = "Фёдор";
		UserState userState = new UserState(name);
		assertEquals(ChatBot.analyzeAnswer(userState, "Мне нужна помощь"), ChatBot.getHelp());
		assertEquals(ChatBot.analyzeAnswer(userState, "Справка"), ChatBot.getHelp());
		assertEquals(ChatBot.analyzeAnswer(userState, " -h"), ChatBot.getHelp());
		assertEquals(ChatBot.analyzeAnswer(userState, "Что делать?"), ChatBot.getHelp());
		assertEquals(userState.getQuestionNumber(), 0);
	}

	@Test
	void testAnalyzeAnswerSkip()
	{
		String name = "Фёдор";
		UserState userState = new UserState(name);
		assertEquals(ChatBot.analyzeAnswer(userState, " "), ChatBot.skipMessage);
		assertEquals(userState.getQuestionNumber(), 1);
		assertEquals(userState.getScore(), 0);
		assertEquals(ChatBot.analyzeAnswer(userState, "Хочу пропустить"), ChatBot.skipMessage);
		assertEquals(userState.getQuestionNumber(), 2);
		assertEquals(userState.getScore(), 0);
		assertEquals(ChatBot.analyzeAnswer(userState, "Следующий вопрос"),  ChatBot.skipMessage);
		assertEquals(userState.getQuestionNumber(), 3);
		assertEquals(userState.getScore(), 0);
	}
	
	@Test
	void testAnalyzeAnswerRightAnswer()
	{
		String name = "Фёдор";
		UserState userState = new UserState(name);
		int scores = 0;
		for (int i = 0; i < ChatBot.questions.length; i ++)
		{
			String answer = ChatBot.questions[i].answers.get(0);
			assertEquals(ChatBot.analyzeAnswer(userState, answer), "Правильный ответ!");
			scores = scores + ChatBot.questions[i].cost;
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
			String answer = ChatBot.questions[0].answers.get(0);
			assertEquals(ChatBot.analyzeAnswer(userState, answer), "Правильный ответ!");
			scores = scores + ChatBot.questions[0].cost;
			assertEquals(userState.getScore(), scores);
			assertEquals(userState.getQuestionNumber(), 1);

	}
	
}
