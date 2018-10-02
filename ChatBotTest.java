import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class ChatBotTest {

	@Test
	void testGetEnding()
	{
		assertEquals(ChatBot.getEnding(0), "��");
		assertEquals(ChatBot.getEnding(5), "��");
		assertEquals(ChatBot.getEnding(46), "��");
		assertEquals(ChatBot.getEnding(21), "�");
		assertEquals(ChatBot.getEnding(1), "�");
		assertEquals(ChatBot.getEnding(22), "�");
		assertEquals(ChatBot.getEnding(3), "�");
		assertEquals(ChatBot.getEnding(104), "�");		
	}
	
	@Test
	void testAnalyzeAnswerHelp()
	{
		String name = "Ը���";
		UserState userState = new UserState(name);
		assertEquals(ChatBot.analyzeAnswer(userState, "��� ����� ������"), ChatBot.getHelp());
		assertEquals(ChatBot.analyzeAnswer(userState, "�������"), ChatBot.getHelp());
		assertEquals(ChatBot.analyzeAnswer(userState, " -h"), ChatBot.getHelp());
		assertEquals(ChatBot.analyzeAnswer(userState, "��� ������?"), ChatBot.getHelp());
		assertEquals(userState.getQuestionNumber(), 0);
	}

	@Test
	void testAnalyzeAnswerSkip()
	{
		String name = "Ը���";
		UserState userState = new UserState(name);
		assertEquals(ChatBot.analyzeAnswer(userState, " "), ChatBot.skipMessage);
		assertEquals(userState.getQuestionNumber(), 1);
		assertEquals(userState.getScore(), 0);
		assertEquals(ChatBot.analyzeAnswer(userState, "���� ����������"), ChatBot.skipMessage);
		assertEquals(userState.getQuestionNumber(), 2);
		assertEquals(userState.getScore(), 0);
		assertEquals(ChatBot.analyzeAnswer(userState, "��������� ������"),  ChatBot.skipMessage);
		assertEquals(userState.getQuestionNumber(), 3);
		assertEquals(userState.getScore(), 0);
	}
	
	@Test
	void testAnalyzeAnswerRightAnswer()
	{
		String name = "Ը���";
		UserState userState = new UserState(name);
		int scores = 0;
		for (int i = 0; i < ChatBot.questions.length; i ++)
		{
			String answer = ChatBot.questions[i].answers.get(0);
			assertEquals(ChatBot.analyzeAnswer(userState, answer), "���������� �����!");
			scores = scores + ChatBot.questions[i].cost;
			assertEquals(userState.getScore(), scores);
			assertEquals(userState.getQuestionNumber(), i + 1);
			
		}
	}
	
	@Test	
	void testAnalyzeAnswerRightAnswerOneCase()
		{
			String name = "Ը���";
			UserState userState = new UserState(name);
			int scores = 0;
			String answer = ChatBot.questions[0].answers.get(0);
			assertEquals(ChatBot.analyzeAnswer(userState, answer), "���������� �����!");
			scores = scores + ChatBot.questions[0].cost;
			assertEquals(userState.getScore(), scores);
			assertEquals(userState.getQuestionNumber(), 1);

	}
	
}
