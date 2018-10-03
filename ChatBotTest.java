import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class ChatBotTest {

	@Test
	void testGetEnding()
	{
		ChatBot chatBot = new ChatBot();
		assertEquals(chatBot.getEnding(0), "��");
		assertEquals(chatBot.getEnding(5), "��");
		assertEquals(chatBot.getEnding(46), "��");
		assertEquals(chatBot.getEnding(21), "�");
		assertEquals(chatBot.getEnding(1), "�");
		assertEquals(chatBot.getEnding(22), "�");
		assertEquals(chatBot.getEnding(3), "�");
		assertEquals(chatBot.getEnding(104), "�");		
	}
	
	@Test
	void testAnalyzeAnswerHelp()
	{
		String name = "Ը���";
		UserState userState = new UserState(name);
		ChatBot chatBot = new ChatBot();
		assertEquals(chatBot.analyzeAnswer(userState, "��� ����� ������"), chatBot.getHelp());
		assertEquals(chatBot.analyzeAnswer(userState, "�������"), chatBot.getHelp());
		assertEquals(chatBot.analyzeAnswer(userState, " -h"), chatBot.getHelp());
		assertEquals(chatBot.analyzeAnswer(userState, "��� ������?"), chatBot.getHelp());
		assertEquals(userState.getQuestionNumber(), 0);
	}

	@Test
	void testAnalyzeAnswerSkip()
	{
		String name = "Ը���";
		UserState userState = new UserState(name);
		ChatBot chatBot = new ChatBot();
		assertEquals(chatBot.analyzeAnswer(userState, " "), chatBot.skipMessage);
		assertEquals(userState.getQuestionNumber(), 1);
		assertEquals(userState.getScore(), 0);
		assertEquals(chatBot.analyzeAnswer(userState, "���� ����������"), chatBot.skipMessage);
		assertEquals(userState.getQuestionNumber(), 2);
		assertEquals(userState.getScore(), 0);
		assertEquals(chatBot.analyzeAnswer(userState, "��������� ������"), chatBot.skipMessage);
		assertEquals(userState.getQuestionNumber(), 3);
		assertEquals(userState.getScore(), 0);
	}
	
	@Test
	void testAnalyzeAnswerRightAnswer()
	{
		String name = "Ը���";
		UserState userState = new UserState(name);
		int scores = 0;
		ChatBot chatBot = new ChatBot();
		for (int i = 0; i < chatBot.questions.length; i ++)
		{
			String answer = chatBot.questions[i].answers.get(0);
			assertEquals(chatBot.analyzeAnswer(userState, answer), "���������� �����!");
			scores = scores + chatBot.questions[i].cost;
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
			ChatBot chatBot = new ChatBot();
			String answer = chatBot.questions[0].answers.get(0);
			assertEquals(chatBot.analyzeAnswer(userState, answer), "���������� �����!");
			scores = scores + chatBot.questions[0].cost;
			assertEquals(userState.getScore(), scores);
			assertEquals(userState.getQuestionNumber(), 1);

	}
	
	@Test	
	void testAnalyzeAnswerWrongAnswerOneCase()
		{
			String name = "Ը���";
			UserState userState = new UserState(name);
			String answer = "����������";
			assertEquals(ChatBot.analyzeAnswer(userState, answer), "������������ �����");
			assertEquals(userState.getScore(), 0);
			assertEquals(userState.getQuestionNumber(), 1);

	}
}
